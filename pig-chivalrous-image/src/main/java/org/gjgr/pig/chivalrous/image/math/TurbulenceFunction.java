package org.gjgr.pig.chivalrous.image.math;

public class TurbulenceFunction extends CompoundFunction2D {

    private float octaves;

    public TurbulenceFunction(Function2D basis, float octaves) {
        super(basis);
        this.octaves = octaves;
    }

    public float getOctaves() {
        return octaves;
    }

    public void setOctaves(float octaves) {
        this.octaves = octaves;
    }

    public float evaluate(float x, float y) {
        float t = 0.0f;

        for (float f = 1.0f; f <= octaves; f *= 2) {
            t += Math.abs(basis.evaluate(f * x, f * y)) / f;
        }
        return t;
    }

}
