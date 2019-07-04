package org.gjgr.pig.chivalrous.image.image;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

/**
 * A BufferedImageOp which iterates another BufferedImageOp.
 */
public class IteratedFilter extends AbstractBufferedImageOp {
    private BufferedImageOp filter;
    private int iterations;

    /**
     * Construct an IteratedFilter.
     *
     * @param filter     the filetr to iterate
     * @param iterations the number of iterations
     */
    public IteratedFilter(BufferedImageOp filter, int iterations) {
        this.filter = filter;
        this.iterations = iterations;
    }

    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        BufferedImage image = src;

        for (int i = 0; i < iterations; i++) {
            image = filter.filter(image, dst);
        }

        return image;
    }
}
