package org.gjgr.pig.chivalrous.image.image;

import org.gjgr.pig.chivalrous.image.composite.MiscComposite;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * A filter which produces the effect of light rays shining out of an image.
 */
public class RaysFilter extends MotionBlurOp {

    private float opacity = 1.0f;
    private float threshold = 0.0f;
    private float strength = 0.5f;
    private boolean raysOnly = false;
    private Colormap colormap;

    public RaysFilter() {
    }

    /**
     * Get the opacity of the rays.
     *
     * @return the opacity.
     * @see #setOpacity
     */
    public float getOpacity() {
        return opacity;
    }

    /**
     * Set the opacity of the rays.
     *
     * @param opacity the opacity.
     * @see #getOpacity
     */
    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    /**
     * Get the threshold value.
     *
     * @return the threshold value
     * @see #setThreshold
     */
    public float getThreshold() {
        return threshold;
    }

    /**
     * Set the threshold value.
     *
     * @param threshold the threshold value
     * @see #getThreshold
     */
    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }

    /**
     * Get the strength of the rays.
     *
     * @return the strength.
     * @see #setStrength
     */
    public float getStrength() {
        return strength;
    }

    /**
     * Set the strength of the rays.
     *
     * @param strength the strength.
     * @see #getStrength
     */
    public void setStrength(float strength) {
        this.strength = strength;
    }

    /**
     * Get whether to render only the rays.
     *
     * @return true to render rays only.
     * @see #setRaysOnly
     */
    public boolean getRaysOnly() {
        return raysOnly;
    }

    /**
     * Set whether to render only the rays.
     *
     * @param raysOnly true to render rays only.
     * @see #getRaysOnly
     */
    public void setRaysOnly(boolean raysOnly) {
        this.raysOnly = raysOnly;
    }

    /**
     * Get the colormap to be used for the filter.
     *
     * @return the colormap
     * @see #setColormap
     */
    public Colormap getColormap() {
        return colormap;
    }

    /**
     * Set the colormap to be used for the filter.
     *
     * @param colormap the colormap
     * @see #getColormap
     */
    public void setColormap(Colormap colormap) {
        this.colormap = colormap;
    }

    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        int width = src.getWidth();
        int height = src.getHeight();
        int[] pixels = new int[width];
        int[] srcPixels = new int[width];

        BufferedImage rays = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        int threshold3 = (int) (threshold * 3 * 255);
        for (int y = 0; y < height; y++) {
            getRGB(src, 0, y, width, 1, pixels);
            for (int x = 0; x < width; x++) {
                int rgb = pixels[x];
                int a = rgb & 0xff000000;
                int r = (rgb >> 16) & 0xff;
                int g = (rgb >> 8) & 0xff;
                int b = rgb & 0xff;
                int l = r + g + b;
                if (l < threshold3) {
                    pixels[x] = 0xff000000;
                } else {
                    l /= 3;
                    pixels[x] = a | (l << 16) | (l << 8) | l;
                }
            }
            setRGB(rays, 0, y, width, 1, pixels);
        }

        rays = super.filter(rays, null);

        for (int y = 0; y < height; y++) {
            getRGB(rays, 0, y, width, 1, pixels);
            getRGB(src, 0, y, width, 1, srcPixels);
            for (int x = 0; x < width; x++) {
                int rgb = pixels[x];
                int a = rgb & 0xff000000;
                int r = (rgb >> 16) & 0xff;
                int g = (rgb >> 8) & 0xff;
                int b = rgb & 0xff;

                if (colormap != null) {
                    int l = r + g + b;
                    rgb = colormap.getColor(l * strength * (1 / 3f));
                } else {
                    r = PixelUtils.clamp((int) (r * strength));
                    g = PixelUtils.clamp((int) (g * strength));
                    b = PixelUtils.clamp((int) (b * strength));
                    rgb = a | (r << 16) | (g << 8) | b;
                }

                pixels[x] = rgb;
            }
            setRGB(rays, 0, y, width, 1, pixels);
        }

        if (dst == null) {
            dst = createCompatibleDestImage(src, null);
        }

        Graphics2D g = dst.createGraphics();
        if (!raysOnly) {
            g.setComposite(AlphaComposite.SrcOver);
            g.drawRenderedImage(src, null);
        }
        g.setComposite(MiscComposite.getInstance(MiscComposite.ADD, opacity));
        g.drawRenderedImage(rays, null);
        g.dispose();

        return dst;
    }

    public String toString() {
        return "Stylize/Rays...";
    }
}
