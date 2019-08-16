package org.gjgr.pig.chivalrous.core.bloom.filter;

import org.gjgr.pig.chivalrous.core.math.HashCommand;

/**
 * 默认Bloom过滤器，使用Java自带的Hash算法
 *
 * @author loolly
 */
public class DefaultFilter extends AbstractFilter {

    public DefaultFilter(long maxValue, int MACHINENUM) {
        super(maxValue, MACHINENUM);
    }

    public DefaultFilter(long maxValue) {
        super(maxValue);
    }

    @Override
    public long hash(String str) {
        return HashCommand.javaDefaultHash(str) % size;
    }
}
