package com.imkiva.flourine.core.geometric;

import com.imkiva.flourine.core.misc.Calculation;

import java.util.Objects;

/**
 * @author kiva
 * @date 2018/9/23
 */
public class Vector {
    private Point coordinate;

    public Vector(float x, float y, float z) {
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

    public void multiply(float number) {
        coordinate = new Point(number * getX(),
                number * getY(),
                number * getZ());
    }

    public float innerProduct(Vector other) {
        return getX() * other.getX()
                + getY() * other.getY()
                + getZ() * other.getZ();
    }

    public Vector crossProduct(Vector other) {
        float normalX = this.getY() * other.getZ()
                - this.getZ() * other.getY();
        float normalY = this.getX() * other.getZ()
                - this.getZ() * other.getX();
        float normalZ = this.getX() * other.getY()
                - this.getY() * other.getX();
        return new Vector(normalX, -normalY, normalZ);
    }

    public float cosineOf(Vector other) {
        return innerProduct(other) / (getModulus() * other.getModulus());
    }

    public float sineOf(Vector other) {
        return Calculation.sqrt(1 - Calculation.square(cosineOf(other)));
    }

    public boolean isParallelTo(Vector other) {
        return Double.compare(cosineOf(other), 0) == 0;
    }

    public boolean isPerpendicularTo(Vector other) {
        return Double.compare(innerProduct(other), 0) == 0;
    }

    public Vector reverse() {
        return new Vector(-getX(), -getY(), -getZ());
    }

    public float getX() {
        return coordinate.getX();
    }

    public float getY() {
        return coordinate.getY();
    }

    public float getZ() {
        return coordinate.getZ();
    }

    public float getModulus() {
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
