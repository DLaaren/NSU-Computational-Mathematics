package org.example;

public class Params {
    private final double a;
    private final double b;
    private final double c;
    private final boolean useGraphics;

    public Params(double a, double b, double c, boolean useGraphics) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.useGraphics = useGraphics;
    }

    public double getC() {
        return c;
    }

    public double getB() {
        return b;
    }

    public double getA() {
        return a;
    }

    public boolean shouldUseGraphics() {
        return useGraphics;
    }
}
