package org.gjgr.pig.chivalrous.core.bloom.filter;

import org.gjgr.pig.chivalrous.core.math.HashCommand;

public class RSFilter extends AbstractFilter {

    public RSFilter(long maxValue, int machineNum) {
        super(maxValue, machineNum);
    }

    public RSFilter(long maxValue) {
        super(maxValue);
    }

    @Override
    public long hash(String str) {
        return HashCommand.rsHash(str) % size;
    }

}
