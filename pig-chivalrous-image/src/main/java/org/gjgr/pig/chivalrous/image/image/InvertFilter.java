package org.gjgr.pig.chivalrous.image.image;

/**
 * A filter which inverts the RGB channels of an image.
 */
public class InvertFilter extends PointFilter {

    public InvertFilter() {
        canFilterIndexColorModel = true;
    }

    public int filterRGB(int x, int y, int rgb) {
        int a = rgb & 0xff000000;
        return a | (~rgb & 0x00ffffff);
    }

    public String toString() {
        return "Colors/Invert";
    }
}
