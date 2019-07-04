package org.gjgr.pig.chivalrous.image.image;

import org.gjgr.pig.chivalrous.image.math.Noise;

import java.awt.Rectangle;
import java.util.Random;

/**
 * A filter which simulates underwater caustics. This can be animated to get a bottom-of-the-swimming-pool effect.
 */
public class CausticsFilter extends WholeImageFilter {

    private float scale = 32;
    private float angle = 0.0f;
    private int brightness = 10;
    private float amount = 1.0f;
    private float turbulence = 1.0f;
    private float dispersion = 0.0f;
    private float time = 0.0f;
    private int samples = 2;
    private int bgColor = 0xff799fff;

    private float s;
    private float c;

    public CausticsFilter() {
    }

    private static int add(int rgb, float brightness) {
        int r = (rgb >> 16) & 0xff;
        int g = (rgb >> 8) & 0xff;
        int b = rgb & 0xff;
        r += brightness;
        g += brightness;
        b += brightness;
        if (r > 255) {
            r = 255;
        }
        if (g > 255) {
            g = 255;
        }
        if (b > 255) {
            b = 255;
        }
        return 0xff000000 | (r << 16) | (g << 8) | b;
    }

    private static int add(int rgb, float brightness, int c) {
        int r = (rgb >> 16) & 0xff;
        int g = (rgb >> 8) & 0xff;
        int b = rgb & 0xff;
        if (c == 2) {
            r += brightness;
        } else if (c == 1) {
            g += brightness;
        } else {
            b += brightness;
        }
        if (r > 255) {
            r = 255;
        }
        if (g > 255) {
            g = 255;
        }
        if (b > 255) {
            b = 255;
        }
        return 0xff000000 | (r << 16) | (g << 8) | b;
    }

    private static float turbulence2(float x, float y, float time, float octaves) {
        float value = 0.0f;
        float remainder;
        float lacunarity = 2.0f;
        float f = 1.0f;
        int i;

        // to prevent "cascading" effects
        x += 371;
        y += 529;

        for (i = 0; i < (int) octaves; i++) {
            value += Noise.noise3(x, y, time) / f;
            x *= lacunarity;
            y *= lacunarity;
            f *= 2;
        }

        remainder = octaves - (int) octaves;
        if (remainder != 0) {
            value += remainder * Noise.noise3(x, y, time) / f;
        }

        return value;
    }

    /**
     * Returns the scale of the texture.
     *
     * @return the scale of the texture.
     * @see #setScale
     */
    public float getScale() {
        return scale;
    }

    /**
     * Specifies the scale of the texture.
     *
     * @param scale the scale of the texture.
     * @min-value 1
     * @max-value 300+
     * @see #getScale
     */
    public void setScale(float scale) {
        this.scale = scale;
    }

    /**
     * Get the brightness.
     *
     * @return the brightness.
     * @see #setBrightness
     */
    public int getBrightness() {
        return brightness;
    }

