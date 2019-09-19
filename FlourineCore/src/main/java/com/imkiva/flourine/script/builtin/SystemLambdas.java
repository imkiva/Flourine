package com.imkiva.flourine.script.builtin;

import com.imkiva.flourine.script.FlourineScriptRuntime;

/**
 * @author kiva
 * @date 2019-09-18
 */
public class SystemLambdas extends LambdaBase {
    public static void loadInto(FlourineScriptRuntime runtime) {
        runtime.defineLambda("exit", P("exitCode?"),
                args -> {
                    int exitCode = args.size() >= 1 ? (int) (double) args.get(0).cast() : 0;
                    System.exit(exitCode);
                    return null;
                });
    }
}
