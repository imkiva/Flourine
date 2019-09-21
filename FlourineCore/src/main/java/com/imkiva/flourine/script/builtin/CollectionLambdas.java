package com.imkiva.flourine.script.builtin;

import com.imkiva.flourine.script.FlourineScriptRuntime;
import com.imkiva.flourine.script.runtime.ScriptException;
import com.imkiva.flourine.script.runtime.types.ListValue;

/**
 * @author kiva
 * @date 2019-09-21
 */
public class CollectionLambdas extends LambdaBase {
    public static void loadInto(FlourineScriptRuntime runtime) {
        runtime.defineLambda("listOf", P("element..."), ListValue::new);
        runtime.defineLambda("listHead", P("list"),
                args -> {
                    ListValue listValue = args.get(0).cast();
                    if (listValue.size() == 0) {
                        throw new ScriptException("Call listHead with empty list");
                    }
                    return listValue.getItem(0);
                });
        runtime.defineLambda("listTail", P("list"),
                args -> {
                    ListValue listValue = args.get(0).cast();
                    if (listValue.size() == 0) {
                        throw new ScriptException("Call listTail with empty list");
                    }
                    return listValue.getItem(listValue.size() - 1);
                });
        runtime.defineLambda("listSize", P("list"),
                args -> {
                    ListValue listValue = args.get(0).cast();
                    return listValue.size();
                });
    }
}
