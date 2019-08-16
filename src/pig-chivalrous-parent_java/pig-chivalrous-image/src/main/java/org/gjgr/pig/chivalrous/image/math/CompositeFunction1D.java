package org.gjgr.pig.chivalrous.image.math;

public class CompositeFunction1D implements Function1D {

    private Function1D f1;
    private Function1D f2;

    public CompositeFunction1D(Function1D f1, Function1D f2) {
        this.f1 = f1;
        this.f2 = f2;
    }

    public float evaluate(float v) {
        return f1.evaluate(f2.evaluate(v));
    }
}
