package com.imkiva.flourine.core.misc;

/**
 * @author kiva
 * @date 2018/9/23
 */
public class Calculation {
    private static final double PRECISION = Math.pow(10, -5);

    public static double square(double x) {
        return x * x;
    }

    public static boolean realEqual(double a, double b) {
        return Math.abs(a - b) <= PRECISION;
    }
}
