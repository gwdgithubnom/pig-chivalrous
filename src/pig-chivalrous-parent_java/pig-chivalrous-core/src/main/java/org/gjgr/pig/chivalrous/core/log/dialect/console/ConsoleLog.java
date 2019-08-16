package org.gjgr.pig.chivalrous.core.log.dialect.console;

import org.gjgr.pig.chivalrous.core.date.DateTimeCommand;
import org.gjgr.pig.chivalrous.core.lang.Console;
import org.gjgr.pig.chivalrous.core.lang.Dict;
import org.gjgr.pig.chivalrous.core.lang.StringCommand;
import org.gjgr.pig.chivalrous.core.log.AbstractLog;
import org.gjgr.pig.chivalrous.core.log.level.Level;

/**
 * 利用System.out.println()打印日志
 *
 * @author Looly
 */
public class ConsoleLog extends AbstractLog {
    private static final long serialVersionUID = -6843151523380063975L;

    private static String logFormat = "[{dateTime}] [{level}] {name}: {msg}";
    private static Level level = Level.DEBUG;

    private String name;

    // ------------------------------------------------------------------------- Constructor
    public ConsoleLog(Class<?> clazz) {
        this.name = clazz.getName();
    }

    public ConsoleLog(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    // ------------------------------------------------------------------------- Log
    @Override
    public void log(Level level, String format, Object... arguments) {
        this.log(level, null, format, arguments);
    }

    @Override
    public void log(Level level, Throwable t, String format, Object... arguments) {
        if (false == isEnabled(level)) {
            return;
        }

        final Dict dict = Dict.create()
                .set("date", DateTimeCommand.now())
                .set("level", level.toString())
                .set("name", this.name)
                .set("msg", StringCommand.format(format, arguments));

        String logMsg = StringCommand.format(logFormat, dict);

        // WARN以上级别打印至System.err
        if (level.ordinal() >= Level.WARN.ordinal()) {
            Console.error(t, logMsg);
        } else {
            Console.log(t, logMsg);
        }

    }

    // ------------------------------------------------------------------------- Trace
    @Override
    public boolean isTraceEnabled() {
        return level.compareTo(Level.TRACE) <= 0;
    }

    @Override
    public void trace(String format, Object... arguments) {
        log(Level.TRACE, format, arguments);
    }

    @Override
    public void trace(Throwable t, String format, Object... arguments) {
        log(Level.TRACE, t, format, arguments);
    }

    // ------------------------------------------------------------------------- Debug
    @Override
    public boolean isDebugEnabled() {
        return level.compareTo(Level.DEBUG) <= 0;
    }

    @Override
    public void debug(String format, Object... arguments) {
        log(Level.DEBUG, format, arguments);
    }

    @Override
    public void debug(Throwable t, String format, Object... arguments) {
        log(Level.DEBUG, t, format, arguments);
    }

    // ------------------------------------------------------------------------- Info
    @Override
    public boolean isInfoEnabled() {
        return level.compareTo(Level.INFO) <= 0;
    }

    @Override
    public void info(String format, Object... arguments) {
        log(Level.INFO, format, arguments);
    }

    @Override
    public void info(Throwable t, String format, Object... arguments) {
        log(Level.INFO, t, format, arguments);
    }

    // ------------------------------------------------------------------------- Warn
    @Override
    public boolean isWarnEnabled() {
        return level.compareTo(Level.WARN) <= 0;
    }

    @Override
    public void warn(String format, Object... arguments) {
        log(Level.WARN, format, arguments);
    }

    @Override
    public void warn(Throwable t, String format, Object... arguments) {
        log(Level.WARN, t, format, arguments);
    }

    // ------------------------------------------------------------------------- Error
    @Override
    public boolean isErrorEnabled() {
        return level.compareTo(Level.ERROR) <= 0;
    }

    @Override
    public void error(String format, Object... arguments) {
        log(Level.ERROR, format, arguments);
    }

    @Override
    public void error(Throwable t, String format, Object... arguments) {
        log(Level.ERROR, t, format, arguments);
    }
}