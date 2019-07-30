package com.imkiva.flourine.script.runtime.types;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author kiva
 * @date 2019-07-23
 */
public class PointValue {
    private Value x;
    private Value y;
    private Value z;

    public PointValue(Value x, Value y, Value z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Value getX() {
        return x;
    }

    public Value getY() {
        return y;
    }

    public Value getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "(" +
                Stream.of(x, y, z)
                        .filter(Objects::nonNull)
                        .map(Object::toString)
                        .collect(Collectors.joining(", ")) +
                ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PointValue that = (PointValue) o;
        return Objects.equals(getX(), that.getX()) &&
                Objects.equals(getY(), that.getY()) &&
                Objects.equals(getZ(), that.getZ());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY(), getZ());
    }
}
