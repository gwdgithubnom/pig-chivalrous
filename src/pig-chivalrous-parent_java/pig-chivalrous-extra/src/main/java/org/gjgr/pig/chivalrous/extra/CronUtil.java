package org.gjgr.pig.chivalrous.extra;

import it.sauronsoftware.cron4j.Scheduler;
import it.sauronsoftware.cron4j.Task;
import org.gjgr.pig.chivalrous.core.convert.Convert;
import org.gjgr.pig.chivalrous.core.exceptions.UtilException;
import org.gjgr.pig.chivalrous.core.lang.ClassCommand;
import org.gjgr.pig.chivalrous.core.setting.Setting;

import java.util.Map.Entry;

/**
 * 定时任务工具类
 *
 * @author xiaoleilu
 * @deprecated Please use [hutool-cron] module
 */
public class CronUtil {
    // private final static Log log = StaticLog.get();

    /**
     * Crontab配置文件
     */
    public static final String CRONTAB_CONFIG_PATH = "config/cron4j.setting";

    private static final Scheduler scheduler = new Scheduler();
    private static Setting crontabSetting;

    /**
     * 自定义定时任务配置文件
     *
     * @param cronSetting 定时任务配置文件
     */
    public static void setCronSetting(Setting cronSetting) {
        crontabSetting = cronSetting;
    }

    /**
     * 自定义定时任务配置文件路径
     *
     * @param cronSettingPath 定时任务配置文件路径（相对绝对都可）
     */
    public static void setCronSetting(String cronSettingPath) {
        crontabSetting = new Setting(cronSettingPath, Setting.DEFAULT_CHARSET, false);
    }

    /**
     * 加入定时任务
     *
     * @param schedulingPattern 定时任务执行时间的crontab表达式
     * @param task              任务
     * @return 定时任务ID
     */
    public static String schedule(String schedulingPattern, Task task) {
        return scheduler.schedule(schedulingPattern, task);
    }

    /**
     * 加入定时任务
     *
     * @param schedulingPattern 定时任务执行时间的crontab表达式
     * @param task              任务
     * @return 定时任务ID
     */
    public static String schedule(String schedulingPattern, Runnable task) {
        return scheduler.schedule(schedulingPattern, task);
    }

    /**
     * 批量加入配置文件中的定时任务
     *
     * @param cronSetting 定时任务设置文件
     */
    public static void schedule(Setting cronSetting) {
        for (Entry<Object, Object> entry : cronSetting.entrySet()) {
            final String jobClass = Convert.toStr(entry.getKey());
            final String pattern = Convert.toStr(entry.getValue());
            try {
                final Runnable job = ClassCommand.newInstance(jobClass);
                schedule(pattern, job);
                // log.info("Schedule [{} {}] added.", pattern, jobClass);
            } catch (Exception e) {
                e.printStackTrace();
                // log.error(e, "Schedule [%s %s] add error!", pattern, jobClass);
            }
        }
    }

    /**
     * 开始
     */
    public static synchronized void start() {
        if (null == crontabSetting) {
            setCronSetting(CRONTAB_CONFIG_PATH);
        }
        if (scheduler.isStarted()) {
            throw new UtilException("Scheduler has been started, please stop it first!");
        }

        schedule(crontabSetting);
        scheduler.start();
    }

    /**
     * 重新启动定时任务<br>
     * 重新启动定时任务会清除动态加载的任务
     */
    public static synchronized void restart() {
        if (null != crontabSetting) {
            crontabSetting.load();
        }
        if (scheduler.isStarted()) {
            scheduler.stop();
        }

        schedule(crontabSetting);
        scheduler.start();
    }

    /**
     * 停止
     */
    public static synchronized void stop() {
        scheduler.stop();
    }

    /**
     * 移除任务
     *
     * @param schedulerId 任务ID
     */
    public void remove(String schedulerId) {
        scheduler.deschedule(schedulerId);
    }

    /**
     * @return 获得cron4j的Scheduler对象
     */
    public Scheduler getScheduler() {
        return scheduler;
    }

}
