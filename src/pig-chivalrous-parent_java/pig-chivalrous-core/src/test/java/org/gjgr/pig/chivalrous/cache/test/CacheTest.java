package org.gjgr.pig.chivalrous.cache.test;

import org.gjgr.pig.chivalrous.core.cache.Cache;
import org.gjgr.pig.chivalrous.core.cache.impl.FIFOCache;
import org.gjgr.pig.chivalrous.core.cache.impl.LFUCache;
import org.gjgr.pig.chivalrous.core.cache.impl.LRUCache;
import org.gjgr.pig.chivalrous.core.cache.impl.TimedCache;
import org.gjgr.pig.chivalrous.core.date.DateUnit;
import org.gjgr.pig.chivalrous.core.util.ThreadUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * 缓存测试用例
 *
 * @author Looly
 */
public class CacheTest {

    @Test
    public void fifoCacheTest() {
        Cache<String, String> fifoCache = new FIFOCache<String, String>(3, 0);
        fifoCache.put("key1", "value1", DateUnit.SECOND.getMillis() * 3);
        fifoCache.put("key2", "value2", DateUnit.SECOND.getMillis() * 3);
        fifoCache.put("key3", "value3", DateUnit.SECOND.getMillis() * 3);
        fifoCache.put("key4", "value4", DateUnit.SECOND.getMillis() * 3);

        // 由于缓存容量只有3，当加入第四个元素的时候，根据FIFO规则，最先放入的对象将被移除
        String value1 = fifoCache.get("key1");
        Assert.assertTrue(null == value1);
    }

    @Test
    public void lfuCacheTest() {
        LFUCache<String, String> lfuCache = new LFUCache<String, String>(3);
        lfuCache.put("key1", "value1", DateUnit.SECOND.getMillis() * 3);
        lfuCache.get("key1");// 使用次数+1
        lfuCache.put("key2", "value2", DateUnit.SECOND.getMillis() * 3);
        lfuCache.put("key3", "value3", DateUnit.SECOND.getMillis() * 3);
        lfuCache.put("key4", "value4", DateUnit.SECOND.getMillis() * 3);

        // 由于缓存容量只有3，当加入第四个元素的时候，根据LRU规则，最少使用的将被移除（2,3被移除）
        String value2 = lfuCache.get("key2");
        String value3 = lfuCache.get("key3");
        Assert.assertTrue(null == value2);
        Assert.assertTrue(null == value3);
    }

    @Test
    public void lruCacheTest() {
        LRUCache<String, String> lruCache = new LRUCache<String, String>(3);
        lruCache.put("key1", "value1", DateUnit.SECOND.getMillis() * 3);
        lruCache.put("key2", "value2", DateUnit.SECOND.getMillis() * 3);
        lruCache.put("key3", "value3", DateUnit.SECOND.getMillis() * 3);
        lruCache.get("key1");// 使用时间推近
        lruCache.put("key4", "value4", DateUnit.SECOND.getMillis() * 3);

        // 由于缓存容量只有3，当加入第四个元素的时候，根据LRU规则，最少使用的将被移除（2被移除）
        String value2 = lruCache.get("key");
        Assert.assertTrue(null == value2);
    }

    @Test
    public void timedCacheTest() {
        TimedCache<String, String> timedCache = new TimedCache<String, String>(DateUnit.SECOND.getMillis() * 3);
        timedCache.put("key1", "value1", 1);
        timedCache.put("key2", "value2", DateUnit.SECOND.getMillis() * 5);

        // 启动定时任务，每5毫秒秒检查一次过期
        timedCache.schedulePrune(5);

        ThreadUtil.sleep(5);

        // 5毫秒后由于value2设置了5秒过期，因此只有value2被保留下来
        String value1 = timedCache.get("key1");
        String value2 = timedCache.get("key2");
        Assert.assertTrue(null == value1);
        Assert.assertFalse(null == value2);

        // 取消定时清理
        timedCache.cancelPruneSchedule();
    }
}
