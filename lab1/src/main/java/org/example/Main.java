package org.example;

// x^3 + ax^2 + bx + c

import java.util.Scanner;

public class Main {
    private static Params readParams() {
        Scanner in = new Scanner(System.in);
        System.out.print("a = ");
        double a = in.nextDouble();
        System.out.print("b = ");
        double b = in.nextDouble();
        System.out.print("c = ");
        double c = in.nextDouble();
        System.out.print("use graphics? (y/n) ");
        String inp = in.next();
        boolean useGraphics = inp.equalsIgnoreCase("y");
        return new Params(a, b, c, useGraphics);
    }
    private static void printSolution(Solver.CubicSolution solution) {
        switch (solution.state()) {
            case ONE_SOLUTION -> {
                System.out.printf("one solution: x = %f: mult = %d%n", solution.x1(), solution.x1_mult());
            }
            case TWO_SOLUTIONS -> {
                System.out.printf("two solutions: x1 = %f: mult = %d; x2 = %f: mult = %d%n", solution.x1(), solution.x1_mult(), solution.x2(), solution.x2_mult());
            }
            case THREE_SOLUTIONS -> {
                System.out.printf("three solutions: x1 = %f: mult = %d; x2 = %f: mult = %d; x3 = %f: mult = %d%n", solution.x1(), solution.x1_mult(), solution.x2(), solution.x2_mult(), solution.x3(), solution.x3_mult());
            }
        }
    }
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Params params = readParams();
        Solver solver = new Solver(params);
        if (params.shouldUseGraphics()) {
            Graphics graphics = new Graphics(params);
            graphics.draw();
        }
        Solver.CubicSolution solution = solver.solve();
        printSolution(solution);
    }

}