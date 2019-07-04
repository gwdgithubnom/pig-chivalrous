package org.gjgr.pig.chivalrous.core.setting;

import org.gjgr.pig.chivalrous.core.lang.StringCommand;

/**
 * 设置异常
 *
 * @author xiaoleilu
 */
public class SettingException extends Exception {
    private static final long serialVersionUID = -4134588858314744501L;

    public SettingException(Throwable e) {
        super(e);
    }

    public SettingException(String message) {
        super(message);
    }

    public SettingException(String messageTemplate, Object... params) {
        super(StringCommand.format(messageTemplate, params));
    }

    public SettingException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public SettingException(Throwable throwable, String messageTemplate, Object... params) {
        super(StringCommand.format(messageTemplate, params), throwable);
    }
}
