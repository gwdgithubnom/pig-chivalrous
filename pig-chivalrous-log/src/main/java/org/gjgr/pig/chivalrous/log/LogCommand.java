package org.gjgr.pig.chivalrous.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

public final class LogCommand {

    public static int ignoreReflectionLog(Class clazz,Level level) {
        Level switchLevel;
        switch (level) {
            case WARN:
                switchLevel = Level.ERROR;
                break;
            case INFO:
                switchLevel = Level.WARN;
                break;
            case DEBUG:
                switchLevel = Level.INFO;
                break;
            case TRACE:
                switchLevel = Level.DEBUG;
                break;
            default:
                switchLevel = Level.ERROR;
                break;
        }
        return setLoggerLevel(LoggerFactory.getLogger(clazz),switchLevel);
    }

    public static int setLog4j2LoggerLevel(Logger targetLogger, Level level) {
        org.apache.logging.log4j.Logger apacheLog4j = org.apache.logging.log4j.LogManager.getLogger(targetLogger.getName());
        switch (level) {
            case WARN:
                org.apache.logging.log4j.core.config.Configurator.setLevel(apacheLog4j.getName(), org.apache.logging.log4j.Level.WARN);
                break;
            case INFO:
                org.apache.logging.log4j.core.config.Configurator.setLevel(apacheLog4j.getName(), org.apache.logging.log4j.Level.INFO);
                break;
            case DEBUG:
                org.apache.logging.log4j.core.config.Configurator.setLevel(apacheLog4j.getName(), org.apache.logging.log4j.Level.DEBUG);
                break;
            case TRACE:
                org.apache.logging.log4j.core.config.Configurator.setLevel(apacheLog4j.getName(), org.apache.logging.log4j.Level.TRACE);
                break;
            default:
                org.apache.logging.log4j.core.config.Configurator.setLevel(apacheLog4j.getName(), org.apache.logging.log4j.Level.FATAL);
                break;
        }
        SystemLogger.info("change log to level {}, with object {}, status: {}", apacheLog4j, targetLogger, targetLogger.isWarnEnabled());
        return 1;
    }

    public static int setLog4jLoggerLevel(Logger targetLogger,Level level){
        org.apache.log4j.Logger apacheLog4j1 = org.apache.log4j.LogManager.getLogger(targetLogger.getName());
        switch (level) {
            case WARN:
                apacheLog4j1.setLevel(org.apache.log4j.Level.WARN);
                break;
            case INFO:
                apacheLog4j1.setLevel(org.apache.log4j.Level.INFO);
                break;
            case DEBUG:
                apacheLog4j1.setLevel(org.apache.log4j.Level.DEBUG);
                break;
            case TRACE:
                apacheLog4j1.setLevel(org.apache.log4j.Level.TRACE);
                break;
            default:
                apacheLog4j1.setLevel(org.apache.log4j.Level.FATAL);
                break;
        }
        SystemLogger.info("change log to level {}, with object {}, status: {}", apacheLog4j1, targetLogger, targetLogger.isWarnEnabled());
        return 1;
    }

    public static int setLoggerLevel(Logger targetLogger, Level level){
        if (targetLogger != null) {
            SystemLogger.info("found the reflection work log {}", targetLogger.getClass());
            try {
                // try found the log class impl
                Class.forName("org.apache.logging.log4j.Logger");
                if (targetLogger instanceof org.apache.logging.log4j.Logger) {
                    setLog4j2LoggerLevel(targetLogger,level);
                    return 1;
                }else{
                    try {
                        // try found the log class impl
                        Class.forName("org.apache.logging.slf4j.Log4jLogger");
                        if (targetLogger instanceof org.apache.logging.slf4j.Log4jLogger) {
                            setLog4j2LoggerLevel(targetLogger,level);
                            return 1;
                        }else{
                            try {
                                Class.forName("org.apache.log4j.Logger");
                                if (targetLogger instanceof org.apache.log4j.Logger) {
                                    setLog4jLoggerLevel(targetLogger,level);
                                    return 1;
                                }
                            } catch (Exception e) {
                            }
                        }
                    } catch (Exception e) {
                    }

                    setLog4j2LoggerLevel(targetLogger,level);
                    setLog4jLoggerLevel(targetLogger,level);
                }

            } catch (Exception e) {
            }
            return -1;
        }else{
            return -1;
        }
    }
}
