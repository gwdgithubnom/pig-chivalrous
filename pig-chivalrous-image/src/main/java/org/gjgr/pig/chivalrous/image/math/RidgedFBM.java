package org.gjgr.pig.chivalrous.image.math;

public class RidgedFBM implements Function2D {

    public float evaluate(float x, float y) {
        return 1 - Math.abs(Noise.noise2(x, y));
    }

}
