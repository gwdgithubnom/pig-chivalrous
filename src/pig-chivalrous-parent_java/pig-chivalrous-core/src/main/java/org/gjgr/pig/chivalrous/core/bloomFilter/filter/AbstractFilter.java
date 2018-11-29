package org.gjgr.pig.chivalrous.core.bloomFilter.filter;

import org.gjgr.pig.chivalrous.core.bloomFilter.BloomFilter;
import org.gjgr.pig.chivalrous.core.bloomFilter.bitMap.BitMap;
import org.gjgr.pig.chivalrous.core.bloomFilter.bitMap.IntMap;
import org.gjgr.pig.chivalrous.core.bloomFilter.bitMap.LongMap;

/**
 * 抽象Bloom过滤器
 *
 * @author loolly
 */
public abstract class AbstractFilter implements BloomFilter {

    protected long size = 0;
    private BitMap bm = null;

    public AbstractFilter(long maxValue, int machineNum) {
        init(maxValue, machineNum);
    }

    public AbstractFilter(long maxValue) {
        this(maxValue, BitMap.MACHINE32);
    }

    public void init(long maxValue, int machineNum) {
        this.size = maxValue;
        switch (machineNum) {
            case BitMap.MACHINE32:
                bm = new IntMap((int) (size / machineNum));
                break;
            case BitMap.MACHINE64:
                bm = new LongMap((int) (size / machineNum));
                break;
            default:
                throw new RuntimeException("Error Machine number!");
        }
    }

    @Override
    public boolean contains(String str) {
        return bm.contains(hash(str));
    }

    @Override
    public boolean add(String str) {
        final long hash = this.hash(str);
        if (bm.contains(hash)) {
            return false;
        }

        bm.add(hash);
        return true;
    }

    /**
     * 自定义Hash方法
     *
     * @param str 字符串
     * @return HashCode
     */
    public abstract long hash(String str);
}