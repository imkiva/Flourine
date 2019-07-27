package com.imkiva.flourine.script.runtime;

import com.imkiva.flourine.script.antlr.FlourineScriptParser.ExpressionContext;
import com.imkiva.flourine.script.runtime.types.Value;

import java.util.HashMap;

/**
 * @author kiva
 * @date 2019-07-23
 */
public class Scope {
    private Scope parent;
    private HashMap<String, Value> variables;
    private HashMap<ExpressionContext, Value> variableCache = new HashMap<>();

    public Scope(Scope parent) {
        this.parent = parent;
        this.variables = new HashMap<>(4);
    }

    public Scope getParent() {
        return parent;
    }

    public void set(String name, Value v) {
        variables.put(name, v);
    }

    public Value find(String name) {
        Value v = variables.get(name);

        return v != null ? v : findInParent(name);
    }

    public HashMap<ExpressionContext, Value> getVariableCache() {
        return variableCache;
    }

    private Value findInParent(String name) {
        return getParent() == null ? null : getParent().find(name);
    }
}
