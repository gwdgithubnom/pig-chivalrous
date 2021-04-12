package org.gjgr.pig.chivalrous.redis;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.gjgr.pig.chivalrous.core.io.file.yml.YmlNode;
import org.gjgr.pig.chivalrous.core.lang.NonNull;
import org.gjgr.pig.chivalrous.core.net.UriBuilder;
import org.gjgr.pig.chivalrous.core.net.UriCommand;
import org.gjgr.pig.chivalrous.core.util.RandomCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.commands.JedisCommands;

/**
 * @Author gwd
 * @Time 11-27-2018 Tuesday
 * @Description: org.gjgr.pig.chivalrous.core:
 * @Target:
 * @More:
 */
public class RedisCommand {

    private static Logger logger = LoggerFactory.getLogger(RedisCommand.class);

    public static RedisClient redisClient(RedisConfig redisConfig) {
        return redisConfig.build();
    }

    public static RedisClient redisClient(YmlNode ymlNode) {
        RedisConfig redisConfig = redisConfig(ymlNode);
        if (redisConfig != null) {
            return redisClient(redisConfig);
        } else {
            return null;
        }
    }

    public static boolean redisUnlock(RedisClient redisClient, String lockKey, String key) {
        long l = redisClient.getJedisCommands().del(lockKey);
        logger.debug("set redis lock, do unlock operation {} about {} in {}", l, key, lockKey);
        return true;
    }

    public static boolean redisUnlock(JedisCommands jedisCommands, String lockKey, String key) {
        long l = jedisCommands.del(lockKey);
        logger.debug("set redis lock, do unlock operation {} about {} in {}", l, key, lockKey);
        return true;
    }

