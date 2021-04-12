package org.gjgr.pig.chivalrous.core.linux;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class ShellCommand extends Thread implements Serializable {

    public static final String RWXR_XR_X = "rwxr-xr-x";
    public static final String PID = "pid";
    private static final Logger log = LoggerFactory.getLogger(ShellCommand.class);
    private static ThreadLocal<ShellConfig> shellConfigThreadLocal = new ThreadLocal<>();

    public ShellCommand(String script, String output, String workPath, String taskId, String shellContent) {
        ShellConfig  shellConfig = new ShellConfig();
        shellConfig.setScript(script);
        shellConfig.setOutput(output);
        shellConfig.setWorkPath(workPath);
        shellConfig.setTaskId(taskId);
        shellConfig.setShellContent(shellContent);
    }

    /**
     * 工具方法，立即执行直到执行结束
     *
     * @param shell shell脚本
     * @param timeOutSeconds 超时时间
     * @return 返回日志
     */
    public static List<String> runShortShell(String shell, long timeOutSeconds) {
        Process process = null;
        List<String> processList = new ArrayList<String>();
        try {
            process = Runtime.getRuntime().exec(new String[] {"/bin/sh", "-c", shell});
            long startRunTime = System.currentTimeMillis() / 1000;
            boolean isStop = false;
            while (System.currentTimeMillis() / 1000 - startRunTime < timeOutSeconds && !isStop) {
                try {
                    isStop = true;
                } catch (IllegalThreadStateException e) {
                    isStop = false;
                }
                try {
                    Thread.sleep(shellConfigThreadLocal.get().getSleepInterval());
                } catch (InterruptedException e) {
                }
            }
            if (!isStop) {
                hardKill(process, shell);
                // 关闭stdin stdout stderr
                process.destroyForcibly();
            } else {
                BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = "";
                while ((line = input.readLine()) != null) {
                    processList.add(line);
                }
                BufferedReader errorInput = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                while ((line = errorInput.readLine()) != null) {
                    processList.add(line);
                }
                log.info("the input shell is {}, result is {}, lines is {}", shell, String.join("\n", processList), processList.size());
                input.close();
                errorInput.close();
            }
        } catch (Exception e) {
            log.error("runShortShell error is {}", ExceptionUtils.getFullStackTrace(e));
        }

        return processList;
    }

    private static int getProcessId(Process process) {
        int processId = 0;

        try {
            Field f = process.getClass().getDeclaredField(PID);
            f.setAccessible(true);

            processId = f.getInt(process);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }

        return processId;
    }

    private static void hardKill(Process process, String taskId) {
        int processId = getProcessId(process);
        if (processId != 0 && process.isAlive()) {
            try {
                String cmd = String.format("kill -15 %d", -processId);
                log.info("hard kill task:{}, process id:{}, cmd:{}", taskId, processId, cmd);
                Runtime.getRuntime().exec(cmd);
            } catch (IOException e) {
                log.error("kill attempt failed ", e);
            }
        }
    }

    /**
     * 解析日志，用于从日志中解析出需要的信息，如appId
     *
     * @param logLine 日志
     */
    public abstract void logParser(String logLine);

    /**
     * shell是否执行完成
     *
     * @return true -> 执行完成
     */
    public boolean isFinish() {
        return shellConfigThreadLocal.get().isFinish();
    }

    public void setFinish(boolean finish) {
        shellConfigThreadLocal.get().setFinish(finish);
    }

    private String buildShellCommand() {
        // generate scripts
        Path path = new File(shellConfigThreadLocal.get().getScript()).toPath();
        if (Files.exists(path)) {
            log.info("taskId: {} script have exits, path is: {} ", shellConfigThreadLocal.get().getTaskId(), shellConfigThreadLocal.get().getScript());
            return shellConfigThreadLocal.get().getScript();
        }
        // 755 否则没有权限
        Set<PosixFilePermission> perms = PosixFilePermissions.fromString(RWXR_XR_X);
        FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
        try {
            Path parent = path.getParent();
            Files.createDirectories(parent);
            Files.createFile(path, attr);
            Files.write(path, shellConfigThreadLocal.get().getShellContent().getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            log.error("taskId : {} script file write file, error: {}", shellConfigThreadLocal.get().getTaskId(), ExceptionUtils.getFullStackTrace(e));
            shellConfigThreadLocal.get().setExitCode(-1);
            shellConfigThreadLocal.get().setFinish(true);
            return "";
        }
        return shellConfigThreadLocal.get().getScript();
    }

    public void forceKillShell() {
        shellConfigThreadLocal.get().setForceKill(true);
    }

    /**
     * 同步执行脚本,并且阻塞等待执行完成.
     * 可以主动杀死 (forceKillShell), 或者超时自杀.
     */
    @Override
    public void run() {
        if (checkWorkPath()) {
            shellConfigThreadLocal.get().setFinish(true);
            shellConfigThreadLocal.get().setExitCode(-1);
            return;
        }
        String runningShellFile = buildShellCommand();
        if ("".equals(runningShellFile)) {
            return;
        }
        shellConfigThreadLocal.get().setStartRunTimeSeconds(System.currentTimeMillis() / 1000);
        ProcessBuilder pb = new ProcessBuilder(runningShellFile)
            .redirectErrorStream(true)
            .directory(new File(shellConfigThreadLocal.get().getWorkPath()));
        try {
            Process process = pb.start();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            // 检查
            boolean runningStatus = false;
            while (!runningStatus) {
                try {
                    Thread.sleep(getSleepInterval());
                } catch (InterruptedException e) {
                    log.error("taskId: {} , errorMessage: {}", shellConfigThreadLocal.get().getTaskId(), e.getMessage(), e);
                }
                log.info("check shell running state for taskId: {} ", shellConfigThreadLocal.get().getTaskId());
                String singleLine;
                while ((singleLine = bufferedReader.readLine()) != null && !shellConfigThreadLocal.get().isForceKill()) {
                    // 解析shell日志
                    logParser(singleLine);
                }
                if (shellConfigThreadLocal.get().isForceKill()) {
                    hardKill(process, shellConfigThreadLocal.get().taskId);
                    break;
                }
                if (System.currentTimeMillis() / 1000 - shellConfigThreadLocal.get().getStartRunTimeSeconds() > shellConfigThreadLocal.get().getTimeOutSeconds()) {
                    hardKill(process, shellConfigThreadLocal.get().taskId);
                    break;
                }
                try {
                    shellConfigThreadLocal.get().setExitCode(process.exitValue());
                    runningStatus = true;
                } catch (IllegalThreadStateException e) {
                    runningStatus = false;
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            log.error("taskId: {} , errorMessage: {} ", shellConfigThreadLocal.get().getTaskId(), e.getMessage(), e);
            shellConfigThreadLocal.get().setExitCode(-1);
            String checkFuserCommand = String.format("ps aux|grep `fuser %s |awk -F ':' '{print $1}' `", runningShellFile);
            List<String> fuserLogs = runShortShell(checkFuserCommand, 60);
            log.info("who use the script file, taskId: {} , fileName: {}, fuserLog: {}",
                shellConfigThreadLocal.get().getTaskId(), runningShellFile, String.join("\n", fuserLogs));
        }
        shellConfigThreadLocal.get().setFinish(true);
        shellConfigThreadLocal.get().setEndTimeSeconds(System.currentTimeMillis() / 1000);
    }

    private boolean checkWorkPath() {
        if (StringUtils.isNotBlank(shellConfigThreadLocal.get().getWorkPath())) {
            try {
                FileUtils.forceMkdir(new File(shellConfigThreadLocal.get().getWorkPath()));
            } catch (IOException e) {
                log.error("[SHELL] create shell work path error.", e);
            }
        }
        return false;
    }

    public int exitCode() {
        return shellConfigThreadLocal.get().getExitCode();
    }

    public int getSleepInterval() {
        return shellConfigThreadLocal.get().getSleepInterval();
    }

    /**
     * platform error
     *
     * @return true if platform's error otherwise false.
     */
    public final boolean isFinishWithPlatformError() {
        int platformErrorTimeThreshold = 120;
        return shellConfigThreadLocal.get().isFinish() && shellConfigThreadLocal.get().getExitCode() != 0 && shellConfigThreadLocal.get().getEndTimeSeconds() - shellConfigThreadLocal.get().getStartRunTimeSeconds() < platformErrorTimeThreshold;
    }
}
