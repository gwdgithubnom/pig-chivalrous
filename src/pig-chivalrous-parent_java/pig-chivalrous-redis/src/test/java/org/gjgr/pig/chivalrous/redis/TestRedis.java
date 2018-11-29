package org.gjgr.pig.chivalrous.redis;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author gwd
 * @Time 11-28-2018  Wednesday
 * @Description: developer.tools:
 * @Target:
 * @More:
 */
public class TestRedis {

    @Ignore
    @Test
    public void test(){
        String uri="demo:port";
        RedisClient redisClient;
        //RedisClient redisClient1=RedisCommand.redisCluster("host:port");
        //redisClient=RedisCommand.redisCluster(uris);
        String[] uris=new String[1];
        uris[0]=uri;
        redisClient=RedisCommand.redisCluster(uri);
        String value=redisClient.get("test");
        Assert.assertEquals("dddddddd",value);
        redisClient.set("test",null);
        redisClient.append("test","micsql");
        Assert.assertEquals(null,redisClient.get("test"));
    }
}
