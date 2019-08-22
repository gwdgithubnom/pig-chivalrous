package org.gjgr.pig.chivalrous.image.math;

public class BlackFunction implements BinaryFunction {
    public boolean isBlack(int rgb) {
        return rgb == 0xff000000;
    }
}
