package org.gjgr.pig.chivalrous.redis;

import org.gjgr.pig.chivalrous.core.util.RandomCommand;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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

    @Test
    public void checkRedis() {
        RedisClient redisClient = RedisCommand.redis("127.0.0.1", 6379);
        JedisPool jedisPool = redisClient.jedisPool();
        Jedis jedis = jedisPool.getResource();
        System.out.println(jedis.exists("test"));
        System.out.println(jedis.get("test"));
    }


    @Test
    public void checkJedis() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(10);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, "127.0.0.1", 6379);
        Jedis jedis = jedisPool.getResource();
        Assert.assertEquals("test", jedis.get("test"));
    }

    @Test
    public void normalJedis() {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        Assert.assertEquals("test", jedis.get("test"));
    }

    @Test
    public void jedisClient() throws InterruptedException {
        RedisClient redisClient = RedisCommand.redis("127.0.0.1", 6379);
        String string = redisClient.jedisCommands().set("test", "test");
        String[] threads = {"thread1", "thread2", "thread3", "thread4", "thread5"};
        List<String> stringList = Arrays.asList(threads);
        AtomicInteger atomicInteger = new AtomicInteger();
        while (redisClient != null) {
            try {
                new Runnable() {
                    @Override
                    public void run() {
                        stringList.parallelStream().forEach(s -> {
                            boolean b = redisClient.jedisCommands().exists("test");
                            System.out.println("current " + atomicInteger.incrementAndGet() + " " + s + " -> " + b);
                            int i = RandomCommand.randomInt(10);
                            if (i > 6) {
                                if (!b) {
                                    redisClient.jedisCommands().set("test", "test");
                                    System.out.println(s + " -> " + b + " has put key test to redis.");
                                } else {
                                    redisClient.jedisCommands().del("test");
                                    System.out.println(s + " -> " + b + "has been put test remove from redis.");
                                }
                            } else {
                                System.out.println(s + " -> " + b + " random is slow then 6, " + i + " keep current status.");
                            }
                        });
                    }
                }.run();
            } catch (Exception e) {
                e.printStackTrace();
                Thread.sleep(3000);
            }
            Thread.sleep(1500);
        }
    }

    @Test
    public void testByOthers() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(500); // 500个连接
        poolConfig.setMaxIdle(32); // 最大的空闲连接
        poolConfig.setMaxWaitMillis(100 * 1000); // 最长的等待时间
        poolConfig.setTestOnBorrow(true); // 获得一个jedis连接时检测可用性

        JedisPool jedisPool = new JedisPool(poolConfig, "127.0.0.1", 6379);
        Jedis jedis = jedisPool.getResource();
        Assert.assertEquals("test", jedis.get("test"));
    }
}
