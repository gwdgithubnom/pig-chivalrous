package org.gjgr.pig.chivalrous.image.image;

/**
 * A filter which allows the red, green and blue channels of an image to be mixed into each other.
 */
public class ChannelMixFilter extends PointFilter {

    private int blueGreen;
    private int redBlue;
    private int greenRed;
    private int intoR;
    private int intoG;
    private int intoB;

    public ChannelMixFilter() {
        canFilterIndexColorModel = true;
    }

    public int getBlueGreen() {
        return blueGreen;
    }

    public void setBlueGreen(int blueGreen) {
        this.blueGreen = blueGreen;
    }

    public int getRedBlue() {
        return redBlue;
    }

    public void setRedBlue(int redBlue) {
        this.redBlue = redBlue;
    }

    public int getGreenRed() {
        return greenRed;
    }

    public void setGreenRed(int greenRed) {
        this.greenRed = greenRed;
    }

    public int getIntoR() {
        return intoR;
    }

    public void setIntoR(int intoR) {
        this.intoR = intoR;
    }

    public int getIntoG() {
        return intoG;
    }

    public void setIntoG(int intoG) {
        this.intoG = intoG;
    }

    public int getIntoB() {
        return intoB;
    }

    public void setIntoB(int intoB) {
        this.intoB = intoB;
    }

    public int filterRGB(int x, int y, int rgb) {
        int a = rgb & 0xff000000;
        int r = (rgb >> 16) & 0xff;
        int g = (rgb >> 8) & 0xff;
        int b = rgb & 0xff;
        int nr = PixelUtils.clamp((intoR * (blueGreen * g + (255 - blueGreen) * b) / 255 + (255 - intoR) * r) / 255);
        int ng = PixelUtils.clamp((intoG * (redBlue * b + (255 - redBlue) * r) / 255 + (255 - intoG) * g) / 255);
        int nb = PixelUtils.clamp((intoB * (greenRed * r + (255 - greenRed) * g) / 255 + (255 - intoB) * b) / 255);
        return a | (nr << 16) | (ng << 8) | nb;
    }

    public String toString() {
        return "Colors/Mix Channels...";
    }
}
