package org.gjgr.pig.chivalrous.image.math;

public class MathFunction1D implements Function1D {

    public static final int SIN = 1;
    public static final int COS = 2;
    public static final int TAN = 3;
    public static final int SQRT = 4;
    public static final int ASIN = -1;
    public static final int ACOS = -2;
    public static final int ATAN = -3;
    public static final int SQR = -4;

    private int operation;

    public MathFunction1D(int operation) {
        this.operation = operation;
    }

    public float evaluate(float v) {
        switch (operation) {
            case SIN:
                return (float) Math.sin(v);
            case COS:
                return (float) Math.cos(v);
            case TAN:
                return (float) Math.tan(v);
            case SQRT:
                return (float) Math.sqrt(v);
            case ASIN:
                return (float) Math.asin(v);
            case ACOS:
                return (float) Math.acos(v);
            case ATAN:
                return (float) Math.atan(v);
            case SQR:
                return v * v;
            default:
                return v;
        }
    }
}
