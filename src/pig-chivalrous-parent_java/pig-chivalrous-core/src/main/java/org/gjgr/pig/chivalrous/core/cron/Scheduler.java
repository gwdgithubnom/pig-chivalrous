package org.gjgr.pig.chivalrous.core.cron;

import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.UUID;

import org.gjgr.pig.chivalrous.core.convert.Convert;
import org.gjgr.pig.chivalrous.core.cron.listener.TaskListener;
import org.gjgr.pig.chivalrous.core.cron.listener.TaskListenerManager;
import org.gjgr.pig.chivalrous.core.cron.pattern.CronPattern;
import org.gjgr.pig.chivalrous.core.cron.task.InvokeTask;
import org.gjgr.pig.chivalrous.core.cron.task.RunnableTask;
import org.gjgr.pig.chivalrous.core.cron.task.Task;
import org.gjgr.pig.chivalrous.core.lang.CollectionCommand;
import org.gjgr.pig.chivalrous.core.setting.Setting;
import org.gjgr.pig.chivalrous.core.util.ThreadUtil;

/**
 * 任务调度器<br>
 * <p>
 * 调度器启动流程：<br>
 * <p>
 * 
 * <pre>
 * 启动Timer -> 启动TaskLauncher -> 启动TaskExecutor
 * </pre>
 * <p>
 * 调度器关闭流程:<br>
 * <p>
 * 
 * <pre>
 * 关闭Timer -> 关闭所有运行中的TaskLauncher -> 关闭所有运行中的TaskExecutor
 * </pre>
 * <p>
 * 其中：
 * <p>
 * 
 * <pre>
 * <strong>TaskLauncher</strong>：定时器每分钟调用一次（如果{@link Scheduler#isMatchSecond()}为<code>true</code>每秒调用一次），
 * 负责检查<strong>TaskTable</strong>是否有匹配到此时间运行的Task
 * </pre>
 * <p>
 * 
 * <pre>
 * <strong>TaskExecutor</strong>：TaskLauncher匹配成功后，触发TaskExecutor执行具体的作业，执行完毕销毁
 * </pre>
 *
 * @author Looly
 */
public class Scheduler {
    /**
     * 是否支持秒匹配
     */
    protected boolean matchSecond = false;
    /**
     * 是否支持年匹配
     */
    protected boolean matchYear = false;
    /**
     * 是否为守护线程
     */
    protected boolean daemon;
    /**
     * 定时任务表
     */
    protected TaskTable taskTable = new TaskTable(this);
    /**
     * 启动器管理器
     */
    protected TaskLauncherManager taskLauncherManager;
    /**
     * 执行器管理器
     */
    protected TaskExecutorManager taskExecutorManager;
    /**
     * 监听管理器列表
     */
    protected TaskListenerManager listenerManager = new TaskListenerManager();
    private Object lock = new Object();
    /**
     * 时区
     */
    private TimeZone timezone;
    /**
     * 是否已经启动
     */
    private boolean started = false;
    /**
     * 定时器
     */
    private CronTimer timer;

    // --------------------------------------------------------- Getters and Setters start

    /**
     * 获得时区，默认为 {@link TimeZone#getDefault()}
     *
     * @return 时区
     */
    public TimeZone getTimeZone() {
        return timezone != null ? timezone : TimeZone.getDefault();
    }

    /**
     * 设置时区
     *
     * @param timezone 时区
     * @return this
     */
    public Scheduler setTimeZone(TimeZone timezone) {
        this.timezone = timezone;
        return this;
    }

    /**
     * 设置是否为守护线程<br>
     * 默认非守护线程
     *
     * @param on <code>true</code>为守护线程，否则非守护线程
     * @throws CronException
     */
    public void setDaemon(boolean on) throws CronException {
        synchronized (lock) {
            if (started) {
                throw new CronException("Scheduler already started!");
            }
            this.daemon = on;
        }
    }

    /**
     * 是否为守护线程
     *
     * @return 是否为守护线程
     */
    public boolean isDeamon() {
        return this.daemon;
    }

    /**
     * 是否支持秒匹配
     *
     * @return <code>true</code>使用，<code>false</code>不使用
     */
    public boolean isMatchSecond() {
        return this.matchSecond;
    }

    /**
     * 设置是否支持秒匹配，默认不使用
     *
     * @param isMatchSecond <code>true</code>支持，<code>false</code>不支持
     * @return this
     */
    public Scheduler setMatchSecond(boolean isMatchSecond) {
        this.matchSecond = isMatchSecond;
        return this;
    }

    /**
     * 是否支持年匹配
     *
     * @return <code>true</code>使用，<code>false</code>不使用
     */
    public boolean isMatchYear() {
        return this.matchYear;
    }

