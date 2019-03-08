package org.gjgr.pig.chivalrous.core.bloom.filter;

import org.gjgr.pig.chivalrous.core.math.HashCommand;

public class TianlFilter extends AbstractFilter {

    public TianlFilter(long maxValue, int machineNum) {
        super(maxValue, machineNum);
    }

    public TianlFilter(long maxValue) {
        super(maxValue);
    }

    @Override
    public long hash(String str) {
        return HashCommand.tianlHash(str) % size;
    }

}
