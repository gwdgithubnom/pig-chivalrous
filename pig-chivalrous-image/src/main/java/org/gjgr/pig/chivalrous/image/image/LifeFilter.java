package org.gjgr.pig.chivalrous.image.image;

import java.awt.Rectangle;

/**
 * A filter which performs one round of the game of Life on an image.
 */
public class LifeFilter extends BinaryFilter {

    public LifeFilter() {
    }

    protected int[] filterPixels(int width, int height, int[] inPixels, Rectangle transformedSpace) {
        int index = 0;
        int[] outPixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r = 0;
                int g = 0;
                int b = 0;
                int pixel = inPixels[y * width + x];
                int a = pixel & 0xff000000;
                int neighbours = 0;

                for (int row = -1; row <= 1; row++) {
                    int iy = y + row;
                    int ioffset;
                    if (0 <= iy && iy < height) {
                        ioffset = iy * width;
                        for (int col = -1; col <= 1; col++) {
                            int ix = x + col;
                            if (!(row == 0 && col == 0) && 0 <= ix && ix < width) {
                                int rgb = inPixels[ioffset + ix];
                                if (blackFunction.isBlack(rgb)) {
                                    neighbours++;
                                }
                            }
                        }
                    }
                }

                if (blackFunction.isBlack(pixel)) {
                    outPixels[index++] = (neighbours == 2 || neighbours == 3) ? pixel : 0xffffffff;
                } else {
                    outPixels[index++] = neighbours == 3 ? 0xff000000 : pixel;
                }
            }

        }
        return outPixels;
    }

    public String toString() {
        return "Binary/Life";
    }

}
