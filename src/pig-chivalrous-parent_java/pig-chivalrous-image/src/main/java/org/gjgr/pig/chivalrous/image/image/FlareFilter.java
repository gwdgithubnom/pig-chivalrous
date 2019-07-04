package org.gjgr.pig.chivalrous.image.image;

import org.gjgr.pig.chivalrous.image.math.Noise;

import java.awt.geom.Point2D;

/**
 * An experimental filter for rendering lens flares.
 */
public class FlareFilter extends PointFilter {

    private int rays = 50;
    private float radius;
    private float baseAmount = 1.0f;
    private float ringAmount = 0.2f;
    private float rayAmount = 0.1f;
    private int color = 0xffffffff;
    private int width;
    private int height;
    private float centreX = 0.5f;
    private float centreY = 0.5f;
    private float ringWidth = 1.6f;

    private float linear = 0.03f;
    private float gauss = 0.006f;
    private float mix = 0.50f;
    private float falloff = 6.0f;
    private float sigma;

    private float icentreX;
    private float icentreY;

    public FlareFilter() {
        setRadius(50.0f);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getRingWidth() {
        return ringWidth;
    }

    public void setRingWidth(float ringWidth) {
        this.ringWidth = ringWidth;
    }

    public float getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(float baseAmount) {
        this.baseAmount = baseAmount;
    }

    public float getRingAmount() {
        return ringAmount;
    }

    public void setRingAmount(float ringAmount) {
        this.ringAmount = ringAmount;
    }

    public float getRayAmount() {
        return rayAmount;
    }

    public void setRayAmount(float rayAmount) {
        this.rayAmount = rayAmount;
    }

    public Point2D getCentre() {
        return new Point2D.Float(centreX, centreY);
    }

    public void setCentre(Point2D centre) {
        this.centreX = (float) centre.getX();
        this.centreY = (float) centre.getY();
    }

    /**
     * Get the radius of the effect.
     *
     * @return the radius
     * @see #setRadius
     */
    public float getRadius() {
        return radius;
    }

    /**
     * Set the radius of the effect.
     *
     * @param radius the radius
     * @min-value 0
     * @see #getRadius
     */
    public void setRadius(float radius) {
        this.radius = radius;
        sigma = radius / 3;
    }

    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
        icentreX = centreX * width;
        icentreY = centreY * height;
        super.setDimensions(width, height);
    }

    public int filterRGB(int x, int y, int rgb) {
        float dx = x - icentreX;
        float dy = y - icentreY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        float a =
                (float) Math.exp(-distance * distance * gauss) * mix + (float) Math.exp(-distance * linear) * (1 - mix);
        float ring;

        a *= baseAmount;

        if (distance > radius + ringWidth) {
            a = ImageMath.lerp((distance - (radius + ringWidth)) / falloff, a, 0);
        }

        if (distance < radius - ringWidth || distance > radius + ringWidth) {
            ring = 0;
        } else {
            ring = Math.abs(distance - radius) / ringWidth;
            ring = 1 - ring * ring * (3 - 2 * ring);
            ring *= ringAmount;
        }

        a += ring;

        float angle = (float) Math.atan2(dx, dy) + ImageMath.PI;
        angle = (ImageMath.mod(angle / ImageMath.PI * 17 + 1.0f + Noise.noise1(angle * 10), 1.0f) - 0.5f) * 2;
        angle = Math.abs(angle);
        angle = (float) Math.pow(angle, 5.0);

        float b = rayAmount * angle / (1 + distance * 0.1f);
        a += b;

        a = ImageMath.clamp(a, 0, 1);
        return ImageMath.mixColors(a, rgb, color);
    }

    public String toString() {
        return "Stylize/Flare...";
    }
}
