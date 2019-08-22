package org.gjgr.pig.chivalrous.image.math;

public abstract class CompoundFunction2D implements Function2D {

    protected Function2D basis;

    public CompoundFunction2D(Function2D basis) {
        this.basis = basis;
    }

    public Function2D getBasis() {
        return basis;
    }

    public void setBasis(Function2D basis) {
        this.basis = basis;
    }

}
