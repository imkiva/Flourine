package com.imkiva.flourine.core.misc;

/**
 * @author kiva
 * @date 2018/9/23
 */
public class FastMath {
    public static final float FLOAT_TOLERANCE = 0.0001f;

    public static float square(float x) {
        return x * x;
    }

    public static float sqrt(float x) {
        return (float) Math.sqrt(x);
    }

    public static boolean equals(float a, float b) {
        return Math.abs(a - b) <= FLOAT_TOLERANCE;
    }
}
