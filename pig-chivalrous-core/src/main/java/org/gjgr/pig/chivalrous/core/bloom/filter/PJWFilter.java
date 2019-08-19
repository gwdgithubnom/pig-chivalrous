package org.gjgr.pig.chivalrous.core.bloom.filter;

import org.gjgr.pig.chivalrous.core.math.HashCommand;

public class PJWFilter extends AbstractFilter {

    public PJWFilter(long maxValue, int machineNum) {
        super(maxValue, machineNum);
    }

    public PJWFilter(long maxValue) {
        super(maxValue);
    }

    @Override
    public long hash(String str) {
        return HashCommand.pjwHash(str) % size;
    }

}
