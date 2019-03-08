package org.gjgr.pig.chivalrous.redis;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisPool;

/**
 * Created by zhangchuang on 16/2/17.
 */
public final class RedisClient {

    private static final Logger logger = LoggerFactory.getLogger(RedisClient.class);
    private static ConcurrentHashMap<String, Object> redisConnector = new ConcurrentHashMap<>();
    private static ThreadLocal<RedisConfig> redisConfigThreadLocal = new ThreadLocal<>();
    private static RedisConfig redisConfig = null;
    private String id;

    protected RedisClient(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JedisPool jedisPool() {
        JedisPool jedisPool = null;
        if (redisConfig != null) {
            RedisConfig temp = redisConfigThreadLocal.get();
            if (temp != null) {
                int id = temp.hashCode();
                if (id == temp.hashCode()) {
                    Object object = redisConnector(id);
                    if (object instanceof JedisPool) {
                        return (JedisPool) object;
                    } else {
                        if (redisConfig.status() <= 0) {
                            // RedisCommand.jedisPool()
                        } else {
                            return null;
                        }
                    }
                } else {

                }
            }
        } else {

        }
        return jedisPool;
    }

    protected Object redisConnector(String key) {
        return redisConnector.get(key);
    }

    protected Object redisConnector(Integer key) {
        return redisConnector.get(key + "");
    }

    public boolean close() {

        return false;
    }

    protected boolean put(RedisConfig redisConfig, Object object) {
        if (redisConnector.containsKey(redisConfig.hasString())) {
            return false;
        } else {
            // redisConnector.put(redisConfig, object);
        }
        return false;
    }

    protected boolean remove(RedisConfig redisConfig) {

        return false;
    }

}
