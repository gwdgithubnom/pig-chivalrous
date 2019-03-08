package org.gjgr.pig.chivalrous.core.log.dialect.log4j;

import org.apache.logging.log4j.Level;
import org.gjgr.pig.chivalrous.core.log.AbstractLocationAwareLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <a href="http://logging.apache.org/log4j/1.2/index.html">Apache Log4J</a> log.<br>
 *
 * @author Looly
 */
public class Log4jLog extends AbstractLocationAwareLog {
    private static final long serialVersionUID = -6843151523380063975L;
    private static final String FQCN = Log4jLog.class.getName();

    private transient Logger logger = null;
    // private final transient Logger logger = null;

    // ------------------------------------------------------------------------- Constructor
    public Log4jLog(Logger logger) {
        this.logger = logger;
    }

    public Log4jLog(Class<?> clazz) {
        this(LoggerFactory.getLogger(clazz));
    }

    public Log4jLog(String name) {
        this(LoggerFactory.getLogger(name));
    }

    @Override
    public String getName() {
        return logger.getName();
    }

    @Override
    public void log(org.gjgr.pig.chivalrous.core.log.level.Level level, String format, Object...arguments) {

    }

    @Override
    public void log(org.gjgr.pig.chivalrous.core.log.level.Level level, Throwable t, String format,
            Object...arguments) {

    }

    // ------------------------------------------------------------------------- Trace
    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public void trace(String format, Object...arguments) {
        trace(null, format, arguments);
    }

    @Override
    public void trace(Throwable t, String format, Object...arguments) {
        // logger.log(FQCN, Level.TRACE, StringCommand.format(format, arguments), t);
    }

    // ------------------------------------------------------------------------- Debug
    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public void debug(String format, Object...arguments) {
        debug(null, format, arguments);
    }

    @Override
    public void debug(Throwable t, String format, Object...arguments) {
        // logger.log(FQCN, Level.DEBUG, StringCommand.format(format, arguments), t);
    }

    // ------------------------------------------------------------------------- Info
    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public void info(String format, Object...arguments) {
        info(null, format, arguments);
    }

    @Override
    public void info(Throwable t, String format, Object...arguments) {
        // logger.log(FQCN, Level.INFO, StringCommand.format(format, arguments), t);
    }

    // ------------------------------------------------------------------------- Warn
    @Override
    public boolean isWarnEnabled() {

        // return logger.isEnabledFor(Level.WARN);
        return true;
    }

    @Override
    public void warn(String format, Object...arguments) {
        warn(null, format, arguments);
    }

    @Override
    public void warn(Throwable t, String format, Object...arguments) {
        // logger.log(FQCN, Level.WARN, StringCommand.format(format, arguments), t);
    }

    // ------------------------------------------------------------------------- Error
    @Override
    public boolean isErrorEnabled() {

        // return logger.isEnabledFor(Level.ERROR);
        return true;
    }

    @Override
    public void error(String format, Object...arguments) {
        error(null, format, arguments);
    }

    @Override
    public void error(Throwable t, String format, Object...arguments) {
        // logger.log(FQCN, Level.ERROR, StringCommand.format(format, arguments), t);
    }

    // ------------------------------------------------------------------------- Log

    public void log(Level level, String format, Object...arguments) {
        log(level, null, format, arguments);
    }

    public void log(Level level, Throwable t, String format, Object...arguments) {
        this.log(FQCN, level, t, format, arguments);
    }

    public void log(String fqcn, Level level, Throwable t, String format, Object...arguments) {
        Level log4jLevel;
        // @TODO
        /*
         * switch (level) { case TRACE: log4jLevel = Level.TRACE; break; case DEBUG: log4jLevel = Level.DEBUG; break;
         * case INFO: log4jLevel = Level.INFO; break; case WARN: log4jLevel = Level.WARN; break; case ERROR: log4jLevel
         * = Level.ERROR; break; default: throw new Error(StringCommand.format("Can not identify level: {}", level)); }
         */
        // logger.log(fqcn, log4jLevel, StringCommand.format(format, arguments), t);
    }

    @Override
    public void log(String fqcn, org.gjgr.pig.chivalrous.core.log.level.Level level, Throwable t, String format,
            Object...arguments) {
        // TODO
    }

    // ------------------------------------------------------------------------- Private method
}
