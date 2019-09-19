package com.imkiva.flourine.core.algebra;

import java.util.Arrays;
import java.util.function.BiFunction;

/**
 * @author kiva
 * @date 2019-09-09
 */
public class Matrix {
    private int row;
    private int column;

    private double[][] data;

    public static Matrix ZERO = Matrix.zero(0);

    public static Matrix zero(Matrix target) {
        if (target == null) {
            throw new IllegalArgumentException();
        }
        return zero(target.getRow(), target.getColumn());
    }

    public static Matrix zero(int n) {
        return zero(n, n);
    }

    public static Matrix zero(int row, int column) {
        return new Matrix(row, column);
    }

    public Matrix(int row, int column) {
        this.row = row;
        this.column = column;
        setZero(row, column);
    }

    public Matrix(double[]... rowData) {
        if (rowData.length == 0) {
            throw new IllegalArgumentException("A matrix should has 1 row at least");
        }

        int column = rowData[0].length;
        for (double[] oneRow : rowData) {
            if (oneRow.length != column) {
                throw new IllegalArgumentException("A matrix should has the same column");
            }
        }

        this.row = rowData.length;
        this.column = rowData[0].length;
        data = rowData;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void set(int row, int column, double element) {
        data[row][column] = element;
    }

    public double get(int row, int column) {
        return data[row][column];
    }

    public Matrix add(Matrix other) {
        if (other == ZERO) {
            return this.slowCopy();
        }

        if (!allowLinearCalculationWith(other)) {
            throw new IllegalArgumentException();
        }

        return linearOperation((i, j) -> get(i, j) + other.get(i, j));
    }

    public Matrix subtract(Matrix other) {
        if (other == ZERO) {
            return this.slowCopy();
        }

        if (!allowLinearCalculationWith(other)) {
            throw new IllegalArgumentException();
        }

        return linearOperation((i, j) -> get(i, j) - other.get(i, j));
    }

    public Matrix multiply(double number) {
        return linearOperation((i, j) -> get(i, j) * number);
    }

    public Matrix negate() {
        return linearOperation((i, j) -> -get(i, j));
    }

    public Matrix multiply(Matrix other) {
        if (other == ZERO) {
            return zero(this);
        }

        if (!allowMultiplyWith(other)) {
            throw new IllegalArgumentException();
        }

        Matrix matrix = new Matrix(getRow(), other.getColumn());

        for (int i = 0; i < matrix.getRow(); ++i) {
            for (int j = 0; j < matrix.getColumn(); ++j) {
                matrix.set(i, j, matrixMultiply(this, other, i, j, matrix.getRow()));
            }
        }
        return matrix;
    }

    private double matrixMultiply(Matrix lhs, Matrix rhs, int i, int j, int p) {
        double sum = 0;
        for (int k = 0; k < p; ++k) {
            sum += lhs.get(i, k) * rhs.get(k, j);
        }
        return sum;
    }

    private Matrix linearOperation(BiFunction<Integer, Integer, Double> operator) {
        Matrix matrix = new Matrix(getRow(), getColumn());
        for (int i = 0; i < getRow(); ++i) {
            for (int j = 0; j < getColumn(); ++j) {
                matrix.set(i, j, operator.apply(i, j));
            }
        }
        return matrix;
    }

    private Matrix slowCopy() {
        Matrix matrix = new Matrix(getRow(), getColumn());
        for (int i = 0; i < getRow(); i++) {
            for (int j = 0; j < getColumn(); j++) {
                matrix.set(i, j, get(i, j));
            }
        }
        return matrix;
    }

    private boolean allowLinearCalculationWith(Matrix other) {
        if (other == null) {
            throw new NullPointerException();
        }
        return getRow() == other.getRow() && getColumn() == other.getColumn();
    }

    private boolean allowMultiplyWith(Matrix other) {
        if (other == null) {
            throw new NullPointerException();
        }
        return getColumn() == other.getRow();
    }

    private void setZero(int row, int column) {
        if (data == null || getRow() != row || getColumn() != column) {
            data = new double[row][column];
            return;
        }

        Arrays.stream(data).forEach(rows -> Arrays.fill(rows, 0.0));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < getRow(); ++i) {
            builder.append("[");
            for (int j = 0; j < getColumn(); ++j) {
                builder.append(String.format("%6.3f ", get(i, j)));
            }
            builder.deleteCharAt(builder.length() - 1);
            builder.append("]\n");
        }
        return builder.toString();
    }
}
