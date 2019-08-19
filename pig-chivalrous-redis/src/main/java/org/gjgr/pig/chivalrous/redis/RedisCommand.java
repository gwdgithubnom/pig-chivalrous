package org.gjgr.pig.chivalrous.redis;

import org.gjgr.pig.chivalrous.core.io.file.yml.YmlNode;
import org.gjgr.pig.chivalrous.core.lang.NonNull;
import org.gjgr.pig.chivalrous.core.net.UriBuilder;
import org.gjgr.pig.chivalrous.core.net.UriCommand;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;

/**
 * @Author gwd
 * @Time 11-27-2018 Tuesday
 * @Description: org.gjgr.pig.chivalrous.core:
 * @Target:
 * @More:
 */
public class RedisCommand {

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

    public static RedisClient redis(@NonNull String[] urls, String[] ports, String password, boolean type) {
        RedisConfig redisConfig = redisConfig(urls, ports, password, type);
        return redisClient(redisConfig);
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

    public static JedisPool jedisPool(RedisConfig redisConfig) {
        HostAndPort hostAndPort = redisConfig.getBase().iterator().next();
        JedisPool jedisPool =
                new JedisPool(redisConfig.getGenericObjectPoolConfig(), hostAndPort.getHost(), hostAndPort.getPort());
        return jedisPool;
    }

    public static JedisCluster jedisCluster(RedisConfig redisConfig) {
        JedisCluster jedisCluster = new JedisCluster(redisConfig.getBase(), redisConfig.getGenericObjectPoolConfig());
        return jedisCluster;
    }

}
