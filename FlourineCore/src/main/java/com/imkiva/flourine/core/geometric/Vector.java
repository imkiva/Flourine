package com.imkiva.flourine.core.geometric;

import com.imkiva.flourine.core.misc.Calculation;

import java.util.Objects;

/**
 * @author kiva
 * @date 2018/9/23
 */
public class Vector {
    private Point coordinate;

    public Vector(double x, double y, double z) {
        coordinate = new Point(x, y, z);
    }

    public Vector(Point start, Point end) {
        coordinate = new Point(end, -start.getX(), -start.getY(), -start.getZ());
    }

    public void add(Vector other) {
        coordinate = new Point(coordinate, other.coordinate);
    }

    public void subtract(Vector other) {
        add(other.reverse());
    }

    public void multiply(double number) {
        coordinate = new Point(number * getX(),
                number * getY(),
                number * getZ());
    }

    public double innerProduct(Vector other) {
        return getX() * other.getX()
                + getY() * other.getY()
                + getZ() * other.getZ();
    }

    public double cosineOfAngel(Vector other) {
        return innerProduct(other) / (getModulus() * other.getModulus());
    }

    public double sineOfAngel(Vector other) {
        return Math.sqrt(1 - Calculation.square(cosineOfAngel(other)));
    }

    public boolean isParallelTo(Vector other) {
        return Calculation.realEqual(Math.abs(innerProduct(other)),
                Math.abs(getModulus() * other.getModulus()));
    }

    public boolean isPerpendicularTo(Vector other) {
        return Calculation.realEqual(innerProduct(other), 0);
    }

    public Vector reverse() {
        return new Vector(-getX(), -getY(), -getZ());
    }

    public double getX() {
        return coordinate.getX();
    }

    public double getY() {
        return coordinate.getY();
    }

    public double getZ() {
        return coordinate.getZ();
    }

    public double getModulus() {
        return coordinate.distanceFromOrigin();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return Objects.equals(coordinate, vector.coordinate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinate);
    }
}
