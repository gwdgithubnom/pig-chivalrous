package org.gjgr.pig.chivalrous.redis;

import org.junit.Ignore;
import org.junit.Test;

/**
 * @Author gwd
 * @Time 11-28-2018 Wednesday
 * @Description: org.gjgr.pig.chivalrous.core:
 * @Target:
 * @More:
 */
public class TestRedis {

    @Ignore
    @Test
    public void test() {
        String uri = "demo:port";
        RedisClient redisClient;
        // RedisClient redisClient1=RedisCommand.redisCluster("host:port");
        // redisClient=RedisCommand.redisCluster(uris);
        String[] uris = new String[1];
        uris[0] = uri;
        // redisClient = RedisCommand.redisCluster(uri);
        // String value = redisClient.get("test");
        // Assert.assertEquals("dddddddd", value);
        // redisClient.set("test", null);
        // redisClient.append("test", "micsql");
        // Assert.assertEquals(null, redisClient.get("test"));
    }
}
