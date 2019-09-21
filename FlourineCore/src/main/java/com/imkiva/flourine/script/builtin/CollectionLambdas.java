package com.imkiva.flourine.script.builtin;

import com.imkiva.flourine.script.FlourineScriptRuntime;
import com.imkiva.flourine.script.runtime.ScriptException;
import com.imkiva.flourine.script.runtime.types.ListValue;

import java.util.ArrayList;

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

        runtime.defineLambda("listAdd", P("list", "element"),
                args -> {
                    ListValue result = new ListValue(new ArrayList<>());
                    if (args.get(0).isList()) {
                        ListValue listValue = args.get(0).cast();
                        result.addAll(listValue);
                        result.add(args.get(1));
                    } else {
                        ListValue listValue = args.get(1).cast();
                        result.add(args.get(0));
                        result.addAll(listValue.getItems());
                    }
                    return result;
                });
    }
}
