package com.imkiva.flourine.script.types;

import com.imkiva.flourine.script.antlr.FlourineScriptParser.ExpressionContext;

import java.util.HashMap;

/**
 * @author kiva
 * @date 2019-07-23
 */
public abstract class LazyValue {
    private HashMap<ExpressionContext, Value> cache = new HashMap<>();

    Value evaluate(ExpressionContext exp) {
        // TODO: eval
        return null;
    }

    boolean hasEvaluatedValue(ExpressionContext exp) {
        return cache.containsKey(exp);
    }
}
