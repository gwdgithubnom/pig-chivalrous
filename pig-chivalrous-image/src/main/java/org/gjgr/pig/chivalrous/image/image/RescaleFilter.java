package org.gjgr.pig.chivalrous.image.image;

/**
 * A filter which simply multiplies pixel values by a given scale factor.
 */
public class RescaleFilter extends TransferFilter {

    private float scale = 1.0f;

    public RescaleFilter() {
    }

    public RescaleFilter(float scale) {
        this.scale = scale;
    }

    protected float transferFunction(float v) {
        return v * scale;
    }

    /**
     * Returns the scale factor.
     *
     * @return the scale factor.
     * @see #setScale
     */
    public float getScale() {
        return scale;
    }

    /**
     * Specifies the scale factor.
     *
     * @param scale the scale factor.
     * @min-value 1
     * @max-value 5+
     * @see #getScale
     */
    public void setScale(float scale) {
        this.scale = scale;
        initialized = false;
    }

    public String toString() {
        return "Colors/Rescale...";
    }

}
