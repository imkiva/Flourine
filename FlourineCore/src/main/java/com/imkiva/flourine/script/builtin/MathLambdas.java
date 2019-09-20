package com.imkiva.flourine.script.builtin;

import com.imkiva.flourine.core.algebra.Matrix;
import com.imkiva.flourine.script.FlourineScriptRuntime;
import com.imkiva.flourine.script.runtime.types.ListValue;
import com.imkiva.flourine.script.runtime.types.PointValue;
import com.imkiva.flourine.utils.FlourineStreams;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kiva
 * @date 2019-09-18
 */
public class MathLambdas extends LambdaBase {
    public static void loadInto(FlourineScriptRuntime runtime) {
        runtime.defineVariable("pi", Math.PI);

        runtime.defineVariable("e", Math.E);

        runtime.defineLambda("distance", P("point1", "point2"),
                args -> {
                    PointValue one = args.get(0).cast();
                    PointValue two = args.get(1).cast();
                    return FlourineStreams.zip(one.coordinates(), two.coordinates(), (a, b) -> Math.pow(a - b, 2))
                            .reduce(Double::sum)
                            .orElse(0.0);
                });

        runtime.defineLambda("Matrix", P("row..."),
                args -> {
                    List<ListValue> rowList = args.stream()
                            .map(a -> (ListValue) a.cast())
                            .collect(Collectors.toList());

                    if (rowList.size() == 0) {
                        return Matrix.ZERO;
                    }

                    int rows = rowList.size();
                    int columns = rowList.get(0).size();

                    for (int i = 1; i < rows; i++) {
                        if (rowList.get(i).size() != columns) {
                            return Matrix.ZERO;
                        }
                    }

                    Matrix matrix = new Matrix(rows, columns);
                    for (int i = 0; i < rows; i++) {
                        ListValue oneRow = rowList.get(i);
                        for (int j = 0; j < columns; j++) {
                            matrix.set(i, j, oneRow.getItem(j).cast());
                        }
                    }

                    return matrix;
                });

        runtime.defineLambda("MatrixMultiply", P("lhs", "rhs"),
                args -> args.stream()
                        .map(t -> (Matrix) t.cast())
                        .reduce(Matrix.UNIT, Matrix::multiply));
    }
}
