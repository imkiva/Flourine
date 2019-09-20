package com.imkiva.flourine.script.runtime;

/**
 * @author kiva
 * @date 2019-07-23
 */
public class Parameter {
    public static final Parameter VARARGS = new Parameter("<varargs>");

    private String name;

    public Parameter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
