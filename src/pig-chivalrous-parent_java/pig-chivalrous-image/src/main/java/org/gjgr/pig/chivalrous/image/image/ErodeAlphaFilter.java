package org.gjgr.pig.chivalrous.image.image;

import java.awt.image.BufferedImage;

public class ErodeAlphaFilter extends PointFilter {

    protected float radius = 5;
    private float threshold;
    private float softness = 0;
    private float lowerThreshold;
    private float upperThreshold;

    public ErodeAlphaFilter() {
        this(3, 0.75f, 0);
    }

    public ErodeAlphaFilter(float radius, float threshold, float softness) {
        this.radius = radius;
        this.threshold = threshold;
        this.softness = softness;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getThreshold() {
        return threshold;
    }

    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }

    public float getSoftness() {
        return softness;
    }

    public void setSoftness(float softness) {
        this.softness = softness;
    }

    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        dst = new GaussianFilter((int) radius).filter(src, null);
        lowerThreshold = 255 * (threshold - softness * 0.5f);
        upperThreshold = 255 * (threshold + softness * 0.5f);
        return super.filter(dst, dst);
    }

    public int filterRGB(int x, int y, int rgb) {
        int a = (rgb >> 24) & 0xff;
        int r = (rgb >> 16) & 0xff;
        int g = (rgb >> 8) & 0xff;
        int b = rgb & 0xff;
        if (a == 255) {
            return 0xffffffff;
        }
        float f = ImageMath.smoothStep(lowerThreshold, upperThreshold, (float) a);
        a = (int) (f * 255);
        if (a < 0) {
            a = 0;
        } else if (a > 255) {
            a = 255;
        }
        return (a << 24) | 0xffffff;
    }

    public String toString() {
        return "Alpha/Erode...";
    }
}
