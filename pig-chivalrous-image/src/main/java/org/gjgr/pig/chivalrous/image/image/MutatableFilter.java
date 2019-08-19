package org.gjgr.pig.chivalrous.image.image;

import java.awt.image.BufferedImageOp;

public interface MutatableFilter {
    void mutate(float mutationLevel, BufferedImageOp dst, boolean keepShape, boolean keepColors);
}
