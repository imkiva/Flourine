package com.imkiva.flourine.script;

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
        runtime.defineLambda("print", P("object"),
                args -> args.stream()
                        .map(Objects::toString)
                        .collect(Collectors.joining(", ")));
    }
}