    public static boolean redisLock(RedisClient redisClient, String lockKey, String key, Long expireSeconds) {
        boolean status = false;
        try {
            int i = 0;
            while (i < 3) {
                long l = redisClient.getJedisCommands().setnx(lockKey, System.currentTimeMillis() + "");
                redisClient.getJedisCommands().expire(lockKey, expireSeconds.intValue());
                if (l > 0) {
                    status = true;
                    break;
                } else {
                    Thread.sleep(RandomCommand.randomInt(6000));
                    i++;
                }
                if (i == 3) {
                    try {
                        Number number = NumberFormat.getInstance().parse(redisClient.getJedisCommands().get(lockKey));
                        if (System.currentTimeMillis() - number.longValue() > 3600000) {
                            redisClient.getJedisCommands().del(lockKey);
                            l = redisClient.getJedisCommands().setnx(lockKey, System.currentTimeMillis() + "");
                            redisClient.getJedisCommands().expire(lockKey, expireSeconds.intValue());
                            if (l > 0) {
                                status = true;
                            } else {
                                logger.debug("get locked item failed {}", key);
                            }
                        } else {
                            logger.debug("current key is been locked {} about {}", lockKey, number.longValue());
                        }
                        redisClient.getJedisCommands().del(lockKey);
                    } catch (Exception e) {
                        logger.error("redis lock failed, parse lock failed {}", lockKey);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("redis lock item failed for item {}", Arrays.toString(e.getStackTrace()) + " " + e.getMessage());
        }
        logger.debug("get redis lock, do lock operation {} about {} in {}", status, key, lockKey);
        return status;
    }

    public static boolean redisLock(JedisCommands jedisCommands, String lockKey, String key, Long expireSeconds) {
        boolean status = false;
        try {
            int i = 0;
            while (i < 3) {
                long l = jedisCommands.setnx(lockKey, System.currentTimeMillis() + "");
                jedisCommands.expire(lockKey, expireSeconds.intValue());
                if (l > 0) {
                    status = true;
                    break;
                } else {
                    Thread.sleep(RandomCommand.randomInt(6000));
                    i++;
                }
                if (i == 3) {
                    try {
                        Number number = NumberFormat.getInstance().parse(jedisCommands.get(lockKey));
                        if (System.currentTimeMillis() - number.longValue() > 3600000) {
                            jedisCommands.del(lockKey);
                            l = jedisCommands.setnx(lockKey, System.currentTimeMillis() + "");
                            jedisCommands.expire(lockKey, expireSeconds.intValue());
                            if (l > 0) {
                                status = true;
                            } else {
                                logger.debug("get locked item failed {}", key);
                            }
                        } else {
                            logger.debug("current key is been locked {} about {}", lockKey, number.longValue());
                        }
                        jedisCommands.del(lockKey);
                    } catch (Exception e) {
                        logger.error("redis lock failed, parse lock failed {}", lockKey);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("redis lock item failed for item {}", Arrays.toString(e.getStackTrace()) + " " + e.getMessage());
        }
        logger.debug("get redis lock, do lock operation {} about {} in {}", status, key, lockKey);
        return status;
    }

    public static RedisConfig redisConfig(YmlNode ymlNode) {
        if (ymlNode == null) {
            return null;
        } else {
            List list = ymlNode.list();
            RedisConfig redisConfig = new RedisConfig();
            list.forEach(l -> {
                String s = l.toString();
                UriBuilder uriBuilder = UriCommand.uriBuilder(s);
                redisConfig.add(uriBuilder.getHost(), uriBuilder.getPort());
                if (uriBuilder.getQueryParams().size() > 0) {
                    Map params = uriBuilder.getParams();
                    if (params.containsKey("password")) {
                        redisConfig.setPassword(params.get("password").toString());
                    }
                    if (params.containsKey("type")) {
                        try {
                            int i = Integer.parseInt(params.get("type").toString());
                            if (i > 0) {
                                redisConfig.setCluster();
                            } else {
                                redisConfig.setNoCluster();
                            }
                        } catch (Exception e) {
                            redisConfig.setCluster();
                        }
                    }
                    if (params.containsKey("connectionTimeout")) {
                        redisConfig.setConnectionTimeout(Integer.parseInt(params.get("connectionTimeout").toString()));
                    }
                    if (params.containsKey("maxAttempts")) {
                        redisConfig.setMaxAttempts(Integer.parseInt(params.get("maxAttempts").toString()));
                    }
                    if (params.containsKey("timeout")) {
                        redisConfig.setTimeout(Integer.parseInt(params.get("timeout").toString()));
                    }
                }
            });
            return redisConfig;
        }
    }

    public static RedisConfig redisConfig(@NonNull String[] urls, String[] ports, boolean type) {
        return redisConfig(urls, ports, null, type);
    }

    public static RedisConfig redisConfig(@NonNull String[] urls, String[] ports, String password, boolean type) {
        RedisConfig redisConfig = new RedisConfig();
        if (type) {
            redisConfig.setCluster();
            redisConfig.add(urls, ports);
        } else {
            redisConfig.setNoCluster();
            redisConfig.put(urls[0], ports[0]);
        }
        if (password != null) {
            redisConfig.setPassword(password);
        }
        return redisConfig;
    }

    public static RedisConfig redisConfig(@NonNull String url, int port, String password, boolean type) {
        String[] urls = new String[1];
        urls[0] = url;
        String[] ports = new String[1];
        ports[0] = port + "";
        return redisConfig(urls, ports, password, false);
    }

    public static RedisClient redis(@NonNull String[] urls, String[] ports, String password, boolean type) {
        RedisConfig redisConfig = redisConfig(urls, ports, password, type);
        return redisClient(redisConfig);
    }

    public static RedisConfig redisConfig(@NonNull String url, int port) {
        return redisConfig(url, port, null, false);
    }

    public static RedisClient redis(@NonNull String url, int port, String password, boolean type) {
        String[] urls = new String[1];
        urls[0] = url;
        String[] ports = new String[1];
        ports[0] = port + "";
        return redis(urls, ports, password, false);
    }

    public static RedisClient redis(@NonNull String url, int port) {
        return redis(url, port, null, false);
    }

    public static RedisConfig redisConfig(@NonNull String uri, String password, boolean type) {
        String[] uris = uri.split(";");
        String[] urls = new String[uris.length];
        String[] ports = new String[uris.length];
        for (int i = 0; i < uris.length; i++) {
            String[] u = uris[i].split(":");
            if (u.length == 1) {
                throw new UnsupportedOperationException("could not parse the uri, format style: ip1:port1" + uri);
            } else {
                urls[i] = u[0];
                ports[i] = u[1];
            }
        }
        return redisConfig(urls, ports, password, type);
    }

    public static RedisClient redis(@NonNull String uri, String password, boolean type) {
        String[] uris = uri.split(";");
        String[] urls = new String[uris.length];
        String[] ports = new String[uris.length];
        for (int i = 0; i < uris.length; i++) {
            String[] u = uris[i].split(":");
            if (u.length == 1) {
                throw new UnsupportedOperationException("could not parse the uri, format style: ip1:port1" + uri);
            } else {
                urls[i] = u[0];
                ports[i] = u[1];
            }
        }
        return redis(urls, ports, password, type);
    }

    public static RedisConfig redisConfig(@NonNull String uri, boolean type) {
        return redisConfig(uri, null, type);
    }

    /**
     * ip:port
     *
     * @param uri
     * @param type
     * @return
     */
    public static RedisClient redis(@NonNull String uri, boolean type) {
        return redis(uri, null, type);
    }

    public static RedisClient redis(@NonNull String uri) {
        if (uri.contains(";")) {
            return redis(uri, null, true);
        } else {
            return redis(uri, null, false);
        }
    }

    public static RedisConfig redisConfig(@NonNull String uri) {
        if (uri.contains(";")) {
            return redisConfig(uri, null, true);
        } else {
            return redisConfig(uri, null, false);
        }
    }

    public static JedisPool jedisPool(RedisConfig redisConfig) {
        HostAndPort hostAndPort = redisConfig.getBase().iterator().next();
        JedisPool jedisPool =
            new JedisPool(redisConfig.buildGenericObjectPoolConfig(), hostAndPort.getHost(), hostAndPort.getPort());
        return jedisPool;
    }

    public static JedisCluster jedisCluster(RedisConfig redisConfig) {
        JedisCluster jedisCluster = new JedisCluster(redisConfig.getBase(), redisConfig.buildGenericObjectPoolConfig());
        return jedisCluster;
    }

}
