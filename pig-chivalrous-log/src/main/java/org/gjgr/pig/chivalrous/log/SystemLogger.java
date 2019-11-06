package org.gjgr.pig.chivalrous.log;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

public class SystemLogger {

    private static Logger logger = LoggerFactory.getLogger(SystemLogger.class);

    public static boolean info(String string, Object... objects) {
        logger.info(string, objects);
        return true;
    }

    public static boolean debug(String string, Object... objects) {
        logger.debug(string, objects);
        return true;
    }

    public static boolean error(String string, Object... objects) {
        logger.error(string, objects);
        return true;
    }

    public static boolean warn(String string, Object... objects) {
        logger.warn(string, objects);
        return true;
    }

    public static boolean trace(String string, Object... objects) {
        logger.trace(string, objects);
        return true;
    }

    public static String trace(Logger logger, String string, Object... objects) {
        String s = MessageFormatter.format(string, objects).getMessage();
        logger.trace(string, objects);
        return s;
    }

    public static String info(Logger logger, String string, Object... objects) {
        String s = MessageFormatter.format(string, objects).getMessage();
        logger.info(string, objects);
        return s;
    }

    public static String debug(Logger logger, String string, Object... objects) {
        String s = MessageFormatter.format(string, objects).getMessage();
        logger.debug(string, objects);
        return s;
    }

    public static String warn(Logger logger, String string, Object... objects) {
        String s = MessageFormatter.format(string, objects).getMessage();
        logger.warn(string, objects);
        return s;
    }

    public static String error(Logger logger, String string, Object... objects) {
        String s = MessageFormatter.format(string, objects).getMessage();
        logger.error(string, objects);
        return s;
    }

    public static String trace(Exception exception) {
        String s = "Exception: " + Arrays.toString(exception.getStackTrace()) + " cause by " + exception.getMessage();
        logger.trace("Exception {} {}", exception.getStackTrace(), exception.getMessage());
        return s;
    }

    public static String debug(Exception exception) {
        String s = "Exception: " + Arrays.toString(exception.getStackTrace()) + " cause by " + exception.getMessage();
        logger.debug("Exception {} {}", exception.getStackTrace(), exception.getMessage());
        return s;
    }

    public static String info(Exception exception) {
        String s = "Exception: " + Arrays.toString(exception.getStackTrace()) + " cause by " + exception.getMessage();
        logger.info("Exception {} {}", exception.getStackTrace(), exception.getMessage());
        return s;
    }

    public static String warn(Exception exception) {
        String s = "Exception: " + Arrays.toString(exception.getStackTrace()) + " cause by " + exception.getMessage();
        logger.warn("Exception {} {}", exception.getStackTrace(), exception.getMessage());
        return s;
    }

    public static String error(Exception exception) {
        String s = "Exception: " + Arrays.toString(exception.getStackTrace()) + " cause by " + exception.getMessage();
        logger.error("Exception {} {}", exception.getStackTrace(), exception.getMessage());
        return s;
    }

    public static String trace(Exception exception, String string, Object... objects) {
        String s = MessageFormatter.format(string, objects).getMessage() + " " + Arrays.toString(exception.getStackTrace()) + " cause by " + exception.getMessage();
        logger.trace(string + "{} - {} - {}", objects, exception.getStackTrace(), exception.getMessage());
        return s;
    }

    public static String debug(Exception exception, String string, Object... objects) {
        String s = MessageFormatter.format(string, objects).getMessage() + " " + Arrays.toString(exception.getStackTrace()) + " cause by " + exception.getMessage();
        logger.debug(string + "{} - {} - {}", objects, exception.getStackTrace(), exception.getMessage());
        return s;
    }

    public static String info(Exception exception, String string, Object... objects) {
        String s = MessageFormatter.format(string, objects).getMessage() + " " + Arrays.toString(exception.getStackTrace()) + " cause by " + exception.getMessage();
        logger.info(string + "{} - {} - {}", objects, exception.getStackTrace(), exception.getMessage());
        return s;
    }

    public static String warn(Exception exception, String string, Object... objects) {
        String s = MessageFormatter.format(string, objects).getMessage() + " " + Arrays.toString(exception.getStackTrace()) + " cause by " + exception.getMessage();
        logger.warn(string + "{} - {} - {}", objects, exception.getStackTrace(), exception.getMessage());
        return s;
    }

    public static String error(Exception exception, String string, Object... objects) {
        String s = MessageFormatter.format(string, objects).getMessage() + " " + Arrays.toString(exception.getStackTrace()) + " cause by " + exception.getMessage();
        logger.error(string + "{} - {} - {}", objects, exception.getStackTrace(), exception.getMessage());
        return s;
    }

}
