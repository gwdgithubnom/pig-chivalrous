package org.gjgr.pig.chivalrous.redis;

import org.gjgr.pig.chivalrous.core.lang.NonNull;

/**
 * @Author gwd
 * @Time 11-27-2018  Tuesday
 * @Description: developer.tools:
 * @Target:
 * @More:
 */
public class RedisCommand {

    public static RedisClient redisClient(RedisConfig redisConfig) {
        return new RedisClient(redisConfig);
    }

    public static RedisClient redis(@NonNull String uri, String password) {
        RedisConfig redisConfig = new RedisConfig();
        String[] uris = uri.split(":");
        if (uris.length == 2) {
            redisConfig.setHost(uris[0]);
            redisConfig.setPort(uris[1]);
            if (password != null) {
                redisConfig.setPassword(password);
            }
            return redisClient(redisConfig);
        } else {
            throw new UnsupportedOperationException("could not parse the uri, format style: ip:port. " + uri);
        }
    }

    //ip:port
    public static RedisClient redis(@NonNull String uri) {
        return redis(uri, null);
    }

    //ip:port
    public static RedisClient redis(@NonNull String url, int port) {
        String uri = url + ":" + port;
        return redis(uri, null);
    }

    public static RedisClient redisCluster(@NonNull String uri) {
        return redisCluster(uri, null);
    }

    public static RedisClient redisCluster(String... uris) {
        return redisCluster(uris, null);
    }

    public static RedisClient redisCluster(String[] uris, String password) {
        RedisConfig redisConfig = new RedisConfig();
        if (password != null) {
            redisConfig.setPassword(password);
        }
        redisConfig.append(uris);
        return redisClient(redisConfig);
    }

    //ip:port{}ip:port
    public static RedisClient redisCluster(@NonNull String uri, String password) {
        RedisConfig redisConfig = new RedisConfig();
        redisConfig.setCluster(uri);
        if (password != null) {
            redisConfig.setPassword(password);
        }
        try {
            return redisClient(redisConfig);
        } catch (Exception e) {
            throw new UnsupportedOperationException("could not parse the uri, format style: ip1:port,ip2:port. " + uri);
        }
    }

}
