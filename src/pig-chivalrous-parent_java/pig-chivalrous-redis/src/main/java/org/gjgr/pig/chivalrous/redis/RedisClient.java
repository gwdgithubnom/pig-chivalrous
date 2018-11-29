package org.gjgr.pig.chivalrous.redis;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.BitPosParams;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.params.geo.GeoRadiusParam;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhangchuang on 16/2/17.
 */
public class RedisClient implements JedisCommands {

    private static final Logger logger = LoggerFactory.getLogger(RedisClient.class);

    private JedisProcessor processor;

    public RedisClient(RedisConfig redisConfig) {
        init(redisConfig);
    }

    private void init(RedisConfig redisConfig) {
        RedisPoolProxyConfig proxyConfig = new RedisPoolProxyConfig();
        proxyConfig.setMaxTotal(500);
        proxyConfig.setMinIdle(5);
        proxyConfig.setMaxWaitMillis(10000);
        proxyConfig.setTestOnBorrow(false);
        proxyConfig.setClusterRetryTimes(5);
        proxyConfig.setClusterTimeout(5000);
        proxyConfig.setPoolTimeOut(3000);
        proxyConfig.setProxyClusterType(1);
        proxyConfig.setPassword(redisConfig.getPassword());
        if (StringUtils.isEmpty(redisConfig.getCluster())) {
            proxyConfig.setProxyHost(redisConfig.getHost());
            proxyConfig.setProxyPort(redisConfig.getPort());
            processor = new JedisProcessor() {
                RedisClientProxy redisPoolProxy = new RedisClientProxy(proxyConfig);

                @Override
                public JedisCommands retreive() {
                    JedisPool jedisPool = redisPoolProxy.getJedisPool();
                    return redisPoolProxy.getResource(jedisPool);
                }
            };
        } else {
            proxyConfig.setProxyCluster(redisConfig.getCluster());
            processor = new JedisProcessor() {
                RedisClientProxy redisPoolProxy = new RedisClientProxy(proxyConfig);

                @Override
                public JedisCommands retreive() {
                    JedisCluster jedisCluster = redisPoolProxy.getCluster();
                    return jedisCluster;
                }
            };
        }
        logger.info("redisClient config: {}", proxyConfig.toString());
    }

    @Override
    public String set(String key, String val) {
        return processor.retreive().set(key, val);
    }

    @Override
    public String set(String key, String val, String nxxx, String expx, long time) {
        return processor.retreive().set(key, val, nxxx, expx, time);
    }

    @Override
    public String set(String key, String value, String nxxx) {
        return processor.retreive().set(key, value, nxxx);
    }

    @Override
    public String get(String key) {
        return processor.retreive().get(key);
    }

    @Override
    public Boolean exists(String key) {
        return processor.retreive().exists(key);
    }

    @Override
    public Long persist(String key) {
        return processor.retreive().persist(key);
    }

    @Override
    public String type(String key) {
        return processor.retreive().type(key);
    }

    @Override
    public Long expire(String key, int seconds) {
        return processor.retreive().expire(key, seconds);
    }

    @Override
    public Long pexpire(String key, long milliseconds) {
        return processor.retreive().pexpire(key, milliseconds);
    }

    @Override
    public Long expireAt(String key, long unixTime) {
        return processor.retreive().expireAt(key, unixTime);
    }

    @Override
    public Long pexpireAt(String key, long millisecondsTimestamp) {
        return processor.retreive().pexpireAt(key, millisecondsTimestamp);
    }

    @Override
    public Long ttl(String key) {
        return processor.retreive().ttl(key);
    }

    @Override
    public Long pttl(String key) {
        return processor.retreive().pttl(key);
    }

    @Override
    public Boolean setbit(String key, long offset, boolean value) {
        return processor.retreive().setbit(key, offset, value);
    }

    @Override
    public Boolean setbit(String key, long offset, String value) {
        return processor.retreive().setbit(key, offset, value);
    }

