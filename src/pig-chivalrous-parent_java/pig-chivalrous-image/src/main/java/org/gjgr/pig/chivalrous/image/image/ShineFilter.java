package org.gjgr.pig.chivalrous.image.image;

import org.gjgr.pig.chivalrous.image.composite.AddComposite;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ShineFilter extends AbstractBufferedImageOp {

    private float radius = 5;
    private float angle = (float) Math.PI * 7 / 4;
    private float distance = 5;
    private float bevel = 0.5f;
    private boolean shadowOnly = false;
    private int shineColor = 0xffffffff;
    private float brightness = 0.2f;
    private float softness = 0;

    public ShineFilter() {
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    /**
     * Get the radius of the kernel.
     *
     * @return the radius
     */
    public float getRadius() {
        return radius;
    }

    /**
     * Set the radius of the kernel, and hence the amount of blur. The bigger the radius, the longer this filter will
     * take.
     *
     * @param radius the radius of the blur in pixels.
     */
    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getBevel() {
        return bevel;
    }

    public void setBevel(float bevel) {
        this.bevel = bevel;
    }

    public int getShineColor() {
        return shineColor;
    }

    public void setShineColor(int shineColor) {
        this.shineColor = shineColor;
    }

    public boolean getShadowOnly() {
        return shadowOnly;
    }

    public void setShadowOnly(boolean shadowOnly) {
        this.shadowOnly = shadowOnly;
    }

    public float getBrightness() {
        return brightness;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
    }

    public float getSoftness() {
        return softness;
    }

    public void setSoftness(float softness) {
        this.softness = softness;
    }

    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        int width = src.getWidth();
        int height = src.getHeight();

        if (dst == null) {
            dst = createCompatibleDestImage(src, null);
        }

        float xOffset = distance * (float) Math.cos(angle);
        float yOffset = -distance * (float) Math.sin(angle);

        BufferedImage matte = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        ErodeAlphaFilter s = new ErodeAlphaFilter(bevel * 10, 0.75f, 0.1f);
        matte = s.filter(src, null);

        BufferedImage shineLayer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = shineLayer.createGraphics();
        g.setColor(new Color(shineColor));
        g.fillRect(0, 0, width, height);
        g.setComposite(AlphaComposite.DstIn);
        g.drawRenderedImage(matte, null);
        g.setComposite(AlphaComposite.DstOut);
        g.translate(xOffset, yOffset);
        g.drawRenderedImage(matte, null);
        g.dispose();
        shineLayer = new GaussianFilter(radius).filter(shineLayer, null);
        shineLayer = new RescaleFilter(3 * brightness).filter(shineLayer, shineLayer);

        g = dst.createGraphics();
        g.drawRenderedImage(src, null);
        g.setComposite(new AddComposite(1.0f));
        g.drawRenderedImage(shineLayer, null);
        g.dispose();

        return dst;
    }

    public String toString() {
        return "Stylize/Shine...";
    }
}
