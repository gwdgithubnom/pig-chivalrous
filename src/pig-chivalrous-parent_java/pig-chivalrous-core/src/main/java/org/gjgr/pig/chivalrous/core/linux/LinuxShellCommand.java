package org.gjgr.pig.chivalrous.core.linux;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author gwd
 * @Time 11-29-2018 Thursday
 * @Description: org.gjgr.pig.chivalrous.core:
 * @Target:
 * @More:
 */
public class LinuxShellCommand extends LinuxCommand {
    private Set<String> params;
    private String message;

    public LinuxShellCommand(String command) {
        this.command = command;
    }

    public LinuxShellCommand(String command, String...params) {
        this.params = new HashSet<>(Arrays.asList(params));
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Set<String> getParams() {
        return params;
    }

    public void setParams(Set<String> params) {
        this.params = params;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        if (params == null) {
            return command;
        } else {
            String param = params.stream().reduce((v1, v2) -> v1 + " " + v2).get();
            return command + " " + param;
        }
    }
}
