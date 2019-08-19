package org.gjgr.pig.chivalrous.log;

import org.gjgr.pig.chivalrous.log.appender.MessageLogAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * @Author gwd
 * @Time 01-30-2019 Wednesday
 * @Description: developer.tools:
 * @Target:
 * @More:
 */
public class Log4jAppender {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(MessageLogAppender.class);
        logger.info("test");
        logger.info("", new HashMap<>());
//        try {
//            Thread.sleep(1000 * 30);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }
}