    /**
     * 设置是否支持年匹配，默认不使用
     *
     * @param isMatchYear <code>true</code>支持，<code>false</code>不支持
     * @return this
     */
    public Scheduler setMatchYear(boolean isMatchYear) {
        this.matchYear = isMatchYear;
        return this;
    }

    /**
     * 增加监听器
     *
     * @param listener {@link TaskListener}
     * @return this
     */
    public Scheduler addListener(TaskListener listener) {
        this.listenerManager.addListener(listener);
        return this;
    }

    /**
     * 移除监听器
     *
     * @param listener {@link TaskListener}
     * @return this
     */
    public Scheduler removeListener(TaskListener listener) {
        this.listenerManager.removeListener(listener);
        return this;
    }
    // --------------------------------------------------------- Getters and Setters end

    // -------------------------------------------------------------------- shcedule start

    /**
     * 批量加入配置文件中的定时任务<br>
     * 配置文件格式为： xxx.xxx.xxx.Class.method = * * * * *
     *
     * @param cronSetting 定时任务设置文件
     */
    public Scheduler schedule(Setting cronSetting) {
        if (CollectionCommand.isNotEmpty(cronSetting)) {
            for (Entry<Object, Object> entry : cronSetting.entrySet()) {
                final String jobClass = Convert.toStr(entry.getKey());
                final String pattern = Convert.toStr(entry.getValue());
                try {
                    schedule(pattern, new InvokeTask(jobClass));
                } catch (Exception e) {
                    throw new CronException(e, "Schedule [{}] [{}] error!", pattern, jobClass);
                }
            }
        }
        return this;
    }

    /**
     * 新增Task，使用随机UUID
     *
     * @param pattern {@link CronPattern}对应的String表达式
     * @param task {@link Runnable}
     * @return ID
     */
    public String schedule(String pattern, Runnable task) {
        return schedule(pattern, new RunnableTask(task));
    }

    /**
     * 新增Task，使用随机UUID
     *
     * @param pattern {@link CronPattern}对应的String表达式
     * @param task {@link Task}
     * @return ID
     */
    public String schedule(String pattern, Task task) {
        String id = UUID.randomUUID().toString();
        schedule(id, pattern, task);
        return id;
    }

    /**
     * 新增Task
     *
     * @param id ID，为每一个Task定义一个ID
     * @param pattern {@link CronPattern}对应的String表达式
     * @param task {@link Runnable}
     * @return this
     */
    public Scheduler schedule(String id, String pattern, Runnable task) {
        return schedule(id, new CronPattern(pattern), new RunnableTask(task));
    }

    /**
     * 新增Task
     *
     * @param id ID，为每一个Task定义一个ID
     * @param pattern {@link CronPattern}对应的String表达式
     * @param task {@link Task}
     * @return this
     */
    public Scheduler schedule(String id, String pattern, Task task) {
        return schedule(id, new CronPattern(pattern), task);
    }

    /**
     * 新增Task
     *
     * @param id ID，为每一个Task定义一个ID
     * @param pattern {@link CronPattern}
     * @param task {@link Task}
     * @return this
     */
    public Scheduler schedule(String id, CronPattern pattern, Task task) {
        taskTable.add(id, pattern, task);
        return this;
    }

    /**
     * 移除Task
     *
     * @param id Task的ID
     */
    public synchronized void deschedule(String id) throws IndexOutOfBoundsException {
        this.taskTable.remove(id);
    }
    // -------------------------------------------------------------------- shcedule end

    /**
     * @return 是否已经启动
     */
    public boolean isStarted() {
        return this.started;
    }

    /**
     * 启动
     *
     * @return this
     */
    public Scheduler start() {
        synchronized (lock) {
            if (this.started) {
                throw new CronException("Schedule is started!");
            }

            this.taskLauncherManager = new TaskLauncherManager(this);
            this.taskExecutorManager = new TaskExecutorManager(this);

            // Start CronTimer
            timer = new CronTimer(this);
            timer.setDaemon(this.daemon);
            timer.start();
            this.started = true;
        }
        return this;
    }

    /**
     * 停止定时任务
     *
     * @return this
     */
    public Scheduler stop() {
        synchronized (lock) {
            if (!started) {
                throw new IllegalStateException("Scheduler not started");
            }

            // 停止CronTimer
            ThreadUtil.interupt(this.timer, true);

            // 停止所有TaskLauncher
            taskLauncherManager.destroy();
            // 停止所有TaskExecutor
            this.taskExecutorManager.destroy();

            // 修改标志
            started = false;
        }
        return this;
    }

    // -------------------------------------------------------------------- notify start
    // -------------------------------------------------------------------- notify end
}
