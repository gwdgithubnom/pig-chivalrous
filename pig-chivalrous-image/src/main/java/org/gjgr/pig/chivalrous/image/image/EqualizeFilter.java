package org.gjgr.pig.chivalrous.image.image;

import java.awt.Rectangle;

/**
 * A filter to perform auto-equalization on an image.
 */
public class EqualizeFilter extends WholeImageFilter {

    private int[][] lut;

    public EqualizeFilter() {
    }

    protected int[] filterPixels(int width, int height, int[] inPixels, Rectangle transformedSpace) {
        Histogram histogram = new Histogram(inPixels, width, height, 0, width);

        int i;
        int j;

        if (histogram.getNumSamples() > 0) {
            float scale = 255.0f / histogram.getNumSamples();
            lut = new int[3][256];
            for (i = 0; i < 3; i++) {
                lut[i][0] = histogram.getFrequency(i, 0);
                for (j = 1; j < 256; j++) {
                    lut[i][j] = lut[i][j - 1] + histogram.getFrequency(i, j);
                }
                for (j = 0; j < 256; j++) {
                    lut[i][j] = Math.round(lut[i][j] * scale);
                }
            }
        } else {
            lut = null;
        }

        i = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                inPixels[i] = filterRGB(x, y, inPixels[i]);
                i++;
            }
        }
        lut = null;

        return inPixels;
    }

    private int filterRGB(int x, int y, int rgb) {
        if (lut != null) {
            int a = rgb & 0xff000000;
            int r = lut[Histogram.RED][(rgb >> 16) & 0xff];
            int g = lut[Histogram.GREEN][(rgb >> 8) & 0xff];
            int b = lut[Histogram.BLUE][rgb & 0xff];

            return a | (r << 16) | (g << 8) | b;
        }
        return rgb;
    }

    public String toString() {
        return "Colors/Equalize";
    }
}
