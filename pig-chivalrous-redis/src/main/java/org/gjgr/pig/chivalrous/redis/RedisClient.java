package org.gjgr.pig.chivalrous.redis;

import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import redis.clients.jedis.commands.JedisCommands;

/**
 * Created by zhangchuang on 16/2/17.
 */
public final class RedisClient implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(RedisClient.class);
    private static ConcurrentHashMap<String, Object> redisConnector = new ConcurrentHashMap<>();
    private String id;

    private static ThreadLocal<JedisCommands> jedisCommandsThreadLocal = new ThreadLocal<>();

    protected RedisClient(String id) {
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public static synchronized Object get(String id) {
        return redisConnector.get(id);
    }

    protected static synchronized boolean build(RedisConfig redisConfig, Object object) {
        if (redisConnector.containsKey(redisConfig.hashString())) {
            return false;
        } else {
            redisConnector.put(redisConfig.hashString(), object);
            return true;
        }
    }

    public static synchronized boolean reBuild(RedisConfig redisConfig, Object object) throws IOException {
        if (redisConnector.containsKey(redisConfig.hashString())) {
            Object oo = redisConnector.get(redisConfig.hashString());
            if (oo instanceof JedisCluster) {
                JedisCluster jedisCluster = (JedisCluster) oo;
                jedisCluster.close();
            } else if (oo instanceof JedisPool) {
                JedisPool jedisPool = (JedisPool) oo;
                jedisPool.close();
            } else {
                throw new RuntimeException("not found the target release method for the object founded. {}" + object.getClass().getName());
            }
            redisConnector.put(redisConfig.hashString(), object);
        } else {
            redisConnector.put(redisConfig.hashString(), object);
        }
        return true;
    }

    public JedisPool jedisPool() {
        if (redisConnector.size() != 0) {
            Object object = redisConnector.get(id);
            if (object instanceof JedisPool) {
                return (JedisPool) object;
            } else if (object instanceof JedisCluster) {
                throw new UnsupportedOperationException("target is not Jedis cluster type. jedis cluster type in "+id);
            } else if (object instanceof JedisCommands) {
                throw new UnsupportedOperationException("target is not Jedis Pool type and not Jedis cluster type. try used jedis Commands in "+id);
            } else {
                throw new RuntimeException("not support the target type:" + object.getClass()+" at id "+id);
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
                throw new UnsupportedOperationException("target is not Jedis Pool type. is Jedis cluster type in "+id);
            } else if (object instanceof JedisCommands) {
                throw new UnsupportedOperationException("target is not Jedis Pool type and not Jedis cluster. try used jedis Commands in "+id);
            } else {
                throw new RuntimeException("not support the target type:" + object.getClass()+" at id "+id);
            }
        } else {
            throw new RuntimeException("has not found any redis command support");
        }
    }

    public Jedis jedis() {
        if (redisConnector.size() != 0) {
            Object object = redisConnector.get(id);
            if (object instanceof JedisPool) {
                JedisPool jedisPool = (JedisPool) object;
                logger.debug("take a jedis from the object pool and need human to closed the connect.");
                return jedisPool.getResource();
            } else {
                throw new RuntimeException("not support the target type:" + object.getClass());
            }
        } else {
            throw new RuntimeException("has not found any redis command support about " + JedisCommands.class.getName());
        }
    }

    public JedisCommands redisCommands() {
        if (redisConnector.size() != 0) {
            Object object = redisConnector.get(id);
            if (object instanceof JedisCommands) {
                return (JedisCommands) object;
            } else if (object instanceof JedisPool) {
                JedisPool jedisPool = (JedisPool) object;
                logger.debug("take a jedis command and need human to release. "+id);
                return jedisPool.getResource();
            } else {
                throw new RuntimeException("not support the target type:" + object.getClass()+" at id "+id);
            }
        } else {
            throw new RuntimeException("has not found any redis command support about " + JedisCommands.class.getName());
        }
    }

    public JedisCommands jedisCommands() {
        if (redisConnector.size() != 0) {
            Object object = redisConnector.get(id);
            if (object instanceof JedisCommands) {
                return (JedisCommands) object;
            } else if (object instanceof JedisPool) {
                JedisPool jedisPool = (JedisPool) object;
                logger.debug("take a jedis command and would auto release.");
                return new JedisClient(jedisPool);
            } else {
                throw new RuntimeException("not support the target type:" + object.getClass());
            }
        } else {
            throw new RuntimeException("has not found any redis command support about " + JedisCommands.class.getName());
        }
    }

    public JedisCommands getJedisCommands(){
        if(jedisCommandsThreadLocal.get()==null){
            jedisCommandsThreadLocal.set(jedisCommands());
        }
        return jedisCommandsThreadLocal.get();
    }

    protected Object redisConnector(String key) {
        return redisConnector.get(key);
    }

    protected Object redisConnector(Integer key) {
        return redisConnector.get(key + "");
    }

    public boolean release() {
        if (redisConnector.size() > 0 && redisConnector.containsKey(id)) {
            try {
                Object o = redisConnector(id);
                if (o instanceof JedisPool) {
                    JedisPool jedisPool = (JedisPool) o;
                    jedisPool.close();
                } else if (o instanceof JedisCluster) {
                    JedisCluster jedisCluster = (JedisCluster) o;
                    jedisCluster.close();
                }
                redisConnector.remove(id);
            } catch (Exception e) {
                logger.error("closed redis connector failed. {}", id);
            }
        }
        return false;
    }

    public boolean free(Object object) {
        if (object != null) {
            if (object instanceof Jedis) {
                Jedis j = (Jedis) object;
                j.flushAll();
                j.close();
            } else if (object instanceof JedisCluster) {
                JedisCluster jedisCluster = (JedisCluster) object;
                jedisCluster.close();
            } else if (object instanceof JedisPool) {
                JedisPool jedisPool = (JedisPool) object;
                jedisPool.close();
            } else {
                logger.warn("unknown the type object {} could not free the resouce.", object);
                return false;
            }
        } else {
            logger.debug("no need to free the object the object is is null");
            return false;
        }
        return false;
    }
}
