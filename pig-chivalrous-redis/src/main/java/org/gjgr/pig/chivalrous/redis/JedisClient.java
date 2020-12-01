package org.gjgr.pig.chivalrous.redis;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.gjgr.pig.chivalrous.log.SystemLogger;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.BitOP;
import redis.clients.jedis.BitPosParams;
import redis.clients.jedis.Client;
import redis.clients.jedis.DebugParams;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisMonitor;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.PipelineBlock;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.TransactionBlock;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.ZParams;
import redis.clients.jedis.params.geo.GeoRadiusParam;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;
import redis.clients.util.Pool;
import redis.clients.util.Slowlog;

/**
 * @Author gwd
 * @Time 06-14-2019  Friday
 * @Description: developer.tools:
 * @Target:
 * @More:
 */
public class JedisClient extends Jedis implements Serializable {

    private static ThreadLocal<Jedis> jedis = new ThreadLocal<>();
    private JedisPool jedisPool;

    public JedisClient(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
        if (jedis.get() == null) {
            this.jedis.set(getResource(this.jedisPool));
        }
    }

    public static synchronized Jedis getResource(JedisPool jedisPool) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
        } catch (Exception e) {
            if (jedis != null) {
                jedis.close();
            }
        }
        return jedis;
    }

    @Override
    public String set(String key, String value) {
        String string = getJedis().set(key, value);
        free();
        return string;
    }

    @Override
    public String set(String key, String value, String nxxx, String expx, long time) {
        String string = getJedis().set(key, value, nxxx, expx, time);
        free();
        return string;
    }

    @Override
    public String get(String key) {
        String string = getJedis().get(key);
        free();
        return string;
    }

    @Override
    public Long exists(String... keys) {
        Long o = getJedis().exists(keys);
        free();
        return o;
    }

    @Override
    public Boolean exists(String key) {
        Boolean o = getJedis().exists(key);
        free();
        return o;
    }

    @Override
    public Long del(String... keys) {
        Long o = getJedis().del(keys);
        free();
        return o;
    }

    @Override
    public Long del(String key) {
        Long o = getJedis().del(key);
        free();
        return o;
    }

    @Override
    public String type(String key) {
        String o = getJedis().type(key);
        free();
        return o;
    }

    @Override
    public Set<String> keys(String pattern) {
        Set<String> o = getJedis().keys(pattern);
        free();
        return o;
    }

    @Override
    public String randomKey() {
        String o = getJedis().randomKey();
        free();
        return o;
    }

    @Override
    public String rename(String oldkey, String newkey) {
        String o = getJedis().rename(oldkey, newkey);
        free();
        return o;
    }

    @Override
    public Long renamenx(String oldkey, String newkey) {
        Long o = getJedis().renamenx(oldkey, newkey);
        free();
        return o;
    }

    @Override
    public Long expire(String key, int seconds) {
        Long o = getJedis().expire(key, seconds);
        free();
        return o;
    }

    @Override
    public Long expireAt(String key, long unixTime) {
        Long o = getJedis().expireAt(key, unixTime);
        free();
        return o;
    }

    @Override
    public Long ttl(String key) {
        Long o = getJedis().ttl(key);
        free();
        return o;
    }

    @Override
    public Long move(String key, int dbIndex) {
        Long o = getJedis().move(key, dbIndex);
        free();
        return o;
    }

    @Override
    public String getSet(String key, String value) {
        String o = getJedis().getSet(key, value);
        free();
        return o;
    }

    @Override
    public List<String> mget(String... keys) {
        List<String> o = getJedis().mget(keys);
        free();
        return o;
    }

    @Override
    public Long setnx(String key, String value) {
        Long o = getJedis().setnx(key, value);
        free();
        return o;
    }

    @Override
    public String setex(String key, int seconds, String value) {
        String o = getJedis().setex(key, seconds, value);
        free();
        return o;
    }

    @Override
    public String mset(String... keysvalues) {
        String o = getJedis().mset(keysvalues);
        free();
        return o;
    }

    @Override
    public Long msetnx(String... keysvalues) {
        Long o = getJedis().msetnx(keysvalues);
        free();
        return o;
    }

    @Override
    public Long decrBy(String key, long integer) {
        Long o = getJedis().decrBy(key, integer);
        free();
        return o;
    }

    @Override
    public Long decr(String key) {
        Long o = getJedis().decr(key);
        free();
        return o;
    }

    @Override
    public Long incrBy(String key, long integer) {
        Long o = getJedis().incrBy(key, integer);
        free();
        return o;
    }

    @Override
    public Double incrByFloat(String key, double value) {
        Double o = getJedis().incrByFloat(key, value);
        free();
        return o;
    }

    @Override
    public Long incr(String key) {
        Long o = getJedis().incr(key);
        free();
        return o;
    }

    @Override
    public Long append(String key, String value) {
        Long o = getJedis().append(key, value);
        free();
        return o;
    }

    @Override
    public String substr(String key, int start, int end) {
        String o = getJedis().substr(key, start, end);
        free();
        return o;
    }

    @Override
    public Long hset(String key, String field, String value) {
        Long o = getJedis().hset(key, field, value);
        free();
        return o;
    }

    @Override
    public String hget(String key, String field) {
        String o = getJedis().hget(key, field);
        free();
        return o;
    }

    @Override
    public Long hsetnx(String key, String field, String value) {
        Long o = getJedis().hsetnx(key, field, value);
        free();
        return o;
    }

    @Override
    public String hmset(String key, Map<String, String> hash) {
        String o = getJedis().hmset(key, hash);
        free();
        return o;
    }

    @Override
    public List<String> hmget(String key, String... fields) {
        List<String> o = getJedis().hmget(key, fields);
        free();
        return o;
    }

    @Override
    public Long hincrBy(String key, String field, long value) {
        Long o = getJedis().hincrBy(key, field, value);
        free();
        return o;
    }

    @Override
    public Double hincrByFloat(String key, String field, double value) {
        Double o = getJedis().hincrByFloat(key, field, value);
        free();
        return o;
    }

    @Override
    public Boolean hexists(String key, String field) {
        Boolean o = getJedis().hexists(key, field);
        free();
        return o;
    }

    @Override
    public Long hdel(String key, String... fields) {
        Long o = getJedis().hdel(key, fields);
        free();
        return o;
    }

    @Override
    public Long hlen(String key) {
        Long o = getJedis().hlen(key);
        free();
        return o;
    }

    @Override
    public Set<String> hkeys(String key) {
        Set<String> o = getJedis().hkeys(key);
        free();
        return o;
    }

    @Override
    public List<String> hvals(String key) {
        List<String> o = getJedis().hvals(key);
        free();
        return o;
    }

    @Override
    public Map<String, String> hgetAll(String key) {
        Map<String, String> o = getJedis().hgetAll(key);
        free();
        return o;
    }

    @Override
    public Long rpush(String key, String... strings) {
        Long o = getJedis().rpush(key, strings);
        free();
        return o;
    }

    @Override
    public Long lpush(String key, String... strings) {
        Long o = getJedis().lpush(key, strings);
        free();
        return o;
    }

    @Override
    public Long llen(String key) {
        Long o = getJedis().llen(key);
        free();
        return o;
    }

    @Override
    public List<String> lrange(String key, long start, long end) {
        List<String> o = getJedis().lrange(key, start, end);
        free();
        return o;
    }

    @Override
    public String ltrim(String key, long start, long end) {

        String o = getJedis().ltrim(key, start, end);
        free();
        return o;

    }

    @Override
    public String lindex(String key, long index) {
        String o = getJedis().lindex(key, index);
        free();
        return o;
    }

    @Override
    public String lset(String key, long index, String value) {
        String o = getJedis().lset(key, index, value);
        free();
        return o;
    }

    @Override
    public Long lrem(String key, long count, String value) {
        Long o = getJedis().lrem(key, count, value);
        free();
        return o;
    }

    @Override
    public String lpop(String key) {
        String o = getJedis().lpop(key);
        free();
        return o;
    }

    @Override
    public String rpop(String key) {
        String o = getJedis().rpop(key);
        free();
        return o;
    }

    @Override
    public String rpoplpush(String srckey, String dstkey) {
        String o = getJedis().rpoplpush(srckey, dstkey);
        free();
        return o;
    }

    @Override
    public Long sadd(String key, String... members) {
        Long o = getJedis().sadd(key, members);
        free();
        return o;
    }

    @Override
    public Set<String> smembers(String key) {
        Set<String> o = getJedis().smembers(key);
        free();
        return o;
    }

    @Override
    public Long srem(String key, String... members) {
        Long o = getJedis().srem(key, members);
        free();
        return o;
    }

    @Override
    public String spop(String key) {
        String o = getJedis().spop(key);
        free();
        return o;
    }

    @Override
    public Set<String> spop(String key, long count) {
        Set<String> o = getJedis().spop(key, count);
        free();
        return o;
    }

    @Override
    public Long smove(String srckey, String dstkey, String member) {
        Long o = getJedis().smove(srckey, dstkey, member);
        free();
        return o;
    }

    @Override
    public Long scard(String key) {
        Long o = getJedis().scard(key);
        free();
        return o;
    }

    @Override
    public Boolean sismember(String key, String member) {
        Boolean o = getJedis().sismember(key, member);
        free();
        return o;
    }

    @Override
    public Set<String> sinter(String... keys) {
        Set<String> o = getJedis().sinter(keys);
        free();
        return o;
    }

    @Override
    public Long sinterstore(String dstkey, String... keys) {
        Long o = getJedis().sinterstore(dstkey, keys);
        free();
        return o;
    }

    @Override
    public Set<String> sunion(String... keys) {
        Set<String> o = getJedis().sunion(keys);
        free();
        return o;
    }

    @Override
    public Long sunionstore(String dstkey, String... keys) {
        Long o = getJedis().sunionstore(dstkey, keys);
        free();
        return o;
    }

    @Override
    public Set<String> sdiff(String... keys) {
        Set<String> o = getJedis().sdiff(keys);
        free();
        return o;
    }

    @Override
    public Long sdiffstore(String dstkey, String... keys) {
        Long o = getJedis().sdiffstore(dstkey, keys);
        free();
        return o;
    }

    @Override
    public String srandmember(String key) {
        String o = getJedis().srandmember(key);
        free();
        return o;
    }

    @Override
    public List<String> srandmember(String key, int count) {
        List<String> o = getJedis().srandmember(key, count);
        free();
        return o;
    }

    @Override
    public Long zadd(String key, double score, String member) {
        Long o = getJedis().zadd(key, score, member);
        free();
        return o;
    }

    @Override
    public Long zadd(String key, double score, String member, ZAddParams params) {
        Long o = getJedis().zadd(key, score, member, params);
        free();
        return o;
    }

    @Override
    public Long zadd(String key, Map<String, Double> scoreMembers) {
        Long o = getJedis().zadd(key, scoreMembers);
        free();
        return o;
    }

    @Override
    public Long zadd(String key, Map<String, Double> scoreMembers, ZAddParams params) {
        Long o = getJedis().zadd(key, scoreMembers, params);
        free();
        return o;
    }

    @Override
    public Set<String> zrange(String key, long start, long end) {
        Set<String> o = getJedis().zrange(key, start, end);
        free();
        return o;
    }

    @Override
    public Long zrem(String key, String... members) {
        Long o = getJedis().zrem(key, members);
        free();
        return o;
    }

    @Override
    public Double zincrby(String key, double score, String member) {
        Double o = getJedis().zincrby(key, score, member);
        free();
        return o;
    }

    @Override
    public Double zincrby(String key, double score, String member, ZIncrByParams params) {
        Double o = getJedis().zincrby(key, score, member, params);
        free();
        return o;
    }

    @Override
    public Long zrank(String key, String member) {
        Long o = getJedis().zrank(key, member);
        free();
        return o;
    }

    @Override
    public Long zrevrank(String key, String member) {
        Long o = getJedis().zrevrank(key, member);
        free();
        return o;
    }

    @Override
    public Set<String> zrevrange(String key, long start, long end) {
        Set<String> o = getJedis().zrevrange(key, start, end);
        free();
        return o;
    }

    @Override
    public Set<Tuple> zrangeWithScores(String key, long start, long end) {
        Set<Tuple> o = getJedis().zrangeWithScores(key, start, end);
        free();
        return o;
    }

    @Override
    public Set<Tuple> zrevrangeWithScores(String key, long start, long end) {
        Set<Tuple> o = getJedis().zrevrangeWithScores(key, start, end);
        free();
        return o;
    }

    @Override
    public Long zcard(String key) {
        Long o = getJedis().zcard(key);
        free();
        return o;
    }

    @Override
    public Double zscore(String key, String member) {
        Double o = getJedis().zscore(key, member);
        free();
        return o;
    }

    @Override
    public String watch(String... keys) {
        String o = getJedis().watch(keys);
        free();
        return o;
    }

    @Override
    public List<String> sort(String key) {
        List<String> o = getJedis().sort(key);
        free();
        return o;
    }

    @Override
    public List<String> sort(String key, SortingParams sortingParameters) {
        List<String> o = getJedis().sort(key, sortingParameters);
        free();
        return o;
    }

    @Override
    public List<String> blpop(int timeout, String... keys) {
        List<String> o = getJedis().blpop(timeout, keys);
        free();
        return o;
    }

    @Override
    public List<String> blpop(String... args) {
        List<String> o = getJedis().blpop(args);
        free();
        return o;
    }

    @Override
    public List<String> brpop(String... args) {
        List<String> o = getJedis().brpop(args);
        free();
        return o;
    }

    @Override
    public List<String> blpop(String arg) {
        List<String> o = getJedis().blpop(arg);
        free();
        return o;
    }

    @Override
    public List<String> brpop(String arg) {
        List<String> o = getJedis().brpop(arg);
        free();
        return o;
    }

    @Override
    public Long sort(String key, SortingParams sortingParameters, String dstkey) {
        Long o = getJedis().sort(key, sortingParameters, dstkey);
        free();
        return o;
    }

    @Override
    public Long sort(String key, String dstkey) {
        Long o = getJedis().sort(key, dstkey);
        free();
        return o;
    }

    @Override
    public List<String> brpop(int timeout, String... keys) {
        List<String> o = getJedis().brpop(timeout, keys);
        free();
        return o;
    }

    @Override
    public Long zcount(String key, double min, double max) {
        Long o = getJedis().zcount(key, min, max);
        free();
        return o;
    }

    @Override
    public Long zcount(String key, String min, String max) {
        Long o = getJedis().zcount(key, min, max);
        free();
        return o;
    }

    @Override
    public Set<String> zrangeByScore(String key, double min, double max) {
        Set<String> o = getJedis().zrangeByScore(key, min, max);
        free();
        return o;
    }

    @Override
    public Set<String> zrangeByScore(String key, String min, String max) {
        Set<String> o = getJedis().zrangeByScore(key, min, max);
        free();
        return o;
    }

    @Override
    public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
        Set<String> o = getJedis().zrangeByScore(key, min, max, offset, count);
        free();
        return o;
    }

    @Override
    public Set<String> zrangeByScore(String key, String min, String max, int offset, int count) {
        Set<String> o = getJedis().zrangeByScore(key, min, max, offset, count);
        free();
        return o;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
        Set<Tuple> o = getJedis().zrangeByScoreWithScores(key, min, max);
        free();
        return o;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max) {
        Set<Tuple> o = getJedis().zrangeByScoreWithScores(key, min, max);
        free();
        return o;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
        Set<Tuple> o = getJedis().zrangeByScoreWithScores(key, min, max, offset, count);
        free();
        return o;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max, int offset, int count) {
        Set<Tuple> o = getJedis().zrangeByScoreWithScores(key, min, max, offset, count);
        free();
        return o;
    }

    @Override
    public Set<String> zrevrangeByScore(String key, double max, double min) {
        Set<String> o = getJedis().zrevrangeByScore(key, max, min);
        free();
        return o;
    }

    @Override
    public Set<String> zrevrangeByScore(String key, String max, String min) {
        Set<String> o = getJedis().zrevrangeByScore(key, max, min);
        free();
        return o;
    }

    @Override
    public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
        Set<String> o = getJedis().zrevrangeByScore(key, max, min, offset, count);
        free();
        return o;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
        Set<Tuple> o = getJedis().zrevrangeByScoreWithScores(key, max, min);
        free();
        return o;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
        Set<Tuple> o = getJedis().zrevrangeByScoreWithScores(key, max, min, offset, count);
        free();
        return o;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min, int offset, int count) {
        Set<Tuple> o = getJedis().zrevrangeByScoreWithScores(key, max, min, offset, count);
        free();
        return o;
    }

    @Override
    public Set<String> zrevrangeByScore(String key, String max, String min, int offset, int count) {
        Set<String> o = getJedis().zrevrangeByScore(key, max, min, offset, count);
        free();
        return o;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min) {
        Set<Tuple> o = getJedis().zrevrangeByScoreWithScores(key, max, min);
        free();
        return o;
    }

    @Override
    public Long zremrangeByRank(String key, long start, long end) {
        Long o = getJedis().zremrangeByRank(key, start, end);
        free();
        return o;
    }

    @Override
    public Long zremrangeByScore(String key, double start, double end) {
        Long o = getJedis().zremrangeByScore(key, start, end);
        free();
        return o;
    }

    @Override
    public Long zremrangeByScore(String key, String start, String end) {
        Long o = getJedis().zremrangeByScore(key, start, end);
        free();
        return o;
    }

    @Override
    public Long zunionstore(String dstkey, String... sets) {
        Long o = getJedis().zunionstore(dstkey, sets);
        free();
        return o;
    }

    @Override
    public Long zunionstore(String dstkey, ZParams params, String... sets) {
        Long o = getJedis().zunionstore(dstkey, params, sets);
        free();
        return o;
    }

    @Override
    public Long zinterstore(String dstkey, String... sets) {
        Long o = getJedis().zinterstore(dstkey, sets);
        free();
        return o;
    }

    @Override
    public Long zinterstore(String dstkey, ZParams params, String... sets) {
        Long o = getJedis().zinterstore(dstkey, params, sets);
        free();
        return o;
    }

    @Override
    public Long zlexcount(String key, String min, String max) {
        Long o = getJedis().zlexcount(key, min, max);
        free();
        return o;
    }

    @Override
    public Set<String> zrangeByLex(String key, String min, String max) {
        Set<String> o = getJedis().zrangeByLex(key, min, max);
        free();
        return o;
    }

    @Override
    public Set<String> zrangeByLex(String key, String min, String max, int offset, int count) {
        Set<String> o = getJedis().zrangeByLex(key, min, max, offset, count);
        free();
        return o;
    }

    @Override
    public Set<String> zrevrangeByLex(String key, String max, String min) {
        Set<String> o = getJedis().zrevrangeByLex(key, max, min);
        free();
        return o;
    }

    @Override
    public Set<String> zrevrangeByLex(String key, String max, String min, int offset, int count) {
        Set<String> o = getJedis().zrevrangeByLex(key, max, min, offset, count);
        free();
        return o;
    }

    @Override
    public Long zremrangeByLex(String key, String min, String max) {
        Long o = getJedis().zremrangeByLex(key, min, max);
        free();
        return o;
    }

    @Override
    public Long strlen(String key) {
        Long o = getJedis().strlen(key);
        free();
        return o;
    }

    @Override
    public Long lpushx(String key, String... string) {
        Long o = getJedis().lpushx(key, string);
        free();
        return o;
    }

    @Override
    public Long persist(String key) {
        Long o = getJedis().persist(key);
        free();
        return o;
    }

    @Override
    public Long rpushx(String key, String... string) {
        Long o = getJedis().rpushx(key, string);
        free();
        return o;
    }

    @Override
    public String echo(String string) {
        String o = getJedis().echo(string);
        free();
        return o;
    }

    @Override
    public Long linsert(String key, BinaryClient.LIST_POSITION where, String pivot, String value) {
        Long o = getJedis().linsert(key, where, pivot, value);
        free();
        return o;
    }

    @Override
    public String brpoplpush(String source, String destination, int timeout) {
        String o = getJedis().brpoplpush(source, destination, timeout);
        free();
        return o;
    }

    @Override
    public Boolean setbit(String key, long offset, boolean value) {
        Boolean o = getJedis().setbit(key, offset, value);
        free();
        return o;
    }

    @Override
    public Boolean setbit(String key, long offset, String value) {
        Boolean o = getJedis().setbit(key, offset, value);
        free();
        return o;
    }

    @Override
    public Boolean getbit(String key, long offset) {
        Boolean o = getJedis().getbit(key, offset);
        free();
        return o;
    }

    @Override
    public Long setrange(String key, long offset, String value) {
        Long o = getJedis().setrange(key, offset, value);
        free();
        return o;
    }

    @Override
    public String getrange(String key, long startOffset, long endOffset) {
        String o = getJedis().getrange(key, startOffset, endOffset);
        free();
        return o;
    }

    @Override
    public Long bitpos(String key, boolean value) {
        Long o = getJedis().bitpos(key, value);
        free();
        return o;
    }

    @Override
    public Long bitpos(String key, boolean value, BitPosParams params) {
        Long o = getJedis().bitpos(key, value, params);
        free();
        return o;
    }

    @Override
    public List<String> configGet(String pattern) {
        List<String> o = getJedis().configGet(pattern);
        free();
        return o;
    }

    @Override
    public String configSet(String parameter, String value) {
        String o = getJedis().configSet(parameter, value);
        free();
        return o;
    }

    @Override
    public Object eval(String script, int keyCount, String... params) {
        Object o = getJedis().eval(script, keyCount, params);
        free();
        return o;
    }

    @Override
    public void subscribe(JedisPubSub jedisPubSub, String... channels) {
        getJedis().subscribe(jedisPubSub, channels);
        free();
        return;
    }

    @Override
    public Long publish(String channel, String message) {
        Long o = getJedis().publish(channel, message);
        free();
        return o;
    }

    @Override
    public void psubscribe(JedisPubSub jedisPubSub, String... patterns) {
        getJedis().psubscribe(jedisPubSub, patterns);
        free();
        return;
    }

    @Override
    public Object eval(String script, List<String> keys, List<String> args) {
        Object o = getJedis().eval(script, keys, args);
        free();
        return o;
    }

    @Override
    public Object eval(String script) {
        Object o = getJedis().eval(script);
        free();
        return o;
    }

    @Override
    public Object evalsha(String script) {
        Object o = getJedis().evalsha(script);
        free();
        return o;
    }

    @Override
    public Object evalsha(String sha1, List<String> keys, List<String> args) {
        Object o = getJedis().evalsha(sha1, keys, args);
        free();
        return o;
    }

    @Override
    public Object evalsha(String sha1, int keyCount, String... params) {
        Object o = getJedis().evalsha(sha1, keyCount, params);
        free();
        return o;
    }

    @Override
    public Boolean scriptExists(String sha1) {
        Boolean o = getJedis().scriptExists(sha1);
        free();
        return o;
    }

    @Override
    public List<Boolean> scriptExists(String... sha1) {
        List<Boolean> o = getJedis().scriptExists(sha1);
        free();
        return o;
    }

    @Override
    public String scriptLoad(String script) {
        String o = getJedis().scriptLoad(script);
        free();
        return o;
    }

    @Override
    public List<Slowlog> slowlogGet() {
        List<Slowlog> o = getJedis().slowlogGet();
        free();
        return o;
    }

    @Override
    public List<Slowlog> slowlogGet(long entries) {
        List<Slowlog> o = getJedis().slowlogGet(entries);
        free();
        return o;
    }

    @Override
    public Long objectRefcount(String string) {
        Long o = getJedis().objectRefcount(string);
        free();
        return o;
    }

    @Override
    public String objectEncoding(String string) {
        String o = getJedis().objectEncoding(string);
        free();
        return o;
    }

    @Override
    public Long objectIdletime(String string) {
        Long o = getJedis().objectIdletime(string);
        free();
        return o;
    }

    @Override
    public Long bitcount(String key) {
        Long o = getJedis().bitcount(key);
        free();
        return o;
    }

    @Override
    public Long bitcount(String key, long start, long end) {
        Long o = getJedis().bitcount(key, start, end);
        free();
        return o;
    }

    @Override
    public Long bitop(BitOP op, String destKey, String... srcKeys) {
        Long o = getJedis().bitop(op, destKey, srcKeys);
        free();
        return o;
    }

    @Override
    public List<Map<String, String>> sentinelMasters() {
        List<Map<String, String>> o = getJedis().sentinelMasters();
        free();
        return o;
    }

    @Override
    public List<String> sentinelGetMasterAddrByName(String masterName) {
        List<String> o = getJedis().sentinelGetMasterAddrByName(masterName);
        free();
        return o;
    }

    @Override
    public Long sentinelReset(String pattern) {
        Long o = getJedis().sentinelReset(pattern);
        free();
        return o;
    }

    @Override
    public List<Map<String, String>> sentinelSlaves(String masterName) {
        List<Map<String, String>> o = getJedis().sentinelSlaves(masterName);
        free();
        return o;
    }

    @Override
    public String sentinelFailover(String masterName) {
        String o = getJedis().sentinelFailover(masterName);
        free();
        return o;
    }

    @Override
    public String sentinelMonitor(String masterName, String ip, int port, int quorum) {
        String o = getJedis().sentinelMonitor(masterName, ip, port, quorum);
        free();
        return o;
    }

    @Override
    public String sentinelRemove(String masterName) {
        String o = getJedis().sentinelRemove(masterName);
        free();
        return o;
    }

    @Override
    public String sentinelSet(String masterName, Map<String, String> parameterMap) {
        String o = getJedis().sentinelSet(masterName, parameterMap);
        free();
        return o;
    }

    @Override
    public byte[] dump(String key) {
        byte[] o = getJedis().dump(key);
        free();
        return o;
    }

    @Override
    public String restore(String key, int ttl, byte[] serializedValue) {
        String o = getJedis().restore(key, ttl, serializedValue);
        free();
        return o;
    }

    @Override
    public Long pexpire(String key, int milliseconds) {
        Long o = getJedis().pexpire(key, milliseconds);
        free();
        return o;
    }

    @Override
    public Long pexpire(String key, long milliseconds) {
        Long o = getJedis().pexpire(key, milliseconds);
        free();
        return o;
    }

    @Override
    public Long pexpireAt(String key, long millisecondsTimestamp) {
        Long o = getJedis().pexpireAt(key, millisecondsTimestamp);
        free();
        return o;
    }

    @Override
    public Long pttl(String key) {
        Long o = getJedis().pttl(key);
        free();
        return o;
    }

    @Override
    public String psetex(String key, int milliseconds, String value) {
        String o = getJedis().psetex(key, milliseconds, value);
        free();
        return o;
    }

    @Override
    public String psetex(String key, long milliseconds, String value) {
        String o = getJedis().psetex(key, milliseconds, value);
        free();
        return o;
    }

    @Override
    public String set(String key, String value, String nxxx) {
        String o = getJedis().set(key, value, nxxx);
        free();
        return o;
    }

    @Override
    public String set(String key, String value, String nxxx, String expx, int time) {
        String o = getJedis().set(key, value, nxxx, expx, time);
        free();
        return o;
    }

    @Override
    public String clientKill(String client) {
        String o = getJedis().clientKill(client);
        free();
        return o;
    }

    @Override
    public String clientSetname(String name) {
        String o = getJedis().clientSetname(name);
        free();
        return o;
    }

    @Override
    public String migrate(String host, int port, String key, int destinationDb, int timeout) {
        String o = getJedis().migrate(host, port, key, destinationDb, timeout);
        free();
        return o;
    }

    @Override
    public ScanResult<String> scan(int cursor) {
        ScanResult<String> o = getJedis().scan(cursor);
        free();
        return o;
    }

    @Override
    public ScanResult<String> scan(int cursor, ScanParams params) {
        ScanResult<String> o = getJedis().scan(cursor, params);
        free();
        return o;
    }

    @Override
    public ScanResult<Map.Entry<String, String>> hscan(String key, int cursor) {
        ScanResult<Map.Entry<String, String>> o = getJedis().hscan(key, cursor);
        free();
        return o;
    }

    @Override
    public ScanResult<Map.Entry<String, String>> hscan(String key, int cursor, ScanParams params) {
        ScanResult<Map.Entry<String, String>> o = getJedis().hscan(key, cursor, params);
        free();
        return o;
    }

    @Override
    public ScanResult<String> sscan(String key, int cursor) {
        ScanResult<String> o = getJedis().sscan(key, cursor);
        free();
        return o;
    }

    @Override
    public ScanResult<String> sscan(String key, int cursor, ScanParams params) {
        ScanResult<String> o = getJedis().sscan(key, cursor, params);
        free();
        return o;
    }

    @Override
    public ScanResult<Tuple> zscan(String key, int cursor) {
        ScanResult<Tuple> o = getJedis().zscan(key, cursor);
        free();
        return o;
    }

    @Override
    public ScanResult<Tuple> zscan(String key, int cursor, ScanParams params) {
        ScanResult<Tuple> o = getJedis().zscan(key, cursor, params);
        free();
        return o;
    }

    @Override
    public ScanResult<String> scan(String cursor) {
        ScanResult<String> o = getJedis().scan(cursor);
        free();
        return o;
    }

    @Override
    public ScanResult<String> scan(String cursor, ScanParams params) {
        ScanResult<String> o = getJedis().scan(cursor, params);
        free();
        return o;
    }

    @Override
    public ScanResult<Map.Entry<String, String>> hscan(String key, String cursor) {
        ScanResult<Map.Entry<String, String>> o = getJedis().hscan(key, cursor);
        free();
        return o;
    }

    @Override
    public ScanResult<Map.Entry<String, String>> hscan(String key, String cursor, ScanParams params) {
        ScanResult<Map.Entry<String, String>> o = getJedis().hscan(key, cursor, params);
        free();
        return o;
    }

    @Override
    public ScanResult<String> sscan(String key, String cursor) {
        ScanResult<String> o = getJedis().sscan(key, cursor);
        free();
        return o;
    }

    @Override
    public ScanResult<String> sscan(String key, String cursor, ScanParams params) {
        ScanResult<String> o = getJedis().sscan(key, cursor, params);
        free();
        return o;
    }

    @Override
    public ScanResult<Tuple> zscan(String key, String cursor) {
        ScanResult<Tuple> o = getJedis().zscan(key, cursor);
        free();
        return o;
    }

    @Override
    public ScanResult<Tuple> zscan(String key, String cursor, ScanParams params) {
        ScanResult<Tuple> o = getJedis().zscan(key, cursor, params);
        free();
        return o;
    }

    @Override
    public String clusterNodes() {
        String o = getJedis().clusterNodes();
        free();
        return o;
    }

    @Override
    public String readonly() {
        String o = getJedis().readonly();
        free();
        return o;
    }

    @Override
    public String clusterMeet(String ip, int port) {
        String o = getJedis().clusterMeet(ip, port);
        free();
        return o;
    }

    @Override
    public String clusterReset(JedisCluster.Reset resetType) {
        String o = getJedis().clusterReset(resetType);
        free();
        return o;
    }

    @Override
    public String clusterAddSlots(int... slots) {
        String o = getJedis().clusterAddSlots(slots);
        free();
        return o;
    }

    @Override
    public String clusterDelSlots(int... slots) {
        String o = getJedis().clusterDelSlots(slots);
        free();
        return o;
    }

    @Override
    public String clusterInfo() {
        String o = getJedis().clusterInfo();
        free();
        return o;
    }

    @Override
    public List<String> clusterGetKeysInSlot(int slot, int count) {
        List<String> o = getJedis().clusterGetKeysInSlot(slot, count);
        free();
        return o;
    }

    @Override
    public String clusterSetSlotNode(int slot, String nodeId) {
        String o = getJedis().clusterSetSlotNode(slot, nodeId);
        free();
        return o;
    }

    @Override
    public String clusterSetSlotMigrating(int slot, String nodeId) {
        String o = getJedis().clusterSetSlotMigrating(slot, nodeId);
        free();
        return o;
    }

    @Override
    public String clusterSetSlotImporting(int slot, String nodeId) {
        String o = getJedis().clusterSetSlotImporting(slot, nodeId);
        free();
        return o;
    }

    @Override
    public String clusterSetSlotStable(int slot) {
        String o = getJedis().clusterSetSlotStable(slot);
        free();
        return o;
    }

    @Override
    public String clusterForget(String nodeId) {
        String o = getJedis().clusterForget(nodeId);
        free();
        return o;
    }

    @Override
    public String clusterFlushSlots() {
        String o = getJedis().clusterFlushSlots();
        free();
        return o;
    }

    @Override
    public Long clusterKeySlot(String key) {
        Long o = getJedis().clusterKeySlot(key);
        free();
        return o;
    }

    @Override
    public Long clusterCountKeysInSlot(int slot) {
        Long o = getJedis().clusterCountKeysInSlot(slot);
        free();
        return o;
    }

    @Override
    public String clusterSaveConfig() {
        String o = getJedis().clusterSaveConfig();
        free();
        return o;
    }

    @Override
    public String clusterReplicate(String nodeId) {
        String o = getJedis().clusterReplicate(nodeId);
        free();
        return o;
    }

    @Override
    public List<String> clusterSlaves(String nodeId) {
        List<String> o = getJedis().clusterSlaves(nodeId);
        free();
        return o;
    }

    @Override
    public String clusterFailover() {
        String o = getJedis().clusterFailover();
        free();
        return o;
    }

    @Override
    public List<Object> clusterSlots() {
        List<Object> o = getJedis().clusterSlots();
        free();
        return o;
    }

    @Override
    public String asking() {
        String o = getJedis().asking();
        free();
        return o;
    }

    @Override
    public List<String> pubsubChannels(String pattern) {
        List<String> o = getJedis().pubsubChannels(pattern);
        free();
        return o;
    }

    @Override
    public Long pubsubNumPat() {
        Long o = getJedis().pubsubNumPat();
        free();
        return o;
    }

    @Override
    public Map<String, String> pubsubNumSub(String... channels) {
        Map<String, String> o = getJedis().pubsubNumSub(channels);
        free();
        return o;
    }

    @Override
    public void close() {
        getJedis().close();
        free();
        return;
    }

    @Override
    public void setDataSource(Pool<Jedis> jedisPool) {
        getJedis().setDataSource(jedisPool);
        free();
        return;
    }

    @Override
    public Long pfadd(String key, String... elements) {
        Long o = getJedis().pfadd(key, elements);
        free();
        return o;
    }

    @Override
    public long pfcount(String key) {
        long o = getJedis().pfcount(key);
        free();
        return o;
    }

    @Override
    public long pfcount(String... keys) {
        long o = getJedis().pfcount(keys);
        free();
        return o;
    }

    @Override
    public String pfmerge(String destkey, String... sourcekeys) {
        String o = getJedis().pfmerge(destkey, sourcekeys);
        free();
        return o;
    }

    @Override
    public List<String> blpop(int timeout, String key) {
        List<String> o = getJedis().blpop(timeout, key);
        free();
        return o;
    }

    @Override
    public List<String> brpop(int timeout, String key) {
        List<String> o = getJedis().brpop(timeout, key);
        free();
        return o;
    }

    @Override
    public Long geoadd(String key, double longitude, double latitude, String member) {
        Long o = getJedis().geoadd(key, longitude, latitude, member);
        free();
        return o;
    }

    @Override
    public Long geoadd(String key, Map<String, GeoCoordinate> memberCoordinateMap) {
        Long o = getJedis().geoadd(key, memberCoordinateMap);
        free();
        return o;
    }

    @Override
    public Double geodist(String key, String member1, String member2) {
        Double o = getJedis().geodist(key, member1, member2);
        free();
        return o;
    }

    @Override
    public Double geodist(String key, String member1, String member2, GeoUnit unit) {
        Double o = getJedis().geodist(key, member1, member2, unit);
        free();
        return o;
    }

    @Override
    public List<String> geohash(String key, String... members) {
        List<String> o = getJedis().geohash(key, members);
        free();
        return o;
    }

    @Override
    public List<GeoCoordinate> geopos(String key, String... members) {
        List<GeoCoordinate> o = getJedis().geopos(key, members);
        free();
        return o;
    }

    @Override
    public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit) {
        List<GeoRadiusResponse> o = getJedis().georadius(key, longitude, latitude, radius, unit);
        free();
        return o;
    }

    @Override
    public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
        List<GeoRadiusResponse> o = getJedis().georadius(key, longitude, latitude, radius, unit, param);
        free();
        return o;
    }

    @Override
    public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit) {
        List<GeoRadiusResponse> o = getJedis().georadiusByMember(key, member, radius, unit);
        free();
        return o;
    }

    @Override
    public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit, GeoRadiusParam param) {
        List<GeoRadiusResponse> o = getJedis().georadiusByMember(key, member, radius, unit, param);
        free();
        return o;
    }

    @Override
    public List<Long> bitfield(String key, String... arguments) {
        List<Long> o = getJedis().bitfield(key, arguments);
        free();
        return o;
    }

    public Jedis jedis() {
        return jedis.get();
    }

    public Jedis getJedis() {
        if (jedis.get() != null) {
            return jedis.get();
        } else {
            Jedis j = getResource(this.jedisPool);
            jedis.set(j);
            return jedis.get();
        }
    }

    public synchronized void free() {
        try {
            // this.jedis.flushAll();
            Jedis j = this.jedis.get();
            this.jedis.remove();
            if (j != null) {
                j.close();
            }
            //SystemLogger.info("return jedis to pool.");
        } catch (Exception e) {
            if (this.jedis.get().isConnected()) {
                SystemLogger.error("not free the jedis {}", e);
                e.printStackTrace();
            } else {
                SystemLogger.info("release jedis failed but resource has been free.");
            }
        } finally {
            this.jedis.remove();
        }
    }

    @Override
    public String ping() {
        String o = getJedis().ping();
        free();
        return o;
    }

    @Override
    public String set(byte[] key, byte[] value) {
        String o = getJedis().set(key, value);
        free();
        return o;
    }

    @Override
    public String set(byte[] key, byte[] value, byte[] nxxx, byte[] expx, long time) {
        String o = getJedis().set(key, value, nxxx, expx, time);
        free();
        return o;
    }

    @Override
    public byte[] get(byte[] key) {
        byte[] o = getJedis().get(key);
        free();
        return o;
    }

    @Override
    public String quit() {
        String o = getJedis().quit();
        free();
        return o;
    }

    @Override
    public Long exists(byte[]... keys) {
        Long o = getJedis().exists(keys);
        free();
        return o;
    }

    @Override
    public Boolean exists(byte[] key) {
        Boolean o = getJedis().exists(key);
        free();
        return o;
    }

    @Override
    public Long del(byte[]... keys) {
        Long o = getJedis().del(keys);
        free();
        return o;
    }

    @Override
    public Long del(byte[] key) {
        Long o = getJedis().del(key);
        free();
        return o;
    }

    @Override
    public String type(byte[] key) {
        String o = getJedis().type(key);
        free();
        return o;
    }

    @Override
    public String flushDB() {
        String o = getJedis().flushDB();
        free();
        return o;
    }

    @Override
    public Set<byte[]> keys(byte[] pattern) {
        Set<byte[]> o = getJedis().keys(pattern);
        free();
        return o;
    }

    @Override
    public byte[] randomBinaryKey() {
        byte[] o = getJedis().randomBinaryKey();
        free();
        return o;
    }

    @Override
    public String rename(byte[] oldkey, byte[] newkey) {
        String o = getJedis().rename(oldkey, newkey);
        free();
        return o;
    }

    @Override
    public Long renamenx(byte[] oldkey, byte[] newkey) {
        Long o = getJedis().renamenx(oldkey, newkey);
        free();
        return o;
    }

    @Override
    public Long dbSize() {
        Long o = getJedis().dbSize();
        free();
        return o;
    }

    @Override
    public Long expire(byte[] key, int seconds) {
        Long o = getJedis().expire(key, seconds);
        free();
        return o;
    }

    @Override
    public Long expireAt(byte[] key, long unixTime) {
        Long o = getJedis().expireAt(key, unixTime);
        free();
        return o;
    }

    @Override
    public Long ttl(byte[] key) {
        Long o = getJedis().ttl(key);
        free();
        return o;
    }

    @Override
    public String select(int index) {
        String o = getJedis().select(index);
        free();
        return o;
    }

    @Override
    public Long move(byte[] key, int dbIndex) {
        Long o = getJedis().move(key, dbIndex);
        free();
        return o;
    }

    @Override
    public String flushAll() {
        String o = getJedis().flushAll();
        free();
        return o;
    }

    @Override
    public byte[] getSet(byte[] key, byte[] value) {
        byte[] o = getJedis().getSet(key, value);
        free();
        return o;
    }

    @Override
    public List<byte[]> mget(byte[]... keys) {
        List<byte[]> o = getJedis().mget(keys);
        free();
        return o;
    }

    @Override
    public Long setnx(byte[] key, byte[] value) {
        Long o = getJedis().setnx(key, value);
        free();
        return o;
    }

    @Override
    public String setex(byte[] key, int seconds, byte[] value) {
        String o = getJedis().setex(key, seconds, value);
        free();
        return o;
    }

    @Override
    public String mset(byte[]... keysvalues) {
        String o = getJedis().mset(keysvalues);
        free();
        return o;
    }

    @Override
    public Long msetnx(byte[]... keysvalues) {
        Long o = getJedis().msetnx(keysvalues);
        free();
        return o;
    }

    @Override
    public Long decrBy(byte[] key, long integer) {
        Long o = getJedis().decrBy(key, integer);
        free();
        return o;
    }

    @Override
    public Long decr(byte[] key) {
        Long o = getJedis().decr(key);
        free();
        return o;
    }

    @Override
    public Long incrBy(byte[] key, long integer) {
        Long o = getJedis().incrBy(key, integer);
        free();
        return o;
    }

    @Override
    public Double incrByFloat(byte[] key, double integer) {
        Double o = getJedis().incrByFloat(key, integer);
        free();
        return o;
    }

    @Override
    public Long incr(byte[] key) {
        Long o = getJedis().incr(key);
        free();
        return o;
    }

    @Override
    public Long append(byte[] key, byte[] value) {
        Long o = getJedis().append(key, value);
        free();
        return o;
    }

    @Override
    public byte[] substr(byte[] key, int start, int end) {
        byte[] o = getJedis().substr(key, start, end);
        free();
        return o;
    }

    @Override
    public Long hset(byte[] key, byte[] field, byte[] value) {
        Long o = getJedis().hset(key, field, value);
        free();
        return o;
    }

    @Override
    public byte[] hget(byte[] key, byte[] field) {
        byte[] o = getJedis().hget(key, field);
        free();
        return o;
    }

    @Override
    public Long hsetnx(byte[] key, byte[] field, byte[] value) {
        Long o = getJedis().hsetnx(key, field, value);
        free();
        return o;
    }

    @Override
    public String hmset(byte[] key, Map<byte[], byte[]> hash) {
        String o = getJedis().hmset(key, hash);
        free();
        return o;
    }

    @Override
    public List<byte[]> hmget(byte[] key, byte[]... fields) {
        List<byte[]> o = getJedis().hmget(key, fields);
        free();
        return o;
    }

    @Override
    public Long hincrBy(byte[] key, byte[] field, long value) {
        Long o = getJedis().hincrBy(key, field, value);
        free();
        return o;
    }

    @Override
    public Double hincrByFloat(byte[] key, byte[] field, double value) {
        Double o = getJedis().hincrByFloat(key, field, value);
        free();
        return o;
    }

    @Override
    public Boolean hexists(byte[] key, byte[] field) {
        Boolean o = getJedis().hexists(key, field);
        free();
        return o;
    }

    @Override
    public Long hdel(byte[] key, byte[]... fields) {
        Long o = getJedis().hdel(key, fields);
        free();
        return o;
    }

    @Override
    public Long hlen(byte[] key) {
        Long o = getJedis().hlen(key);
        free();
        return o;
    }

    @Override
    public Set<byte[]> hkeys(byte[] key) {
        Set<byte[]> o = getJedis().hkeys(key);
        free();
        return o;
    }

    @Override
    public List<byte[]> hvals(byte[] key) {
        List<byte[]> o = getJedis().hvals(key);
        free();
        return o;
    }

    @Override
    public Map<byte[], byte[]> hgetAll(byte[] key) {
        Map<byte[], byte[]> o = getJedis().hgetAll(key);
        free();
        return o;
    }

    @Override
    public Long rpush(byte[] key, byte[]... strings) {
        Long o = getJedis().rpush(key, strings);
        free();
        return o;
    }

    @Override
    public Long lpush(byte[] key, byte[]... strings) {
        Long o = getJedis().lpush(key, strings);
        free();
        return o;
    }

    @Override
    public Long llen(byte[] key) {
        Long o = getJedis().llen(key);
        free();
        return o;
    }

    @Override
    public List<byte[]> lrange(byte[] key, long start, long end) {
        List<byte[]> o = getJedis().lrange(key, start, end);
        free();
        return o;
    }

    @Override
    public String ltrim(byte[] key, long start, long end) {
        String o = getJedis().ltrim(key, start, end);
        free();
        return o;
    }

    @Override
    public byte[] lindex(byte[] key, long index) {
        byte[] o = getJedis().lindex(key, index);
        free();
        return o;
    }

    @Override
    public String lset(byte[] key, long index, byte[] value) {
        String o = getJedis().lset(key, index, value);
        free();
        return o;
    }

    @Override
    public Long lrem(byte[] key, long count, byte[] value) {
        Long o = getJedis().lrem(key, count, value);
        free();
        return o;
    }

    @Override
    public byte[] lpop(byte[] key) {
        byte[] o = getJedis().lpop(key);
        free();
        return o;
    }

    @Override
    public byte[] rpop(byte[] key) {
        byte[] o = getJedis().rpop(key);
        free();
        return o;
    }

    @Override
    public byte[] rpoplpush(byte[] srckey, byte[] dstkey) {
        byte[] o = getJedis().rpoplpush(srckey, dstkey);
        free();
        return o;
    }

    @Override
    public Long sadd(byte[] key, byte[]... members) {
        Long o = getJedis().sadd(key, members);
        free();
        return o;
    }

    @Override
    public Set<byte[]> smembers(byte[] key) {
        Set<byte[]> o = getJedis().smembers(key);
        free();
        return o;
    }

    @Override
    public Long srem(byte[] key, byte[]... member) {
        Long o = getJedis().srem(key, member);
        free();
        return o;
    }

    @Override
    public byte[] spop(byte[] key) {
        byte[] o = getJedis().spop(key);
        free();
        return o;
    }

    @Override
    public Set<byte[]> spop(byte[] key, long count) {
        Set<byte[]> o = getJedis().spop(key, count);
        free();
        return o;
    }

    @Override
    public Long smove(byte[] srckey, byte[] dstkey, byte[] member) {
        Long o = getJedis().smove(srckey, dstkey, member);
        free();
        return o;
    }

    @Override
    public Long scard(byte[] key) {
        Long o = getJedis().scard(key);
        free();
        return o;
    }

    @Override
    public Boolean sismember(byte[] key, byte[] member) {
        Boolean o = getJedis().sismember(key, member);
        free();
        return o;
    }

    @Override
    public Set<byte[]> sinter(byte[]... keys) {
        Set<byte[]> o = getJedis().sinter(keys);
        free();
        return o;
    }

    @Override
    public Long sinterstore(byte[] dstkey, byte[]... keys) {
        Long o = getJedis().sinterstore(dstkey, keys);
        free();
        return o;
    }

    @Override
    public Set<byte[]> sunion(byte[]... keys) {
        Set<byte[]> o = getJedis().sunion(keys);
        free();
        return o;
    }

    @Override
    public Long sunionstore(byte[] dstkey, byte[]... keys) {
        Long o = getJedis().sunionstore(dstkey, keys);
        free();
        return o;
    }

    @Override
    public Set<byte[]> sdiff(byte[]... keys) {
        Set<byte[]> o = getJedis().sdiff(keys);
        free();
        return o;
    }

    @Override
    public Long sdiffstore(byte[] dstkey, byte[]... keys) {
        Long o = getJedis().sdiffstore(dstkey, keys);
        free();
        return o;
    }

    @Override
    public byte[] srandmember(byte[] key) {
        byte[] o = getJedis().srandmember(key);
        free();
        return o;
    }

    @Override
    public List<byte[]> srandmember(byte[] key, int count) {
        List<byte[]> o = getJedis().srandmember(key, count);
        free();
        return o;
    }

    @Override
    public Long zadd(byte[] key, double score, byte[] member) {
        Long o = getJedis().zadd(key, score, member);
        free();
        return o;
    }

    @Override
    public Long zadd(byte[] key, double score, byte[] member, ZAddParams params) {
        Long o = getJedis().zadd(key, score, member, params);
        free();
        return o;
    }

    @Override
    public Long zadd(byte[] key, Map<byte[], Double> scoreMembers) {
        Long o = getJedis().zadd(key, scoreMembers);
        free();
        return o;
    }

    @Override
    public Long zadd(byte[] key, Map<byte[], Double> scoreMembers, ZAddParams params) {
        Long o = getJedis().zadd(key, scoreMembers, params);
        free();
        return o;
    }

    @Override
    public Set<byte[]> zrange(byte[] key, long start, long end) {
        Set<byte[]> o = getJedis().zrange(key, start, end);
        free();
        return o;
    }

    @Override
    public Long zrem(byte[] key, byte[]... members) {
        Long o = getJedis().zrem(key, members);
        free();
        return o;
    }

    @Override
    public Double zincrby(byte[] key, double score, byte[] member) {
        Double o = getJedis().zincrby(key, score, member);
        free();
        return o;
    }

    @Override
    public Double zincrby(byte[] key, double score, byte[] member, ZIncrByParams params) {
        Double o = getJedis().zincrby(key, score, member, params);
        free();
        return o;
    }

    @Override
    public Long zrank(byte[] key, byte[] member) {
        Long o = getJedis().zrank(key, member);
        free();
        return o;
    }

    @Override
    public Long zrevrank(byte[] key, byte[] member) {
        Long o = getJedis().zrevrank(key, member);
        free();
        return o;
    }

    @Override
    public Set<byte[]> zrevrange(byte[] key, long start, long end) {
        Set<byte[]> o = getJedis().zrevrange(key, start, end);
        free();
        return o;
    }

    @Override
    public Set<Tuple> zrangeWithScores(byte[] key, long start, long end) {
        Set<Tuple> o = getJedis().zrangeWithScores(key, start, end);
        free();
        return o;
    }

    @Override
    public Set<Tuple> zrevrangeWithScores(byte[] key, long start, long end) {
        Set<Tuple> o = getJedis().zrevrangeWithScores(key, start, end);
        free();
        return o;
    }

    @Override
    public Long zcard(byte[] key) {
        Long o = getJedis().zcard(key);
        free();
        return o;
    }

    @Override
    public Double zscore(byte[] key, byte[] member) {
        Double o = getJedis().zscore(key, member);
        free();
        return o;
    }

    @Override
    public Transaction multi() {
        Transaction o = getJedis().multi();
        free();
        return o;
    }

    @Override
    public List<Object> multi(TransactionBlock jedisTransaction) {
        List<Object> o = getJedis().multi(jedisTransaction);
        free();
        return o;
    }

    @Override
    public void connect() {
        getJedis().connect();
        free();
        return;
    }

    @Override
    public void disconnect() {
        getJedis().disconnect();
        free();
        return;
    }

    @Override
    public void resetState() {
        getJedis().resetState();
        free();
        return;
    }

    @Override
    public String watch(byte[]... keys) {
        String o = getJedis().watch(keys);
        free();
        return o;
    }

    @Override
    public String unwatch() {
        String o = getJedis().unwatch();
        free();
        return o;
    }

    @Override
    public List<byte[]> sort(byte[] key) {
        List<byte[]> o = getJedis().sort(key);
        free();
        return o;
    }

    @Override
    public List<byte[]> sort(byte[] key, SortingParams sortingParameters) {
        List<byte[]> o = getJedis().sort(key, sortingParameters);
        free();
        return o;
    }

    @Override
    public List<byte[]> blpop(int timeout, byte[]... keys) {
        List<byte[]> o = getJedis().blpop(timeout, keys);
        free();
        return o;
    }

    @Override
    public Long sort(byte[] key, SortingParams sortingParameters, byte[] dstkey) {
        Long o = getJedis().sort(key, sortingParameters, dstkey);
        free();
        return o;
    }

    @Override
    public Long sort(byte[] key, byte[] dstkey) {
        Long o = getJedis().sort(key, dstkey);
        free();
        return o;
    }

    @Override
    public List<byte[]> brpop(int timeout, byte[]... keys) {
        List<byte[]> o = getJedis().brpop(timeout, keys);
        free();
        return o;
    }

    @Override
    public List<byte[]> blpop(byte[] arg) {
        List<byte[]> o = getJedis().blpop(arg);
        free();
        return o;
    }

    @Override
    public List<byte[]> brpop(byte[] arg) {
        List<byte[]> o = getJedis().brpop(arg);
        free();
        return o;
    }

    @Override
    public List<byte[]> blpop(byte[]... args) {
        List<byte[]> o = getJedis().blpop(args);
        free();
        return o;
    }

    @Override
    public List<byte[]> brpop(byte[]... args) {
        List<byte[]> o = getJedis().brpop(args);
        free();
        return o;
    }

    @Override
    public String auth(String password) {
        String o = getJedis().auth(password);
        free();
        return o;
    }

    @Override
    public List<Object> pipelined(PipelineBlock jedisPipeline) {
        List<Object> o = getJedis().pipelined(jedisPipeline);
        free();
        return o;
    }

    @Override
    public Pipeline pipelined() {
        Pipeline o = getJedis().pipelined();
        free();
        return o;
    }

    @Override
    public Long zcount(byte[] key, double min, double max) {
        Long o = getJedis().zcount(key, min, max);
        free();
        return o;
    }

    @Override
    public Long zcount(byte[] key, byte[] min, byte[] max) {
        Long o = getJedis().zcount(key, min, max);
        free();
        return o;
    }

    @Override
    public Set<byte[]> zrangeByScore(byte[] key, double min, double max) {
        Set<byte[]> o = getJedis().zrangeByScore(key, min, max);
        free();
        return o;
    }

    @Override
    public Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max) {
        Set<byte[]> o = getJedis().zrangeByScore(key, min, max);
        free();
        return o;
    }

    @Override
    public Set<byte[]> zrangeByScore(byte[] key, double min, double max, int offset, int count) {
        Set<byte[]> o = getJedis().zrangeByScore(key, min, max, offset, count);
        free();
        return o;
    }

    @Override
    public Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max, int offset, int count) {
        Set<byte[]> o = getJedis().zrangeByScore(key, min, max, offset, count);
        free();
        return o;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max) {
        Set<Tuple> o = getJedis().zrangeByScoreWithScores(key, min, max);
        free();
        return o;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max) {
        Set<Tuple> o = getJedis().zrangeByScoreWithScores(key, min, max);
        free();
        return o;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max, int offset, int count) {
        Set<Tuple> o = getJedis().zrangeByScoreWithScores(key, min, max, offset, count);
        free();
        return o;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max, int offset, int count) {
        Set<Tuple> o = getJedis().zrangeByScoreWithScores(key, min, max, offset, count);
        free();
        return o;
    }

    @Override
    public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min) {
        Set<byte[]> o = getJedis().zrevrangeByScore(key, max, min);
        free();
        return o;
    }

    @Override
    public Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min) {
        Set<byte[]> o = getJedis().zrevrangeByScore(key, max, min);
        free();
        return o;
    }

    @Override
    public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min, int offset, int count) {
        Set<byte[]> o = getJedis().zrevrangeByScore(key, max, min, offset, count);
        free();
        return o;
    }

    @Override
    public Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min, int offset, int count) {
        Set<byte[]> o = getJedis().zrevrangeByScore(key, max, min, offset, count);
        free();
        return o;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min) {
        Set<Tuple> o = getJedis().zrevrangeByScoreWithScores(key, max, min);
        free();
        return o;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min, int offset, int count) {
        Set<Tuple> o = getJedis().zrevrangeByScoreWithScores(key, max, min, offset, count);
        free();
        return o;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min) {
        Set<Tuple> o = getJedis().zrevrangeByScoreWithScores(key, max, min);
        free();
        return o;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min, int offset, int count) {
        Set<Tuple> o = getJedis().zrevrangeByScoreWithScores(key, max, min, offset, count);
        free();
        return o;
    }

    @Override
    public Long zremrangeByRank(byte[] key, long start, long end) {
        Long o = getJedis().zremrangeByRank(key, start, end);
        free();
        return o;
    }

    @Override
    public Long zremrangeByScore(byte[] key, double start, double end) {
        Long o = getJedis().zremrangeByScore(key, start, end);
        free();
        return o;
    }

    @Override
    public Long zremrangeByScore(byte[] key, byte[] start, byte[] end) {
        Long o = getJedis().zremrangeByScore(key, start, end);
        free();
        return o;
    }

    @Override
    public Long zunionstore(byte[] dstkey, byte[]... sets) {
        Long o = getJedis().zunionstore(dstkey, sets);
        free();
        return o;
    }

    @Override
    public Long zunionstore(byte[] dstkey, ZParams params, byte[]... sets) {
        Long o = getJedis().zunionstore(dstkey, params, sets);
        free();
        return o;
    }

    @Override
    public Long zinterstore(byte[] dstkey, byte[]... sets) {
        Long o = getJedis().zinterstore(dstkey, sets);
        free();
        return o;
    }

    @Override
    public Long zinterstore(byte[] dstkey, ZParams params, byte[]... sets) {
        Long o = getJedis().zinterstore(dstkey, params, sets);
        free();
        return o;
    }

    @Override
    public Long zlexcount(byte[] key, byte[] min, byte[] max) {
        Long o = getJedis().zlexcount(key, min, max);
        free();
        return o;
    }

    @Override
    public Set<byte[]> zrangeByLex(byte[] key, byte[] min, byte[] max) {
        Set<byte[]> o = getJedis().zrangeByLex(key, min, max);
        free();
        return o;
    }

    @Override
    public Set<byte[]> zrangeByLex(byte[] key, byte[] min, byte[] max, int offset, int count) {
        Set<byte[]> o = getJedis().zrangeByLex(key, min, max, offset, count);
        free();
        return o;
    }

    @Override
    public Set<byte[]> zrevrangeByLex(byte[] key, byte[] max, byte[] min) {
        Set<byte[]> o = getJedis().zrevrangeByLex(key, max, min);
        free();
        return o;
    }

    @Override
    public Set<byte[]> zrevrangeByLex(byte[] key, byte[] max, byte[] min, int offset, int count) {
        Set<byte[]> o = getJedis().zrevrangeByLex(key, max, min, offset, count);
        free();
        return o;
    }

    @Override
    public Long zremrangeByLex(byte[] key, byte[] min, byte[] max) {
        Long o = getJedis().zremrangeByLex(key, min, max);
        free();
        return o;
    }

    @Override
    public String save() {
        String o = getJedis().save();
        free();
        return o;
    }

    @Override
    public String bgsave() {
        String o = getJedis().bgsave();
        free();
        return o;
    }

    @Override
    public String bgrewriteaof() {
        String o = getJedis().bgrewriteaof();
        free();
        return o;
    }

    @Override
    public Long lastsave() {
        Long o = getJedis().lastsave();
        free();
        return o;
    }

    @Override
    public String shutdown() {
        String o = getJedis().shutdown();
        free();
        return o;
    }

    @Override
    public String info() {
        String o = getJedis().info();
        free();
        return o;
    }

    @Override
    public String info(String section) {
        String o = getJedis().info(section);
        free();
        return o;
    }

    @Override
    public void monitor(JedisMonitor jedisMonitor) {
        getJedis().monitor(jedisMonitor);
        free();
        return;
    }

    @Override
    public String slaveof(String host, int port) {
        String o = getJedis().slaveof(host, port);
        free();
        return o;
    }

    @Override
    public String slaveofNoOne() {
        String o = getJedis().slaveofNoOne();
        free();
        return o;
    }

    @Override
    public List<byte[]> configGet(byte[] pattern) {
        List<byte[]> o = getJedis().configGet(pattern);
        free();
        return o;
    }

    @Override
    public String configResetStat() {
        String o = getJedis().configResetStat();
        free();
        return o;
    }

    @Override
    public byte[] configSet(byte[] parameter, byte[] value) {
        byte[] o = getJedis().configSet(parameter, value);
        free();
        return o;
    }

    @Override
    public boolean isConnected() {
        boolean o = getJedis().isConnected();
        free();
        return o;
    }

    @Override
    public Long strlen(byte[] key) {
        Long o = getJedis().strlen(key);
        free();
        return o;
    }

    @Override
    public void sync() {
        getJedis().sync();
        free();
        return;
    }

    @Override
    public Long lpushx(byte[] key, byte[]... string) {
        Long o = getJedis().lpushx(key, string);
        free();
        return o;
    }

    @Override
    public Long persist(byte[] key) {
        Long o = getJedis().persist(key);
        free();
        return o;
    }

    @Override
    public Long rpushx(byte[] key, byte[]... string) {
        Long o = getJedis().rpushx(key, string);
        free();
        return o;
    }

    @Override
    public byte[] echo(byte[] string) {
        byte[] o = getJedis().echo(string);
        free();
        return o;
    }

    @Override
    public Long linsert(byte[] key, BinaryClient.LIST_POSITION where, byte[] pivot, byte[] value) {
        Long o = getJedis().linsert(key, where, pivot, value);
        free();
        return o;
    }

    @Override
    public String debug(DebugParams params) {
        String o = getJedis().debug(params);
        free();
        return o;
    }

    @Override
    public Client getClient() {
        Client o = getJedis().getClient();
        free();
        return o;
    }

    @Override
    public byte[] brpoplpush(byte[] source, byte[] destination, int timeout) {
        byte[] o = getJedis().brpoplpush(source, destination, timeout);
        free();
        return o;
    }

    @Override
    public Boolean setbit(byte[] key, long offset, boolean value) {
        Boolean o = getJedis().setbit(key, offset, value);
        free();
        return o;
    }

    @Override
    public Boolean setbit(byte[] key, long offset, byte[] value) {
        Boolean o = getJedis().setbit(key, offset, value);
        free();
        return o;
    }

    @Override
    public Boolean getbit(byte[] key, long offset) {
        Boolean o = getJedis().getbit(key, offset);
        free();
        return o;
    }

    @Override
    public Long bitpos(byte[] key, boolean value) {
        Long o = getJedis().bitpos(key, value);
        free();
        return o;
    }

    @Override
    public Long bitpos(byte[] key, boolean value, BitPosParams params) {
        Long o = getJedis().bitpos(key, value, params);
        free();
        return o;
    }

    @Override
    public Long setrange(byte[] key, long offset, byte[] value) {
        Long o = getJedis().setrange(key, offset, value);
        free();
        return o;
    }

    @Override
    public byte[] getrange(byte[] key, long startOffset, long endOffset) {
        byte[] o = getJedis().getrange(key, startOffset, endOffset);
        free();
        return o;
    }

    @Override
    public Long publish(byte[] channel, byte[] message) {
        Long o = getJedis().publish(channel, message);
        free();
        return o;
    }

    @Override
    public void subscribe(BinaryJedisPubSub jedisPubSub, byte[]... channels) {
        getJedis().subscribe(jedisPubSub, channels);
        free();
        return;
    }

    @Override
    public void psubscribe(BinaryJedisPubSub jedisPubSub, byte[]... patterns) {
        getJedis().psubscribe(jedisPubSub, patterns);
        free();
        return;
    }

    @Override
    public Long getDB() {
        Long o = getJedis().getDB();
        free();
        return o;
    }

    @Override
    public Object eval(byte[] script, List<byte[]> keys, List<byte[]> args) {
        Object o = getJedis().eval(script, keys, args);
        free();
        return o;
    }

    @Override
    public Object eval(byte[] script, byte[] keyCount, byte[]... params) {
        Object o = getJedis().eval(script, keyCount, params);
        free();
        return o;
    }

    @Override
    public Object eval(byte[] script, int keyCount, byte[]... params) {
        Object o = getJedis().eval(script, keyCount, params);
        free();
        return o;
    }

    @Override
    public Object eval(byte[] script) {
        Object o = getJedis().eval(script);
        free();
        return o;
    }

    @Override
    public Object evalsha(byte[] sha1) {
        Object o = getJedis().evalsha(sha1);
        free();
        return o;
    }

    @Override
    public Object evalsha(byte[] sha1, List<byte[]> keys, List<byte[]> args) {
        Object o = getJedis().evalsha(sha1, keys, args);
        free();
        return o;
    }

    @Override
    public Object evalsha(byte[] sha1, int keyCount, byte[]... params) {
        Object o = getJedis().evalsha(sha1, keyCount, params);
        free();
        return o;
    }

    @Override
    public String scriptFlush() {
        String o = getJedis().scriptFlush();
        free();
        return o;
    }

    @Override
    public Long scriptExists(byte[] sha1) {
        Long o = getJedis().scriptExists(sha1);
        free();
        return o;
    }

    @Override
    public List<Long> scriptExists(byte[]... sha1) {
        List<Long> o = getJedis().scriptExists(sha1);
        free();
        return o;
    }

    @Override
    public byte[] scriptLoad(byte[] script) {
        byte[] o = getJedis().scriptLoad(script);
        free();
        return o;
    }

    @Override
    public String scriptKill() {
        String o = getJedis().scriptKill();
        free();
        return o;
    }

    @Override
    public String slowlogReset() {
        String o = getJedis().slowlogReset();
        free();
        return o;
    }

    @Override
    public Long slowlogLen() {
        Long o = getJedis().slowlogLen();
        free();
        return o;
    }

    @Override
    public List<byte[]> slowlogGetBinary() {
        List<byte[]> o = getJedis().slowlogGetBinary();
        free();
        return o;
    }

    @Override
    public List<byte[]> slowlogGetBinary(long entries) {
        List<byte[]> o = getJedis().slowlogGetBinary(entries);
        free();
        return o;
    }

    @Override
    public Long objectRefcount(byte[] key) {
        Long o = getJedis().objectRefcount(key);
        free();
        return o;
    }

    @Override
    public byte[] objectEncoding(byte[] key) {
        byte[] o = getJedis().objectEncoding(key);
        free();
        return o;
    }

    @Override
    public Long objectIdletime(byte[] key) {
        Long o = getJedis().objectIdletime(key);
        free();
        return o;
    }

    @Override
    public Long bitcount(byte[] key) {
        Long o = getJedis().bitcount(key);
        free();
        return o;
    }

    @Override
    public Long bitcount(byte[] key, long start, long end) {
        Long o = getJedis().bitcount(key, start, end);
        free();
        return o;
    }

    @Override
    public Long bitop(BitOP op, byte[] destKey, byte[]... srcKeys) {
        Long o = getJedis().bitop(op, destKey, srcKeys);
        free();
        return o;
    }

    @Override
    public byte[] dump(byte[] key) {
        byte[] o = getJedis().dump(key);
        free();
        return o;
    }

    @Override
    public String restore(byte[] key, int ttl, byte[] serializedValue) {
        String o = getJedis().restore(key, ttl, serializedValue);
        free();
        return o;
    }

    @Override
    public Long pexpire(byte[] key, int milliseconds) {
        Long o = getJedis().pexpire(key, milliseconds);
        free();
        return o;
    }

    @Override
    public Long pexpire(byte[] key, long milliseconds) {
        Long o = getJedis().pexpire(key, milliseconds);
        free();
        return o;
    }

    @Override
    public Long pexpireAt(byte[] key, long millisecondsTimestamp) {
        Long o = getJedis().pexpireAt(key, millisecondsTimestamp);
        free();
        return o;
    }

    @Override
    public Long pttl(byte[] key) {
        Long o = getJedis().pttl(key);
        free();
        return o;
    }

    @Override
    public String psetex(byte[] key, int milliseconds, byte[] value) {
        String o = getJedis().psetex(key, milliseconds, value);
        free();
        return o;
    }

    @Override
    public String psetex(byte[] key, long milliseconds, byte[] value) {
        String o = getJedis().psetex(key, milliseconds, value);
        free();
        return o;
    }

    @Override
    public String set(byte[] key, byte[] value, byte[] nxxx) {
        String o = getJedis().set(key, value, nxxx);
        free();
        return o;
    }

    @Override
    public String set(byte[] key, byte[] value, byte[] nxxx, byte[] expx, int time) {
        String o = getJedis().set(key, value, nxxx, expx, time);
        free();
        return o;
    }

    @Override
    public String clientKill(byte[] client) {
        String o = getJedis().clientKill(client);
        free();
        return o;
    }

    @Override
    public String clientGetname() {
        String o = getJedis().clientGetname();
        free();
        return o;
    }

    @Override
    public String clientList() {
        String o = getJedis().clientList();
        free();
        return o;
    }

    @Override
    public String clientSetname(byte[] name) {
        String o = getJedis().clientSetname(name);
        free();
        return o;
    }

    @Override
    public List<String> time() {
        List<String> o = getJedis().time();
        free();
        return o;
    }

    @Override
    public String migrate(byte[] host, int port, byte[] key, int destinationDb, int timeout) {
        String o = getJedis().migrate(host, port, key, destinationDb, timeout);
        free();
        return o;
    }

    @Override
    public Long waitReplicas(int replicas, long timeout) {
        Long o = getJedis().waitReplicas(replicas, timeout);
        free();
        return o;
    }

    @Override
    public Long pfadd(byte[] key, byte[]... elements) {
        Long o = getJedis().pfadd(key, elements);
        free();
        return o;
    }

    @Override
    public long pfcount(byte[] key) {
        long o = getJedis().pfcount(key);
        free();
        return o;
    }

    @Override
    public String pfmerge(byte[] destkey, byte[]... sourcekeys) {
        String o = getJedis().pfmerge(destkey, sourcekeys);
        free();
        return o;
    }

    @Override
    public Long pfcount(byte[]... keys) {
        Long o = getJedis().pfcount(keys);
        free();
        return o;
    }

    @Override
    public ScanResult<byte[]> scan(byte[] cursor) {
        ScanResult<byte[]> o = getJedis().scan(cursor);
        free();
        return o;
    }

    @Override
    public ScanResult<byte[]> scan(byte[] cursor, ScanParams params) {
        ScanResult<byte[]> o = getJedis().scan(cursor, params);
        free();
        return o;
    }

    @Override
    public ScanResult<Map.Entry<byte[], byte[]>> hscan(byte[] key, byte[] cursor) {
        ScanResult<Map.Entry<byte[], byte[]>> o = getJedis().hscan(key, cursor);
        free();
        return o;
    }

    @Override
    public ScanResult<Map.Entry<byte[], byte[]>> hscan(byte[] key, byte[] cursor, ScanParams params) {
        ScanResult<Map.Entry<byte[], byte[]>> o = getJedis().hscan(key, cursor, params);
        free();
        return o;
    }

    @Override
    public ScanResult<byte[]> sscan(byte[] key, byte[] cursor) {
        ScanResult<byte[]> o = getJedis().sscan(key, cursor);
        free();
        return o;
    }

    @Override
    public ScanResult<byte[]> sscan(byte[] key, byte[] cursor, ScanParams params) {
        ScanResult<byte[]> o = getJedis().sscan(key, cursor, params);
        free();
        return o;
    }

    @Override
    public ScanResult<Tuple> zscan(byte[] key, byte[] cursor) {
        ScanResult<Tuple> o = getJedis().zscan(key, cursor);
        free();
        return o;
    }

    @Override
    public ScanResult<Tuple> zscan(byte[] key, byte[] cursor, ScanParams params) {
        ScanResult<Tuple> o = getJedis().zscan(key, cursor, params);
        free();
        return o;
    }

    @Override
    public Long geoadd(byte[] key, double longitude, double latitude, byte[] member) {
        Long o = getJedis().geoadd(key, longitude, latitude, member);
        free();
        return o;
    }

    @Override
    public Long geoadd(byte[] key, Map<byte[], GeoCoordinate> memberCoordinateMap) {
        Long o = getJedis().geoadd(key, memberCoordinateMap);
        free();
        return o;
    }

    @Override
    public Double geodist(byte[] key, byte[] member1, byte[] member2) {
        Double o = getJedis().geodist(key, member1, member2);
        free();
        return o;
    }

    @Override
    public Double geodist(byte[] key, byte[] member1, byte[] member2, GeoUnit unit) {
        Double o = getJedis().geodist(key, member1, member2, unit);
        free();
        return o;
    }

    @Override
    public List<byte[]> geohash(byte[] key, byte[]... members) {
        List<byte[]> o = getJedis().geohash(key, members);
        free();
        return o;
    }

    @Override
    public List<GeoCoordinate> geopos(byte[] key, byte[]... members) {
        List<GeoCoordinate> o = getJedis().geopos(key, members);
        free();
        return o;
    }

    @Override
    public List<GeoRadiusResponse> georadius(byte[] key, double longitude, double latitude, double radius, GeoUnit unit) {
        List<GeoRadiusResponse> o = getJedis().georadius(key, longitude, latitude, radius, unit);
        free();
        return o;
    }

    @Override
    public List<GeoRadiusResponse> georadius(byte[] key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
        List<GeoRadiusResponse> o = getJedis().georadius(key, longitude, latitude, radius, unit, param);
        free();
        return o;
    }

    @Override
    public List<GeoRadiusResponse> georadiusByMember(byte[] key, byte[] member, double radius, GeoUnit unit) {
        List<GeoRadiusResponse> o = getJedis().georadiusByMember(key, member, radius, unit);
        free();
        return o;
    }

    @Override
    public List<GeoRadiusResponse> georadiusByMember(byte[] key, byte[] member, double radius, GeoUnit unit, GeoRadiusParam param) {
        List<GeoRadiusResponse> o = getJedis().georadiusByMember(key, member, radius, unit, param);
        free();
        return o;
    }

    @Override
    public List<byte[]> bitfield(byte[] key, byte[]... arguments) {
        List<byte[]> o = getJedis().bitfield(key, arguments);
        free();
        return o;
    }
}
