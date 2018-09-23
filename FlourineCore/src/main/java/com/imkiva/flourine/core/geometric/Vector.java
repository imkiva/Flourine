package com.imkiva.flourine.core.geometric;

import com.imkiva.flourine.core.misc.FastMath;

import java.util.Objects;

/**
 * @author kiva
 * @date 2018/9/23
 */
public class Vector {
    private Point coordinate;

    public static Vector of(float x, float y, float z) {
        return new Vector(x, y, z);
    }

    public static Vector of(Point start, Point end) {
        return new Vector(start, end);
    }

    private Vector(float x, float y, float z) {
        coordinate = Point.of(x, y, z);
    }

    private Vector(Point start, Point end) {
        coordinate = Point.of(end, -start.getX(), -start.getY(), -start.getZ());
    }

    public Vector add(Vector other) {
        return Point.of(coordinate, other.coordinate).toPositionVector();
    }

    public Vector subtract(Vector other) {
        return add(other.reverse());
    }

    public Vector multiply(float number) {
        return Point.of(number * getX(),
                number * getY(),
                number * getZ())
                .toPositionVector();
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
        return FastMath.sqrt(1 - FastMath.square(cosineOf(other)));
    }

    public boolean isParallelTo(Vector other) {
        return FastMath.equals(cosineOf(other), 1);
    }

    public boolean isPerpendicularTo(Vector other) {
        return FastMath.equals(innerProduct(other), 0);
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

    @Override
    public String toString() {
        return String.format("Vector(%.2f, %.2f, %.2f)",
                getX(), getY(), getZ());
    }
}
