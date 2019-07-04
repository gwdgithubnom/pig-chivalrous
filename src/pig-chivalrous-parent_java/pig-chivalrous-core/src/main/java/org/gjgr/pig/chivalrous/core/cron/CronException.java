package org.gjgr.pig.chivalrous.core.cron;

import org.gjgr.pig.chivalrous.core.lang.StringCommand;

/**
 * 定时任务异常
 *
 * @author xiaoleilu
 */
public class CronException extends RuntimeException {
    private static final long serialVersionUID = 8247610319171014183L;

    public CronException(Throwable e) {
        super(e.getMessage(), e);
    }

    public CronException(String message) {
        super(message);
    }

    public CronException(String messageTemplate, Object... params) {
        super(StringCommand.format(messageTemplate, params));
    }

    public CronException(Throwable throwable, String messageTemplate, Object... params) {
        super(StringCommand.format(messageTemplate, params), throwable);
    }
}