    @Override
    public Boolean getbit(String key, long offset) {
        return processor.retreive().getbit(key, offset);
    }

    @Override
    public Long setrange(String key, long offset, String value) {
        return processor.retreive().setrange(key, offset, value);
    }

    @Override
    public String getrange(String key, long startOffset, long endOffset) {
        return processor.retreive().getrange(key, startOffset, endOffset);
    }

    @Override
    public String getSet(String key, String value) {
        return processor.retreive().getSet(key, value);
    }

    @Override
    public Long setnx(String key, String value) {
        return processor.retreive().setnx(key, value);
    }

    @Override
    public String setex(String key, int seconds, String value) {
        return processor.retreive().setex(key, seconds, value);
    }

    @Override
    public String psetex(String key, long milliseconds, String value) {
        return processor.retreive().psetex(key, milliseconds, value);
    }

    @Override
    public Long decrBy(String key, long integer) {
        return processor.retreive().decrBy(key, integer);
    }

    @Override
    public Long decr(String key) {
        return processor.retreive().decr(key);
    }

    @Override
    public Long incrBy(String key, long integer) {
        return processor.retreive().incrBy(key, integer);
    }

    @Override
    public Double incrByFloat(String key, double value) {
        return processor.retreive().incrByFloat(key, value);
    }

    @Override
    public Long incr(String key) {
        return processor.retreive().incr(key);
    }

    @Override
    public Long append(String key, String value) {
        return processor.retreive().append(key, value);
    }

    @Override
    public String substr(String key, int start, int end) {
        return processor.retreive().substr(key, start, end);
    }

    @Override
    public Long hset(String key, String field, String value) {
        return processor.retreive().hset(key, field, value);
    }

    @Override
    public String hget(String key, String field) {
        return processor.retreive().hget(key, field);
    }

    @Override
    public Long hsetnx(String key, String field, String value) {
        return processor.retreive().hsetnx(key, field, value);
    }

    @Override
    public String hmset(String key, Map<String, String> hash) {
        return processor.retreive().hmset(key, hash);
    }

    @Override
    public List<String> hmget(String key, String... fields) {
        return processor.retreive().hmget(key, fields);
    }

    @Override
    public Long hincrBy(String key, String field, long value) {
        return processor.retreive().hincrBy(key, field, value);
    }

    @Override
    public Double hincrByFloat(String key, String field, double value) {
        return processor.retreive().hincrByFloat(key, field, value);
    }

    @Override
    public Boolean hexists(String key, String field) {
        return processor.retreive().hexists(key, field);
    }

    @Override
    public Long hdel(String key, String... field) {
        return processor.retreive().hdel(key, field);
    }

    @Override
    public Long hlen(String key) {
        return processor.retreive().hlen(key);
    }

    @Override
    public Set<String> hkeys(String key) {
        return processor.retreive().hkeys(key);
    }

    @Override
    public List<String> hvals(String key) {
        return processor.retreive().hvals(key);
    }

    @Override
    public Map<String, String> hgetAll(String key) {
        return processor.retreive().hgetAll(key);
    }

    @Override
    public Long rpush(String key, String... string) {
        return processor.retreive().rpush(key, string);
    }

    @Override
    public Long lpush(String key, String... string) {
        return processor.retreive().lpush(key, string);
    }

    @Override
    public Long llen(String key) {
        return processor.retreive().llen(key);
    }

    @Override
    public List<String> lrange(String key, long start, long end) {
        return processor.retreive().lrange(key, start, end);
    }

    @Override
    public String ltrim(String key, long start, long end) {
        return processor.retreive().ltrim(key, start, end);
    }

    @Override
    public String lindex(String key, long index) {
        return processor.retreive().lindex(key, index);
    }

    @Override
    public String lset(String key, long index, String value) {
        return processor.retreive().lset(key, index, value);
    }

    @Override
    public Long lrem(String key, long count, String value) {
        return processor.retreive().lrem(key, count, value);
    }

