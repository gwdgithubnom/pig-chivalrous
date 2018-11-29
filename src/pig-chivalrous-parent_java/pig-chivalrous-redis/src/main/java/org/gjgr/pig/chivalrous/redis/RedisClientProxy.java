package org.gjgr.pig.chivalrous.redis;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class RedisClientProxy {
    private static final int DEFAULT_TIMEOUT = 2000;
    private static Logger logger = LoggerFactory.getLogger(RedisClientProxy.class);
    public RedisPoolProxyConfig proxyConfig;
    public ConcurrentHashMap<Integer, JedisPool> jedisPoolMapCache = new ConcurrentHashMap<Integer, JedisPool>();
    private JedisPool jedisPool = null;
    private JedisCluster jedisCluster = null;
    private long poolexpiretime = 60000;
    private ThreadLocal<Jedis> jedisThreadLocal = new ThreadLocal<>();
    private MonitorJedisPoolMap monitor = null;

    public RedisClientProxy(RedisPoolProxyConfig poolConfig) {
        if (poolConfig == null) {
            throw new NullPointerException("pool config is null, should be not null");
        }
        // 初始化redis连接池配置
        proxyConfig = poolConfig;

        // redis连接池更新频率 默认值 60000 毫秒
        if (poolConfig.getRedisPoolRefreshInterval() != 0) {
            poolexpiretime = poolConfig.getRedisPoolRefreshInterval();
        }
        // 更新ip列表
        if (StringUtils.isNotEmpty(proxyConfig.getProxyCluster())) {
            // 启动监测线程
            // monitor = new MonitorJedisPoolMap(this);
            // todo monitor.start();
            jedisCluster = generateJedisCluster();
        } else if (StringUtils.isNotEmpty(proxyConfig.getProxyHost())) {
            this.jedisPool = generateJedisPool(proxyConfig.getProxyHost(), proxyConfig.getProxyPort(),
                    proxyConfig.getPassword());
        } else {
            throw new UnsupportedOperationException("could not find the handler to do next work.");
        }

    }

    /**
     * 生成redis连接池
     *
     * @return
     */
    public final synchronized JedisPool generateJedisPool(String ip, int port, String password) {
        // temporary 不支持ping,需设置为false
        proxyConfig.setTestOnBorrow(false);
        proxyConfig.setTestWhileIdle(false);
        proxyConfig.setTestOnCreate(false);
        proxyConfig.setTestOnReturn(false);
        int timeout = proxyConfig.getPoolTimeOut() != 0 ? proxyConfig.getPoolTimeOut() : DEFAULT_TIMEOUT;
        JedisPool jedisPool = new JedisPool(proxyConfig, ip, port, timeout, password);
        return jedisPool;
    }

    /**
     * 生成redis连接池
     *
     * @return
     */
    public final synchronized JedisPool generateJedisPool(String ip, int port) {
        // temporary 不支持ping,需设置为false
        return generateJedisPool(ip, port, null);
    }

    /**
     * 生成redis连接池
     *
     * @return
     */
    public final synchronized JedisCluster generateJedisCluster() {
        Set<HostAndPort> nodeSet = new HashSet<>();
        for (String host2port : proxyConfig.getProxyCluster().split(",")) {
            String[] hostport = host2port.split(":");
            HostAndPort hostAndPort = new HostAndPort(hostport[0], Integer.valueOf(hostport[1]));
            nodeSet.add(hostAndPort);
        }
        JedisCluster jedisCluster = new JedisCluster(nodeSet);
        return jedisCluster;
    }

    /**
     * @return
     */
    public JedisPool getJedisPool() {
        return this.jedisPool;
    }

    /**
     * 获取jedis资源
     *
     * @param jedisPool
     * @return
     */
    public Jedis getResource(JedisPool jedisPool) {
        try {
            Jedis jedis = jedisThreadLocal.get();
            if (jedis == null) {
                jedis = jedisPool.getResource();
                jedisThreadLocal.set(jedis);
            }
            return jedis;
        } catch (Exception e) {
            logger.error("get resource error: ", e);
        }
        throw new RuntimeException("can not get redisClient resource");
    }

    /**
     * 获取jedis cluster资源
     *
     * @return
     */
    public JedisCluster getCluster() {
        try {
            return jedisCluster;
        } catch (Exception e) {
            logger.error("get resource error: ", e);
        }
        throw new RuntimeException("can not get redisClient resource");
    }

    public long getPoolexpiretime() {
        return poolexpiretime;
    }

    public void setPoolexpiretime(long poolexpiretime) {
        this.poolexpiretime = poolexpiretime;
    }

    /**
     * 检测Jedis连接池可用性
     *
     * @param jedisPool
     * @return
     */
    @SuppressWarnings("deprecation")
    private boolean checkJedisPool(JedisPool jedisPool) {
        if (jedisPool == null) {
            return false;
        }
        Jedis jedis = null;
        int failCount = 0;

        for (int i = 0; i < proxyConfig.getFailCount(); i++) {
            try {
                jedis = jedisPool.getResource();
                String setResult = jedis.set(proxyConfig.getTestKey(), "1");
                logger.info("checkJedisPool set key:" + proxyConfig.getTestKey() + ", result : " + setResult);
                jedisPool.returnResource(jedis);
                if (!"OK".equalsIgnoreCase(setResult)) {
                    failCount++;
                } else {
                    break;
                }
            } catch (Exception e) {
                failCount++;
                logger.error(e.getMessage(), e);
                jedisPool.returnBrokenResource(jedis);
            }
        }
        if (failCount == proxyConfig.getFailCount()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 检测缓存中的Redis连接池是否可用
     * <p>
     * 通过checkJedisPool检测,不可用则从缓存中移除,可用则保留
     * </p>
     *
     * @param jedisPoolMapCache
     */
    private void checkCachePool(ConcurrentHashMap<Integer, JedisPool> jedisPoolMapCache) {
        logger.info("check cache pool start");
        if (jedisPoolMapCache == null || jedisPoolMapCache.size() == 0) {
            return;
        }
        for (Map.Entry<Integer, JedisPool> entry : jedisPoolMapCache.entrySet()) {
            JedisPool jedisPool = entry.getValue();
            if (!checkJedisPool(jedisPool)) {
                try {
                    logger.info("remove not work pool,cache key : " + entry.getKey() + ", pool : " + entry.getValue());
                    jedisPool.destroy();
                    jedisPoolMapCache.remove(entry.getKey());
                } catch (Exception e) {
                    logger.error("redisClient pool destroy fail", e);
                }
            }
        }
        logger.info("check cache pool end");

    }

    /**
     * 关闭redis client
     */
    public void shutdown() {
        shutdownMonitor();
        destroyJedisPool();
    }

    /**
     * 关闭监控线程
     */
    private void shutdownMonitor() {
        logger.info("shutdown the monitor thread");
        if (monitor != null) {
            try {
                // 设置关闭状态
                monitor.shutdown();
                // 中断监控线程
                monitor.interrupt();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);

            }
        }

    }

    /**
     * 销毁jedis连接池
     */
    private void destroyJedisPool() {
        logger.info("destroy the jedis pool cache.");
        try {
            if (jedisPoolMapCache != null && jedisPoolMapCache.size() > 0) {
                for (JedisPool jedisPool : jedisPoolMapCache.values()) {
                    if (jedisPool != null && !jedisPool.isClosed()) {
                        jedisPool.destroy();
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

}
