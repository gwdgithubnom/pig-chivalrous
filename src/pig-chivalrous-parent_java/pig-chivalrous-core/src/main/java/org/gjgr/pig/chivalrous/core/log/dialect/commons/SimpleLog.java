package org.gjgr.pig.chivalrous.core.log.dialect.commons;

import org.gjgr.pig.chivalrous.core.lang.StringCommand;
import org.gjgr.pig.chivalrous.core.log.AbstractLog;
import org.gjgr.pig.chivalrous.core.log.level.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Apache Commons Logging
 *
 * @author Looly
 */
public class SimpleLog extends AbstractLog {
    private static final long serialVersionUID = -6843151523380063975L;

    private final transient Logger logger;
    private final String name;

    // ------------------------------------------------------------------------- Constructor
    public SimpleLog(Logger logger, String name) {
        this.logger = logger;
        this.name = name;
    }

    public SimpleLog(Class<?> clazz) {
        this(LoggerFactory.getLogger(clazz), clazz.getName());
    }

    public SimpleLog(String name) {
        this(LoggerFactory.getLogger(name), name);
    }

    @Override
    public String getName() {
        return this.name;
    }

    // ------------------------------------------------------------------------- Log
    @Override
    public void log(Level level, String format, Object...arguments) {
        switch (level) {
            case TRACE:
                trace(format, arguments);
                break;
            case DEBUG:
                debug(format, arguments);
                break;
            case INFO:
                info(format, arguments);
                break;
            case WARN:
                warn(format, arguments);
                break;
            case ERROR:
                error(format, arguments);
                break;
            default:
                throw new Error(StringCommand.format("Can not identify level: {}", level));
        }
    }

    @Override
    public void log(Level level, Throwable t, String format, Object...arguments) {
        switch (level) {
            case TRACE:
                trace(t, format, arguments);
                break;
            case DEBUG:
                debug(t, format, arguments);
                break;
            case INFO:
                info(t, format, arguments);
                break;
            case WARN:
                warn(t, format, arguments);
                break;
            case ERROR:
                error(t, format, arguments);
                break;
            default:
                throw new Error(StringCommand.format("Can not identify level: {}", level));
        }
    }

    // ------------------------------------------------------------------------- Trace
    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public void trace(String format, Object...arguments) {
        if (isTraceEnabled()) {
            logger.trace(StringCommand.format(format, arguments));
        }
    }

    @Override
    public void trace(Throwable t, String format, Object...arguments) {
        if (isTraceEnabled()) {
            logger.trace(StringCommand.format(format, arguments), t);
        }
    }

    // ------------------------------------------------------------------------- Debug
    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public void debug(String format, Object...arguments) {
        if (isDebugEnabled()) {
            logger.debug(StringCommand.format(format, arguments));
        }
    }

    @Override
    public void debug(Throwable t, String format, Object...arguments) {
        if (isDebugEnabled()) {
            logger.debug(StringCommand.format(format, arguments), t);
        }
    }

    // ------------------------------------------------------------------------- Info
    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public void info(String format, Object...arguments) {
        if (isInfoEnabled()) {
            logger.info(StringCommand.format(format, arguments));
        }
    }

    @Override
    public void info(Throwable t, String format, Object...arguments) {
        if (isInfoEnabled()) {
            logger.info(StringCommand.format(format, arguments), t);
        }
    }

    // ------------------------------------------------------------------------- Warn
    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    @Override
    public void warn(String format, Object...arguments) {
        if (isWarnEnabled()) {
            logger.warn(StringCommand.format(format, arguments));
        }
    }

    @Override
    public void warn(Throwable t, String format, Object...arguments) {
        if (isWarnEnabled()) {
            logger.warn(StringCommand.format(format, arguments), t);
        }
    }

    // ------------------------------------------------------------------------- Error
    @Override
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    @Override
    public void error(String format, Object...arguments) {
        if (isErrorEnabled()) {
            logger.error(StringCommand.format(format, arguments));
        }
    }

    @Override
    public void error(Throwable t, String format, Object...arguments) {
        if (isErrorEnabled()) {
            logger.warn(StringCommand.format(format, arguments), t);
        }
    }

    // ------------------------------------------------------------------------- Private method
}
