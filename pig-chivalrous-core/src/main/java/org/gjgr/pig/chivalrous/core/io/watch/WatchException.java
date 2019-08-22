package org.gjgr.pig.chivalrous.core.io.watch;

import org.gjgr.pig.chivalrous.core.exceptions.ExceptionUtil;
import org.gjgr.pig.chivalrous.core.lang.StringCommand;

/**
 * 监听异常
 *
 * @author Looly
 */
public class WatchException extends RuntimeException {
    private static final long serialVersionUID = 8068509879445395353L;

    public WatchException(Throwable e) {
        super(ExceptionUtil.getMessage(e), e);
    }

    public WatchException(String message) {
        super(message);
    }

    public WatchException(String messageTemplate, Object... params) {
        super(StringCommand.format(messageTemplate, params));
    }

    public WatchException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public WatchException(Throwable throwable, String messageTemplate, Object... params) {
        super(StringCommand.format(messageTemplate, params), throwable);
    }
}
