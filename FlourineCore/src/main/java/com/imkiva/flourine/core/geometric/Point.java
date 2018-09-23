package com.imkiva.flourine.core.geometric;

import com.imkiva.flourine.core.misc.Calculation;

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

    public Point(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point(Point point, Point delta) {
        this(point, delta.getX(), delta.getY(), delta.getZ());
    }

    public Point(Point point, float deltaX, float deltaY, float deltaZ) {
        this(point.x + deltaX, point.y + deltaY, point.z + deltaZ);
    }

    public float distance(Point another) {
        float tx = Calculation.square(getX() - another.getX());
        float ty = Calculation.square(getY() - another.getY());
        float tz = Calculation.square(getZ() - another.getZ());
        return Calculation.sqrt(tx + ty + tz);
    }

    public float distanceFromOrigin() {
        return distance(Point.ORIGIN);
    }

    public Vector toPositionVector() {
        return new Vector(Point.ORIGIN, this);
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
        return Float.compare(point.getX(), getX()) == 0 &&
                Float.compare(point.getY(), getY()) == 0 &&
                Float.compare(point.getZ(), getZ()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY(), getZ());
    }
}
