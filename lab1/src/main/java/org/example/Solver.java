package org.example;


// y(x) = x^3 + ax^2 + bx + c
// y'(x) = 3x^2 + 2ax + b
// ~x_1 = (-a + sqrt(a^2 - 3b))/3
// ~x_2 = (-a - sqrt(a^2 - 3b))/3
// D = (a^2 - 3b) // 2
public class Solver {
    private final double EPS = 10e-9;
    private final double DELTA = 1;
    private enum QuadraticSolutionState {
        NO_SOLUTIONS,
        TWO_SOLUTIONS,
        ONE_SOLUTION,
    }
    public enum CubicSolutionState {
        ONE_SOLUTION,
        TWO_SOLUTIONS,
        THREE_SOLUTIONS
    }
    private record Segment(double l, double r) {}
    private record Ray(double startX, boolean goesRight) {}
    private record QuadraticSolution(double quarterD, double x1, double x2, QuadraticSolutionState state) {}
    public record CubicSolution(double x1, int x1_mult, double x2, int x2_mult, double x3, int x3_mult, CubicSolutionState state) {

    }
    private final Params params;
    public Solver(Params params) {
        this.params = params;
    }
    private double y(double x) {
        return Math.pow(x, 3) + params.getA() * Math.pow(x, 2) + params.getB() * x + params.getC();
    }

    private double difY(double x) {
        return 3 * Math.pow(x, 2) + params.getA() * 2 * x + params.getB();
    }
    private boolean isASolution(double x) {
        return Math.abs(y(x)) < EPS;
    }

    // y'(x) = 3x^2 + 2ax + b
    // ~x_1 = (-a + sqrt(a^2 - 3b))/3
    // ~x_2 = (-a - sqrt(a^2 - 3b))/3
    // D = (a^2 - 3b)
    private QuadraticSolution solveDifY() {
        double quarterD = Math.pow(this.params.getA(), 2) - 3 * this.params.getB();
        double x1, x2;
        QuadraticSolutionState state;
        if (quarterD < 0) {
            state = QuadraticSolutionState.NO_SOLUTIONS;
            x1 = 0;
            x2 = 0;
        } else if (quarterD == 0) {
            state = QuadraticSolutionState.ONE_SOLUTION;
            x1 = (-this.params.getA()) / 3;
            x2 = 0;
        } else {
            state = QuadraticSolutionState.TWO_SOLUTIONS;
            x1 = (-this.params.getA() - Math.sqrt(quarterD)) / 3;
            x2 = (-this.params.getA() + Math.sqrt(quarterD)) / 3;
        }
        return new QuadraticSolution(quarterD, x1, x2, state);
    }
    private double findSolutionOnSegment(Segment segment) {
        if (isASolution(segment.r())) return segment.r();
        if (isASolution(segment.l())) return segment.l();
        double l = segment.l();
        double r = segment.r();
        double c = (l + r) / 2;
        if (isASolution(c)) return c;
        while (true) {
            if (isASolution(c)) {
                return c;
            } else if (y(c) * y(r) < 0) {
                l = c;
            } else {
                r = c;
            }
            c = (l + r) / 2.0;
        }
    }
    private double findSolutionOnRay(Ray ray) {
        double x1 = ray.startX();
        boolean goingRight = ray.goesRight();
        double step = goingRight ? DELTA : -DELTA;
        double x2 = x1 + step;
        Segment solutionSegment;
        while(true) {
            if (isASolution(x1)) return x1;
            if (isASolution(x2)) return x2;
            if (y(x1) * y(x2) > 0) {
                x1 = x2;
                x2 = x2 + step;
            } else {
                double l = x1, r = x2;
                if (!goingRight) {
                    l = x2;
                    r = x1;
                }
                solutionSegment = new Segment(l, r);
                break;
            }
        }
        return findSolutionOnSegment(solutionSegment);
    }
    public CubicSolution solve() {
        System.out.printf("solving x^3 + %fx^2 + %fx + %f%s", params.getA(), params.getB(), params.getC(), params.shouldUseGraphics() ? " with graphics\n" : "\n");
        QuadraticSolution diffSolution = solveDifY();
        switch (diffSolution.state) {
            case NO_SOLUTIONS, ONE_SOLUTION -> {
                double sol = 0;
                if (isASolution(0)) {
                    sol = 0;
                } else if (y(0) > 0) {
                    sol = findSolutionOnRay(new Ray(0, false));
                } else {
                    sol = findSolutionOnRay(new Ray(0, true));
                }

                int multiplicity = calculateRootMultiplicity(sol, 0, 0);
                return new CubicSolution(sol, multiplicity,0, 0, 0, 0, CubicSolutionState.ONE_SOLUTION);
            }
            case TWO_SOLUTIONS -> {
                double x1 = diffSolution.x1();
                double x2 = diffSolution.x2();
                if (isASolution(x1)) {
                    double sol2 = findSolutionOnRay(new Ray(x2, true));
                    int multiplicity1 = calculateRootMultiplicity(x1, x1, x2);
                    int multiplicity2 = calculateRootMultiplicity(sol2, x1, x2);
                    return new CubicSolution(x1, multiplicity1, sol2, multiplicity2, 0, 0, CubicSolutionState.TWO_SOLUTIONS);
                }
                if (isASolution(x2)) {
                    double sol2 = findSolutionOnRay(new Ray(x1, false));
                    int multiplicity1 = calculateRootMultiplicity(x2, x1, x2);
                    int multiplicity2 = calculateRootMultiplicity(sol2, x1, x2);
                    return new CubicSolution(x2, multiplicity1, sol2, multiplicity2, 0, 0, CubicSolutionState.TWO_SOLUTIONS);
                }
                if (y(x1) < 0) {
                    double sol = findSolutionOnRay(new Ray(x2, true));
                    int multiplicity = calculateRootMultiplicity(sol, 0, 0);
                    return new CubicSolution(sol, multiplicity, 0, 0, 0, 0, CubicSolutionState.ONE_SOLUTION);
                }
                if(y(x2) > 0) {
                    double sol = findSolutionOnRay(new Ray(x1, false));
                    int multiplicity = calculateRootMultiplicity(sol, 0, 0);
                    return new CubicSolution(sol, multiplicity, 0, 0, 0, 0, CubicSolutionState.ONE_SOLUTION);
                }
                double sol1 = findSolutionOnRay(new Ray(x1, false));
                double sol2 = findSolutionOnRay(new Ray(x2, true));
                double sol3 = findSolutionOnSegment(new Segment(x1, x2));

                int multiplicity1 = calculateRootMultiplicity(sol1, x1, x2);
                int multiplicity2 = calculateRootMultiplicity(sol2, x1, x2);
                int multiplicity3 = calculateRootMultiplicity(sol3, x1, x2);
                return new CubicSolution(sol1, multiplicity1, sol2, multiplicity2, sol3, multiplicity3, CubicSolutionState.THREE_SOLUTIONS);
            }
        }
        return null;
    }

    private int calculateRootMultiplicity(double root, double alpha, double beta) {
        double b = this.params.getA();
        double c = this.params.getB();
        double d = this.params.getC();

        if (Math.pow(Math.abs(b / 3), 2) == Math.abs(c / 3) && Math.abs((c / 3) * (b / 3)) == Math.abs(d)) {
            return 3;
        } else if (root == alpha || root == beta) {
            return 2; // Корень имеет кратность 2, если коэффициент при x^2 (b) близок к нулю
        } else {
            return 1; // Иначе корень имеет кратность 1
        }
    }

}
