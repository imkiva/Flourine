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

        return v == null ? (parent == null ? null : parent.find(name)) : v;
    }
}