    @Override
    public String lpop(String key) {
        return processor.retreive().lpop(key);
    }

    @Override
    public String rpop(String key) {
        return processor.retreive().rpop(key);
    }

    @Override
    public Long sadd(String key, String... member) {
        return processor.retreive().sadd(key, member);
    }

    @Override
    public Set<String> smembers(String key) {
        return processor.retreive().smembers(key);
    }

    @Override
    public Long srem(String key, String... member) {
        return processor.retreive().srem(key, member);
    }

    @Override
    public String spop(String key) {
        return processor.retreive().spop(key);
    }

    @Override
    public Set<String> spop(String key, long count) {
        return processor.retreive().spop(key, count);
    }

    @Override
    public Long scard(String key) {
        return processor.retreive().scard(key);
    }

    @Override
    public Boolean sismember(String key, String member) {
        return processor.retreive().sismember(key, member);
    }

    @Override
    public String srandmember(String key) {
        return processor.retreive().srandmember(key);
    }

    @Override
    public List<String> srandmember(String key, int count) {
        return processor.retreive().srandmember(key, count);
    }

    @Override
    public Long strlen(String key) {
        return processor.retreive().strlen(key);
    }

    @Override
    public Long zadd(String key, double score, String member) {
        return processor.retreive().zadd(key, score, member);
    }

    @Override
    public Long zadd(String key, double score, String member, ZAddParams params) {
        return processor.retreive().zadd(key, score, member, params);
    }

    @Override
    public Long zadd(String key, Map<String, Double> scoreMembers) {
        return processor.retreive().zadd(key, scoreMembers);
    }

    @Override
    public Long zadd(String key, Map<String, Double> scoreMembers, ZAddParams params) {
        return processor.retreive().zadd(key, scoreMembers, params);
    }

    @Override
    public Set<String> zrange(String key, long start, long end) {
        return processor.retreive().zrange(key, start, end);
    }

    @Override
    public Long zrem(String key, String... member) {
        return processor.retreive().zrem(key, member);
    }

    @Override
    public Double zincrby(String key, double score, String member) {
        return processor.retreive().zincrby(key, score, member);
    }

    @Override
    public Double zincrby(String key, double score, String member, ZIncrByParams params) {
        return processor.retreive().zincrby(key, score, member, params);
    }

    @Override
    public Long zrank(String key, String member) {
        return processor.retreive().zrank(key, member);
    }

    @Override
    public Long zrevrank(String key, String member) {
        return processor.retreive().zrevrank(key, member);
    }

    @Override
    public Set<String> zrevrange(String key, long start, long end) {
        return processor.retreive().zrevrange(key, start, end);
    }

    @Override
    public Set<Tuple> zrangeWithScores(String key, long start, long end) {
        return processor.retreive().zrangeWithScores(key, start, end);
    }

    @Override
    public Set<Tuple> zrevrangeWithScores(String key, long start, long end) {
        return processor.retreive().zrevrangeWithScores(key, start, end);
    }

    @Override
    public Long zcard(String key) {
        return processor.retreive().zcard(key);
    }

    @Override
    public Double zscore(String key, String member) {
        return processor.retreive().zscore(key, member);
    }

    @Override
    public List<String> sort(String key) {
        return processor.retreive().sort(key);
    }

    @Override
    public List<String> sort(String key, SortingParams sortingParameters) {
        return processor.retreive().sort(key, sortingParameters);
    }

    @Override
    public Long zcount(String key, double min, double max) {
        return processor.retreive().zcount(key, min, max);
    }

    @Override
    public Long zcount(String key, String min, String max) {
        return processor.retreive().zcount(key, min, max);
    }

    @Override
    public Set<String> zrangeByScore(String key, double min, double max) {
        return processor.retreive().zrangeByScore(key, min, max);
    }

    @Override
    public Set<String> zrangeByScore(String key, String min, String max) {
        return processor.retreive().zrangeByScore(key, min, max);
    }

