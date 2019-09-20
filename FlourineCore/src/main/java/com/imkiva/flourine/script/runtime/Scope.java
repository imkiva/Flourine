package com.imkiva.flourine.script.runtime;

import com.imkiva.flourine.script.runtime.types.Value;
import com.imkiva.flourine.utils.Pair;

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

    public void set(Parameter parameter, Argument argument) {
        set(parameter, argument.getArgumentValue());
    }

    public void set(Parameter parameter, Value argument) {
        set(parameter.getName(), argument);
    }

    public void set(Pair<Parameter, Argument> pair) {
        set(pair.getFirst(), pair.getSecond());
    }

    public Value find(String name) {
        Value v = variables.get(name);
        return v == null ? (parent == null ? null : parent.find(name)) : v;
    }
}
