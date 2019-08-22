package org.gjgr.pig.chivalrous.image.image;

import org.gjgr.pig.chivalrous.image.math.Noise;

import java.awt.image.BufferedImage;

/**
 * This filter applies a marbling effect to an image, displacing pixels by random amounts.
 */
public class MarbleFilter extends TransformFilter {

    private float[] sinTable;
    private float[] cosTable;
    private float xScale = 4;
    private float yScale = 4;
    private float amount = 1;
    private float turbulence = 1;

    public MarbleFilter() {
        setEdgeAction(CLAMP);
    }

    /**
     * Get the X scale of the effect.
     *
     * @return the scale.
     * @see #setXScale
     */
    public float getXScale() {
        return xScale;
    }

    /**
     * Set the X scale of the effect.
     *
     * @param xScale the scale.
     * @see #getXScale
     */
    public void setXScale(float xScale) {
        this.xScale = xScale;
    }

    /**
     * Get the Y scale of the effect.
     *
     * @return the scale.
     * @see #setYScale
     */
    public float getYScale() {
        return yScale;
    }

    /**
     * Set the Y scale of the effect.
     *
     * @param yScale the scale.
     * @see #getYScale
     */
    public void setYScale(float yScale) {
        this.yScale = yScale;
    }

    /**
     * Get the amount of effect.
     *
     * @return the amount
     * @see #setAmount
     */
    public float getAmount() {
        return amount;
    }

    /**
     * Set the amount of effect.
     *
     * @param amount the amount
     * @min-value 0
     * @max-value 1
     * @see #getAmount
     */
    public void setAmount(float amount) {
        this.amount = amount;
    }

    /**
     * Returns the turbulence of the effect.
     *
     * @return the turbulence of the effect.
     * @see #setTurbulence
     */
    public float getTurbulence() {
        return turbulence;
    }

    /**
     * Specifies the turbulence of the effect.
     *
     * @param turbulence the turbulence of the effect.
     * @min-value 0
     * @max-value 1
     * @see #getTurbulence
     */
    public void setTurbulence(float turbulence) {
        this.turbulence = turbulence;
    }

    private void initialize() {
        sinTable = new float[256];
        cosTable = new float[256];
        for (int i = 0; i < 256; i++) {
            float angle = ImageMath.TWO_PI * i / 256f * turbulence;
            sinTable[i] = (float) (-yScale * Math.sin(angle));
            cosTable[i] = (float) (yScale * Math.cos(angle));
        }
    }

    private int displacementMap(int x, int y) {
        return PixelUtils.clamp((int) (127 * (1 + Noise.noise2(x / xScale, y / xScale))));
    }

    protected void transformInverse(int x, int y, float[] out) {
        int displacement = displacementMap(x, y);
        out[0] = x + sinTable[displacement];
        out[1] = y + cosTable[displacement];
    }

    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        initialize();
        return super.filter(src, dst);
    }

    public String toString() {
        return "Distort/Marble...";
    }
}