    @Override
    public Set<String> zrevrangeByScore(String key, double max, double min) {
        return processor.retreive().zrevrangeByScore(key, max, min);
    }

    @Override
    public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
        return processor.retreive().zrangeByScore(key, min, max, offset, count);
    }

    @Override
    public Set<String> zrevrangeByScore(String key, String max, String min) {
        return processor.retreive().zrevrangeByScore(key, max, min);
    }

    @Override
    public Set<String> zrangeByScore(String key, String min, String max, int offset, int count) {
        return processor.retreive().zrangeByScore(key, min, max, offset, count);
    }

    @Override
    public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
        return processor.retreive().zrevrangeByScore(key, max, min, offset, count);
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
        return processor.retreive().zrangeByScoreWithScores(key, min, max);
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
        return processor.retreive().zrevrangeByScoreWithScores(key, max, min);
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
        return processor.retreive().zrangeByScoreWithScores(key, min, max, offset, count);
    }

    @Override
    public Set<String> zrevrangeByScore(String key, String max, String min, int offset, int count) {
        return processor.retreive().zrevrangeByScore(key, max, min, offset, count);
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max) {
        return processor.retreive().zrangeByScoreWithScores(key, min, max);
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min) {
        return processor.retreive().zrevrangeByScoreWithScores(key, max, min);
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max, int offset, int count) {
        return processor.retreive().zrangeByScoreWithScores(key, min, max, offset, count);
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
        return processor.retreive().zrevrangeByScoreWithScores(key, max, min, offset, count);
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min, int offset, int count) {
        return processor.retreive().zrevrangeByScoreWithScores(key, max, min, offset, count);
    }

    @Override
    public Long zremrangeByRank(String key, long start, long end) {
        return processor.retreive().zremrangeByRank(key, start, end);
    }

    @Override
    public Long zremrangeByScore(String key, double start, double end) {
        return processor.retreive().zremrangeByScore(key, start, end);
    }

    @Override
    public Long zremrangeByScore(String key, String start, String end) {
        return processor.retreive().zremrangeByScore(key, start, end);
    }

    @Override
    public Long zlexcount(String key, String min, String max) {
        return processor.retreive().zlexcount(key, min, max);
    }

    @Override
    public Set<String> zrangeByLex(String key, String min, String max) {
        return processor.retreive().zrangeByLex(key, min, max);
    }

    @Override
    public Set<String> zrangeByLex(String key, String min, String max, int offset, int count) {
        return processor.retreive().zrangeByLex(key, min, max, offset, count);
    }

    @Override
    public Set<String> zrevrangeByLex(String key, String max, String min) {
        return processor.retreive().zrevrangeByLex(key, max, min);
    }

    @Override
    public Set<String> zrevrangeByLex(String key, String max, String min, int offset, int count) {
        return processor.retreive().zrevrangeByLex(key, max, min, offset, count);
    }

    @Override
    public Long zremrangeByLex(String key, String min, String max) {
        return processor.retreive().zremrangeByLex(key, min, max);
    }

    @Override
    public Long linsert(String key, BinaryClient.LIST_POSITION where, String pivot, String value) {
        return processor.retreive().linsert(key, where, pivot, value);
    }

    @Override
    public Long lpushx(String key, String... string) {
        return processor.retreive().lpushx(key, string);
    }

    @Override
    public Long rpushx(String key, String... string) {
        return processor.retreive().rpushx(key, string);
    }

    @Override
    public List<String> blpop(String arg) {
        return processor.retreive().blpop(arg);
    }

    @Override
    public List<String> blpop(int timeout, String key) {
        return processor.retreive().blpop(timeout, key);
    }

    @Override
    public List<String> brpop(String arg) {
        return processor.retreive().brpop(arg);
    }

    @Override
    public List<String> brpop(int timeout, String key) {
        return processor.retreive().brpop(timeout, key);
    }

    @Override
    public Long del(String key) {
        return processor.retreive().del(key);
    }

    @Override
    public String echo(String string) {
        return processor.retreive().echo(string);
    }

    @Override
    public Long move(String key, int dbIndex) {
        return processor.retreive().move(key, dbIndex);
    }

    @Override
    public Long bitcount(String key) {
        return processor.retreive().bitcount(key);
    }

    @Override
    public Long bitcount(String key, long start, long end) {
        return processor.retreive().bitcount(key, start, end);
    }

    @Override
    public Long bitpos(String key, boolean value) {
        return processor.retreive().bitpos(key, value);
    }

    @Override
    public Long bitpos(String key, boolean value, BitPosParams params) {
        return processor.retreive().bitpos(key, value, params);
    }

    @Override
    public ScanResult<Map.Entry<String, String>> hscan(String key, int cursor) {
        return processor.retreive().hscan(key, cursor);
    }

    @Override
    public ScanResult<String> sscan(String key, int cursor) {
        return processor.retreive().sscan(key, cursor);
    }

    @Override
    public ScanResult<Tuple> zscan(String key, int cursor) {
        return processor.retreive().zscan(key, cursor);
    }

    @Override
    public ScanResult<Map.Entry<String, String>> hscan(String key, String cursor) {
        return processor.retreive().hscan(key, cursor);
    }

    @Override
    public ScanResult<Map.Entry<String, String>> hscan(String key, String cursor, ScanParams params) {
        return processor.retreive().hscan(key, cursor, params);
    }

    @Override
    public ScanResult<String> sscan(String key, String cursor) {
        return processor.retreive().sscan(key, cursor);
    }

    @Override
    public ScanResult<String> sscan(String key, String cursor, ScanParams params) {
        return processor.retreive().sscan(key, cursor, params);
    }

    @Override
    public ScanResult<Tuple> zscan(String key, String cursor) {
        return processor.retreive().zscan(key, cursor);
    }

    @Override
    public ScanResult<Tuple> zscan(String key, String cursor, ScanParams params) {
        return processor.retreive().zscan(key, cursor, params);
    }

    @Override
    public Long pfadd(String key, String... elements) {
        return processor.retreive().pfadd(key, elements);
    }

    @Override
    public long pfcount(String key) {
        return 0;
    }

    @Override
    public Long geoadd(String key, double longitude, double latitude, String member) {
        return processor.retreive().geoadd(key, longitude, latitude, member);
    }

    @Override
    public Long geoadd(String key, Map<String, GeoCoordinate> memberCoordinateMap) {
        return processor.retreive().geoadd(key, memberCoordinateMap);
    }

    @Override
    public Double geodist(String key, String member1, String member2) {
        return processor.retreive().geodist(key, member1, member2);
    }

    @Override
    public Double geodist(String key, String member1, String member2, GeoUnit unit) {
        return processor.retreive().geodist(key, member1, member2, unit);
    }

    @Override
    public List<String> geohash(String key, String... members) {
        return processor.retreive().geohash(key, members);
    }

    @Override
    public List<GeoCoordinate> geopos(String key, String... members) {
        return processor.retreive().geopos(key, members);
    }

    @Override
    public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius,
                                             GeoUnit unit) {
        return processor.retreive().georadius(key, longitude, latitude, radius, unit);
    }

    @Override
    public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit,
                                             GeoRadiusParam param) {
        return processor.retreive().georadius(key, longitude, latitude, radius, unit, param);
    }

    @Override
    public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit) {
        return processor.retreive().georadiusByMember(key, member, radius, unit);
    }

    @Override
    public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit,
                                                     GeoRadiusParam param) {
        return processor.retreive().georadiusByMember(key, member, radius, unit, param);
    }

    @Override
    public List<Long> bitfield(String key, String... arguments) {
        return processor.retreive().bitfield(key, arguments);
    }

    interface JedisProcessor {
        JedisCommands retreive();
    }

}
