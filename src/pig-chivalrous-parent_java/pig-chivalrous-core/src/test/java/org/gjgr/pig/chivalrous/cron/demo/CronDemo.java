package org.gjgr.pig.chivalrous.cron.demo;

import org.gjgr.pig.chivalrous.core.cron.CronUtil;
import org.gjgr.pig.chivalrous.core.log.LogFactory;
import org.gjgr.pig.chivalrous.core.log.dialect.console.ConsoleLogFactory;

/**
 * 定时任务样例
 */
public class CronDemo {
    public static void main(String[] args) {
        LogFactory.setCurrentLogFactory(ConsoleLogFactory.class);

        // CronUtil.schedule("*/2 * * * * *", new Task(){
        //
        // @Override
        // public void execute() {
        // Console.log("Task excuted.");
        // }
        // });

        // 支持秒级别定时任务
        CronUtil.setMatchSecond(true);

        CronUtil.start();
        // CronUtil.stop();
    }
}
