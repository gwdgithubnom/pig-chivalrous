package org.gjgr.pig.chivalrous.log;

import org.gjgr.pig.chivalrous.core.lang.Console;
import org.gjgr.pig.chivalrous.core.log.Log;
import org.gjgr.pig.chivalrous.core.log.LogFactory;
import org.gjgr.pig.chivalrous.core.log.dialect.commons.SimpleLogFactory;
import org.gjgr.pig.chivalrous.core.log.dialect.console.ConsoleLogFactory;
import org.gjgr.pig.chivalrous.core.log.dialect.jdk.JdkLogFactory;
import org.gjgr.pig.chivalrous.core.log.level.Level;
import org.junit.Test;

/**
 * 日志门面单元测试
 *
 * @author Looly
 */
public class LogTest {
    @Test
    public void logTest() {
        Log log = LogFactory.get();
        // 自动选择日志实现
        log.debug("This is {} log", Level.DEBUG);
        log.info("This is {} log", Level.INFO);
        log.warn("This is {} log", Level.WARN);

        // Exception e = new Exception("test Exception");
        // log.error(e, "This is {} log", Level.ERROR);
    }

    @Test
    public void customLogTest() {
        // 自动选择日志实现
        Log log = LogFactory.get();
        log.debug("This is {} log", "default");
        Console.log("----------------------------------------------------------------------");

        // 自定义日志实现为Apache Commons Logging
        LogFactory.setCurrentLogFactory(new SimpleLogFactory());
        // 自动选择日志实现
        log.debug("This is {} log", "custom apache commons logging");
        Console.log("----------------------------------------------------------------------");

        /*
         * //自定义日志实现为TinyLog LogFactory.setCurrentLogFactory(new TinyLogFactory()); // 自动选择日志实现
         * log.debug("This is {} log", "custom tinylog");
         * Console.log("----------------------------------------------------------------------");
         */
        // 自定义日志实现为JDK Logging
        LogFactory.setCurrentLogFactory(new JdkLogFactory());
        // 自动选择日志实现
        log.info("This is {} log", "custom jdk logging");
        Console.log("----------------------------------------------------------------------");

        // 自定义日志实现为Console Logging
        LogFactory.setCurrentLogFactory(new ConsoleLogFactory());
        // 自动选择日志实现
        log.info("This is {} log", "custom Console");
        Console.log("----------------------------------------------------------------------");
    }
}