    /**
     * Set the brightness.
     *
     * @param brightness the brightness.
     * @min-value 0
     * @max-value 1
     * @see #getBrightness
     */
    public void setBrightness(int brightness) {
        this.brightness = brightness;
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
     * Specifies the turbulence of the texture.
     *
     * @param turbulence the turbulence of the texture.
     * @min-value 0
     * @max-value 1
     * @see #getTurbulence
     */
    public void setTurbulence(float turbulence) {
        this.turbulence = turbulence;
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
     * Get the dispersion.
     *
     * @return the dispersion
     * @see #setDispersion
     */
    public float getDispersion() {
        return dispersion;
    }

    /**
     * Set the dispersion.
     *
     * @param dispersion the dispersion
     * @min-value 0
     * @max-value 1
     * @see #getDispersion
     */
    public void setDispersion(float dispersion) {
        this.dispersion = dispersion;
    }

    /**
     * Set the time.
     *
     * @return the time
     * @see #setTime
     */
    public float getTime() {
        return time;
    }

    /**
     * Set the time. Use this to animate the effect.
     *
     * @param time the time
     * @see #getTime
     */
    public void setTime(float time) {
        this.time = time;
    }

    /**
     * Get the number of samples per pixel.
     *
     * @return the number of samples
     * @see #setSamples
     */
    public int getSamples() {
        return samples;
    }

    /**
     * Set the number of samples per pixel. More samples means better quality, but slower rendering.
     *
     * @param samples the number of samples
     * @see #getSamples
     */
    public void setSamples(int samples) {
        this.samples = samples;
    }

    /**
     * Get the background color.
     *
     * @return the color
     * @see #setBgColor
     */
    public int getBgColor() {
        return bgColor;
    }

    /**
     * Set the background color.
     *
     * @param c the color
     * @see #getBgColor
     */
    public void setBgColor(int c) {
        bgColor = c;
    }

    protected int[] filterPixels(int width, int height, int[] inPixels, Rectangle transformedSpace) {
        Random random = new Random(0);

        s = (float) Math.sin(0.1);
        c = (float) Math.cos(0.1);

        int srcWidth = originalSpace.width;
        int srcHeight = originalSpace.height;
        int outWidth = transformedSpace.width;
        int outHeight = transformedSpace.height;
        int index = 0;
        int[] pixels = new int[outWidth * outHeight];

        for (int y = 0; y < outHeight; y++) {
            for (int x = 0; x < outWidth; x++) {
                pixels[index++] = bgColor;
            }
        }

        int v = brightness / samples;
        if (v == 0) {
            v = 1;
        }

        float rs = 1.0f / scale;
        float d = 0.95f;
        index = 0;
        for (int y = 0; y < outHeight; y++) {
            for (int x = 0; x < outWidth; x++) {
                for (int s = 0; s < samples; s++) {
                    float sx = x + random.nextFloat();
                    float sy = y + random.nextFloat();
                    float nx = sx * rs;
                    float ny = sy * rs;
                    float xDisplacement;
                    float yDisplacement;
                    float focus = 0.1f + amount;
                    xDisplacement = evaluate(nx - d, ny) - evaluate(nx + d, ny);
                    yDisplacement = evaluate(nx, ny + d) - evaluate(nx, ny - d);

                    if (dispersion > 0) {
                        for (int c = 0; c < 3; c++) {
                            float ca = (1 + c * dispersion);
                            float srcX = sx + scale * focus * xDisplacement * ca;
                            float srcY = sy + scale * focus * yDisplacement * ca;

                            if (srcX < 0 || srcX >= outWidth - 1 || srcY < 0 || srcY >= outHeight - 1) {
                            } else {
                                int i = ((int) srcY) * outWidth + (int) srcX;
                                int rgb = pixels[i];
                                int r = (rgb >> 16) & 0xff;
                                int g = (rgb >> 8) & 0xff;
                                int b = rgb & 0xff;
                                if (c == 2) {
                                    r += v;
                                } else if (c == 1) {
                                    g += v;
                                } else {
                                    b += v;
                                }
                                if (r > 255) {
                                    r = 255;
                                }
                                if (g > 255) {
                                    g = 255;
                                }
                                if (b > 255) {
                                    b = 255;
                                }
                                pixels[i] = 0xff000000 | (r << 16) | (g << 8) | b;
                            }
                        }
                    } else {
                        float srcX = sx + scale * focus * xDisplacement;
                        float srcY = sy + scale * focus * yDisplacement;

                        if (srcX < 0 || srcX >= outWidth - 1 || srcY < 0 || srcY >= outHeight - 1) {
                        } else {
                            int i = ((int) srcY) * outWidth + (int) srcX;
                            int rgb = pixels[i];
                            int r = (rgb >> 16) & 0xff;
                            int g = (rgb >> 8) & 0xff;
                            int b = rgb & 0xff;
                            r += v;
                            g += v;
                            b += v;
                            if (r > 255) {
                                r = 255;
                            }
                            if (g > 255) {
                                g = 255;
                            }
                            if (b > 255) {
                                b = 255;
                            }
                            pixels[i] = 0xff000000 | (r << 16) | (g << 8) | b;
                        }
                    }
                }
            }
        }
        return pixels;
    }

    private float evaluate(float x, float y) {
        float xt = s * x + c * time;
        float tt = c * x - c * time;
        float f = turbulence == 0.0 ? Noise.noise3(xt, y, tt) : turbulence2(xt, y, tt, turbulence);
        return f;
    }

    public String toString() {
        return "Texture/Caustics...";
    }

}
