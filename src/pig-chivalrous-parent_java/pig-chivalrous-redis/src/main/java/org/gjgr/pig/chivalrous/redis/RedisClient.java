package org.gjgr.pig.chivalrous.redis;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisPool;

/**
 * Created by zhangchuang on 16/2/17.
 */
public final class RedisClient {

    private static final Logger logger = LoggerFactory.getLogger(RedisClient.class);
    private static ConcurrentHashMap<String, Object> redisConnector = new ConcurrentHashMap<>();
    private String id;

    protected RedisClient(String id) {
        this.id = id;
    }

    public JedisPool jedisPool() {
        if (redisConnector.size() != 0) {
            Object object = redisConnector.get(id);
            if (object instanceof JedisCluster) {
                throw new UnsupportedOperationException("target is not Jedis Pool type. jedis cluster type");
            } else if (object instanceof JedisPool) {
                return (JedisPool) object;
            } else if (object instanceof JedisCommands) {
                throw new UnsupportedOperationException("target is not Jedis Pool type. try used jedis Commands");
            } else {
                throw new RuntimeException("not support the target type:" + object.getClass());
            }
        } else {
            throw new RuntimeException("has not found any redis command support");
        }
    }

    public JedisCluster jedisCluster() {
        if (redisConnector.size() != 0) {
            Object object = redisConnector.get(id);
            if (object instanceof JedisCluster) {
                return (JedisCluster) object;
            } else if (object instanceof JedisPool) {
                throw new UnsupportedOperationException("target is not Jedis Pool type. jedis pool type");
            } else if (object instanceof JedisCommands) {
                throw new UnsupportedOperationException("target is not Jedis Pool type. try used jedis Commands");
            } else {
                throw new RuntimeException("not support the target type:" + object.getClass());
            }
        } else {
            throw new RuntimeException("has not found any redis command support");
        }
    }

    public JedisCommands jedisCommands() {
        if (redisConnector.size() != 0) {
            Object object = redisConnector.get(id);
            if (object instanceof JedisCluster) {
                throw new UnsupportedOperationException("target is not Jedis Pool type. jedis cluster type");
            } else if (object instanceof JedisPool) {
                throw new UnsupportedOperationException("target is not Jedis Pool type. jedis pool type");
            } else if (object instanceof JedisCommands) {
                return (JedisCommands) object;
            } else {
                throw new RuntimeException("not support the target type:" + object.getClass());
            }
        } else {
            throw new RuntimeException("has not found any redis command support");
        }
    }

    protected Object redisConnector(String key) {
        return redisConnector.get(key);
    }

    protected Object redisConnector(Integer key) {
        return redisConnector.get(key + "");
    }

    public boolean close() {
        if (redisConnector.size() > 0 && redisConnector.containsKey(id)) {
            try {
                redisConnector.remove(id);
            } catch (Exception e) {
                logger.error("closed redis connector failed. {}", id);
            }
        }
        return false;
    }

    protected boolean put(RedisConfig redisConfig, Object object) {
        if (redisConnector.containsKey(redisConfig.hashString())) {
            return false;
        } else {
            // redisConnector.put(redisConfig, object);
        }
        return false;
    }
}
