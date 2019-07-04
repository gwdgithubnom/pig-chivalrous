package org.gjgr.pig.chivalrous.image.image;

import java.awt.image.BufferedImage;

/**
 * A filter which simulates the appearance of looking through glass. A separate grayscale displacement image is provided
 * and pixels in the source image are displaced according to the gradient of the displacement map.
 */
public class DisplaceFilter extends TransformFilter {

    private float amount = 1;
    private BufferedImage displacementMap = null;
    private int[] xmap;
    private int[] ymap;
    private int dw;
    private int dh;

    public DisplaceFilter() {
    }

    /**
     * Get the displacement map.
     *
     * @return an image representing the displacment at each point
     * @see #setDisplacementMap
     */
    public BufferedImage getDisplacementMap() {
        return displacementMap;
    }

    /**
     * Set the displacement map.
     *
     * @param displacementMap an image representing the displacment at each point
     * @see #getDisplacementMap
     */
    public void setDisplacementMap(BufferedImage displacementMap) {
        this.displacementMap = displacementMap;
    }

    /**
     * Get the amount of distortion.
     *
     * @return the amount
     * @see #setAmount
     */
    public float getAmount() {
        return amount;
    }

    /**
     * Set the amount of distortion.
     *
     * @param amount the amount
     * @min-value 0
     * @max-value 1
     * @see #getAmount
     */
    public void setAmount(float amount) {
        this.amount = amount;
    }

    protected void transformInverse(int x, int y, float[] out) {
        float xDisplacement;
        float yDisplacement;
        float nx = x;
        float ny = y;
        int i = (y % dh) * dw + x % dw;
        out[0] = x + amount * xmap[i];
        out[1] = y + amount * ymap[i];
    }

    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        int w = src.getWidth();
        int h = src.getHeight();

        BufferedImage dm = displacementMap != null ? displacementMap : src;

        dw = dm.getWidth();
        dh = dm.getHeight();

        int[] mapPixels = new int[dw * dh];
        getRGB(dm, 0, 0, dw, dh, mapPixels);
        xmap = new int[dw * dh];
        ymap = new int[dw * dh];

        int i = 0;
        for (int y = 0; y < dh; y++) {
            for (int x = 0; x < dw; x++) {
                int rgb = mapPixels[i];
                int r = (rgb >> 16) & 0xff;
                int g = (rgb >> 8) & 0xff;
                int b = rgb & 0xff;
                mapPixels[i] = (r + g + b) / 8; // An arbitrary scaling factor which gives a good range for "amount"
                i++;
            }
        }

        i = 0;
        for (int y = 0; y < dh; y++) {
            int j1 = ((y + dh - 1) % dh) * dw;
            int j2 = y * dw;
            int j3 = ((y + 1) % dh) * dw;
            for (int x = 0; x < dw; x++) {
                int k1 = (x + dw - 1) % dw;
                int k2 = x;
                int k3 = (x + 1) % dw;
                xmap[i] = mapPixels[k1 + j1] + mapPixels[k1 + j2] + mapPixels[k1 + j3] - mapPixels[k3 + j1]
                        - mapPixels[k3 + j2] - mapPixels[k3 + j3];
                ymap[i] = mapPixels[k1 + j3] + mapPixels[k2 + j3] + mapPixels[k3 + j3] - mapPixels[k1 + j1]
                        - mapPixels[k2 + j1] - mapPixels[k3 + j1];
                i++;
            }
        }
        mapPixels = null;
        dst = super.filter(src, dst);
        xmap = ymap = null;
        return dst;
    }

    public String toString() {
        return "Distort/Displace...";
    }
}
