package com.imkiva.flourine.core.geometric;

import com.imkiva.flourine.core.misc.Calculation;

import java.util.Objects;

/**
 * @author kiva
 * @date 2018/9/23
 */
public class Point {
    public static final Point ORIGIN = new Point(0, 0, 0);

    private double x;
    private double y;
    private double z;

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point(Point point, Point delta) {
        this(point, delta.getX(), delta.getY(), delta.getZ());
    }

    public Point(Point point, double deltaX, double deltaY, double deltaZ) {
        this(point.x + deltaX, point.y + deltaY, point.z + deltaZ);
    }

    public double distance(Point another) {
        double tx = Calculation.square(getX() - another.getX());
        double ty = Calculation.square(getY() - another.getY());
        double tz = Calculation.square(getZ() - another.getZ());
        return Math.sqrt(tx + ty + tz);
    }

    public double distanceFromOrigin() {
        return distance(Point.ORIGIN);
    }

    public Vector toPositionVector() {
        return new Vector(Point.ORIGIN, this);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
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
        return Double.compare(point.getX(), getX()) == 0 &&
                Double.compare(point.getY(), getY()) == 0 &&
                Double.compare(point.getZ(), getZ()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY(), getZ());
    }
}
