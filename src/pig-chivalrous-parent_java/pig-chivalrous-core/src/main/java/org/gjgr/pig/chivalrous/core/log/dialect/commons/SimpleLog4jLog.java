package org.gjgr.pig.chivalrous.core.log.dialect.commons;

import org.gjgr.pig.chivalrous.core.lang.StringCommand;
import org.gjgr.pig.chivalrous.core.log.AbstractLocationAwareLog;
import org.gjgr.pig.chivalrous.core.log.level.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Apache Commons Logging for Log4j
 *
 * @author Looly
 */
public class SimpleLog4jLog extends AbstractLocationAwareLog {
    private static final long serialVersionUID = -6843151523380063975L;

    private static final String FQCN = SimpleLog4jLog.class.getName();

    private final transient Logger logger;
    private final String name;

    // ------------------------------------------------------------------------- Constructor
    public SimpleLog4jLog(Logger logger, String name) {
        this.logger = logger;
        this.name = name;
    }

    public SimpleLog4jLog(Class<?> clazz) {
        this(LoggerFactory.getLogger(clazz), clazz.getName());
    }

    public SimpleLog4jLog(String name) {
        this(LoggerFactory.getLogger(name), name);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void log(org.gjgr.pig.chivalrous.core.log.level.Level level, String format, Object... arguments) {
        this.log(level, null, format, arguments);

    }

    @Override
    public void log(org.gjgr.pig.chivalrous.core.log.level.Level level, Throwable t, String format,
                    Object... arguments) {
        this.log(FQCN, level, t, format, arguments);

    }

    @Override
    public void log(String fqcn, org.gjgr.pig.chivalrous.core.log.level.Level level, Throwable t, String format,
                    Object... arguments) {
        Level log4jLevel;
        switch (level) {
            case TRACE:
                log4jLevel = Level.TRACE;
                logger.trace(FQCN, log4jLevel, StringCommand.format(format, arguments), t);
                break;
            case DEBUG:
                log4jLevel = Level.DEBUG;
                logger.debug(FQCN, log4jLevel, StringCommand.format(format, arguments), t);
                break;
            case INFO:
                log4jLevel = Level.INFO;
                logger.info(FQCN, log4jLevel, StringCommand.format(format, arguments), t);
                break;
            case WARN:
                log4jLevel = Level.WARN;
                logger.warn(FQCN, log4jLevel, StringCommand.format(format, arguments), t);
                break;
            case ERROR:
                log4jLevel = Level.ERROR;
                logger.error(FQCN, log4jLevel, StringCommand.format(format, arguments), t);
                break;
            default:
                throw new Error(StringCommand.format("Can not identify level: {}", level));
        }
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public void debug(String format, Object... arguments) {
        debug(null, format, arguments);
    }

    @Override
    public void debug(Throwable t, String format, Object... arguments) {
        logger.debug(FQCN, Level.DEBUG, StringCommand.format(format, arguments), t);

    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    @Override
    public void error(String format, Object... arguments) {
        error(null, format, arguments);

    }

    @Override
    public void error(Throwable t, String format, Object... arguments) {
        logger.error(FQCN, Level.ERROR, StringCommand.format(format, arguments), t);

    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();

    }

    @Override
    public void info(String format, Object... arguments) {
        info(null, format, arguments);

    }

    @Override
    public void info(Throwable t, String format, Object... arguments) {
        logger.info(FQCN, Level.INFO, StringCommand.format(format, arguments), t);

    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public void trace(String format, Object... arguments) {
        trace(null, format, arguments);

    }

    @Override
    public void trace(Throwable t, String format, Object... arguments) {
        logger.trace(FQCN, Level.TRACE, StringCommand.format(format, arguments), t);

    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    @Override
    public void warn(String format, Object... arguments) {
        warn(null, format, arguments);

    }

    @Override
    public void warn(Throwable t, String format, Object... arguments) {
        logger.warn(FQCN, Level.WARN, StringCommand.format(format, arguments), t);

    }
}
