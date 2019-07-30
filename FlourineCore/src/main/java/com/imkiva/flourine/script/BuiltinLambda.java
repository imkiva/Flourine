package com.imkiva.flourine.script;

import com.imkiva.flourine.script.runtime.types.PointValue;
import com.imkiva.flourine.utils.FlourineStreams;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author kiva
 * @date 2019-07-30
 */
public class BuiltinLambda {
    private static String[] P(String... params) {
        return params;
    }

    static void init(FlourineScriptRuntime runtime) {
        runtime.defineLambda("toString", P("object"),
                args -> args.stream()
                        .map(Objects::toString)
                        .collect(Collectors.joining(", ")));

        runtime.defineLambda("concat", P("object..."),
                args -> args.stream()
                        .map(Objects::toString)
                        .collect(Collectors.joining()));

        runtime.defineLambda("distance", P("point1", "point2"),
                args -> {
                    PointValue one = args.get(0).cast();
                    PointValue two = args.get(1).cast();
                    return FlourineStreams.zip(one.coordinates(), two.coordinates(), (a, b) -> Math.pow(a - b, 2))
                            .reduce(Double::sum)
                            .orElse(0.0);
                });
    }
}
