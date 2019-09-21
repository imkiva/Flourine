package com.imkiva.flourine.script.builtin;

import com.imkiva.flourine.script.FlourineScriptRuntime;
import com.imkiva.flourine.script.runtime.types.ListValue;

/**
 * @author kiva
 * @date 2019-09-21
 */
public class CollectionLambdas extends LambdaBase {
    public static void loadInto(FlourineScriptRuntime runtime) {
        runtime.defineLambda("listOf", P("element..."), ListValue::new);
    }
}
