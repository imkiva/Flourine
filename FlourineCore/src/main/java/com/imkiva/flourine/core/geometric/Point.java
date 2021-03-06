package com.imkiva.flourine.core.geometric;

import com.imkiva.flourine.core.misc.FastMath;

import java.util.Objects;

/**
 * @author kiva
 * @date 2018/9/23
 */
public class Point {
    public static final Point ORIGIN = new Point(0, 0, 0);

    private float x;
    private float y;
    private float z;

    public static Point of(float x, float y, float z) {
        return new Point(x, y, z);
    }

    public static Point of(Point point, Point delta) {
        return Point.of(point, delta.getX(), delta.getY(), delta.getZ());
    }

    public static Point of(Point point, float deltaX, float deltaY, float deltaZ) {
        return Point.of(point.x + deltaX, point.y + deltaY, point.z + deltaZ);
    }

    private Point(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float distance(Point another) {
        float tx = FastMath.square(getX() - another.getX());
        float ty = FastMath.square(getY() - another.getY());
        float tz = FastMath.square(getZ() - another.getZ());
        return FastMath.sqrt(tx + ty + tz);
    }

    public float distanceFromOrigin() {
        return distance(Point.ORIGIN);
    }

    public Vector toPositionVector() {
        return Vector.of(Point.ORIGIN, this);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    @Override
    public String toString() {
        return String.format("Point(%.2f, %.2f, %.2f)", getX(), getY(), getZ());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return FastMath.equals(point.getX(), getX()) &&
                FastMath.equals(point.getY(), getY()) &&
                FastMath.equals(point.getZ(), getZ());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY(), getZ());
    }
}
