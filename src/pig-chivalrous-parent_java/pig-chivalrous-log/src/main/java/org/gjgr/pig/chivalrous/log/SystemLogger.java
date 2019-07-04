package org.gjgr.pig.chivalrous.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author gwd
 * @Time 01-30-2019 Wednesday
 * @Description: global-news-feed:
 * @Target:
 * @More:
 */
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
        logger.info(string, objects);
        return true;
    }

}
