package org.gjgr.pig.chivalrous.image.image;

import org.gjgr.pig.chivalrous.image.math.BinaryFunction;
import org.gjgr.pig.chivalrous.image.math.BlackFunction;

/**
 * The superclass for some of the filters which work on binary images.
 */
public abstract class BinaryFilter extends WholeImageFilter {

    protected int newColor = 0xff000000;
    protected BinaryFunction blackFunction = new BlackFunction();
    protected int iterations = 1;
    protected Colormap colormap;

    /**
     * Get the number of iterations the effect is performed.
     *
     * @return the number of iterations
     * @see #setIterations
     */
    public int getIterations() {
        return iterations;
    }

    /**
     * Set the number of iterations the effect is performed.
     *
     * @param iterations the number of iterations
     * @min-value 0
     * @see #getIterations
     */
    public void setIterations(int iterations) {
        this.iterations = iterations;
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

    public int getNewColor() {
        return newColor;
    }

    public void setNewColor(int newColor) {
        this.newColor = newColor;
    }

    public BinaryFunction getBlackFunction() {
        return blackFunction;
    }

    public void setBlackFunction(BinaryFunction blackFunction) {
        this.blackFunction = blackFunction;
    }

}
