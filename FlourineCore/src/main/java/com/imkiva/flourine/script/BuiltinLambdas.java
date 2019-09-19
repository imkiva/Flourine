package com.imkiva.flourine.script;

import com.imkiva.flourine.script.builtin.MathLambdas;
import com.imkiva.flourine.script.builtin.StringLambdas;
import com.imkiva.flourine.script.builtin.SystemLambdas;

/**
 * @author kiva
 * @date 2019-07-30
 */
class BuiltinLambdas {
    static void loadInto(FlourineScriptRuntime runtime) {
        StringLambdas.loadInto(runtime);
        MathLambdas.loadInto(runtime);
        SystemLambdas.loadInto(runtime);
    }
}
