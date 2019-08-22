package org.gjgr.pig.chivalrous.core.bloom.filter;

import org.gjgr.pig.chivalrous.core.math.HashCommand;

public class ELFFilter extends AbstractFilter {

    public ELFFilter(long maxValue, int MACHINENUM) {
        super(maxValue, MACHINENUM);
    }

    public ELFFilter(long maxValue) {
        super(maxValue);
    }

    @Override
    public long hash(String str) {
        return HashCommand.elfHash(str) % size;
    }

}
