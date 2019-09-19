package com.imkiva.flourine.script.builtin;

import com.imkiva.flourine.script.FlourineScriptRuntime;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author kiva
 * @date 2019-09-18
 */
public class StringLambdas extends LambdaBase {
    public static void loadInto(FlourineScriptRuntime runtime) {
        runtime.defineLambda("toString", P("object"),
                args -> args.stream()
                        .map(Objects::toString)
                        .collect(Collectors.joining(", ")));

        runtime.defineLambda("concat", P("object..."),
                args -> args.stream()
                        .map(Objects::toString)
                        .collect(Collectors.joining()));
    }
}
