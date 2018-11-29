package org.gjgr.pig.chivalrous.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonitorJedisPoolMap extends Thread {

    public static final Logger logger = LoggerFactory.getLogger(MonitorJedisPoolMap.class);
    public RedisClientProxy redisPoolProxy = null;

    // 线程关闭状态, false-运行,true-关闭
    private volatile boolean shutdown = false;

    public MonitorJedisPoolMap(RedisClientProxy poolProxy) {
        redisPoolProxy = poolProxy;
    }

    @Override
    public void run() {
        while (!shutdown) {
            try {
                Thread.sleep(redisPoolProxy.getPoolexpiretime());
            } catch (InterruptedException ie) {
                logger.info("the monitor thread is interrupted");
                // 恢复中断状态
                Thread.currentThread().interrupt();

            } catch (Exception e) {
                logger.error("monitorjedispoolmap refreshRedisPool error:", e);
                e.printStackTrace();
            }

        }
    }

    public void shutdown() {
        shutdown = true;
    }
}
