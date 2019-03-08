package org.gjgr.pig.chivalrous.core.bloom.filter;

import org.gjgr.pig.chivalrous.core.math.HashCommand;

public class FNVFilter extends AbstractFilter {

    public FNVFilter(long maxValue, int machineNum) {
        super(maxValue, machineNum);
    }

    public FNVFilter(long maxValue) {
        super(maxValue);
    }

    @Override
    public long hash(String str) {
        return HashCommand.fnvHash(str);
    }

}
