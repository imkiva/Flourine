package com.imkiva.flourine.script.runtime.types;

import com.imkiva.flourine.script.runtime.Parameter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kiva
 * @date 2019-07-23
 */
public abstract class Lambda {
    public abstract List<Parameter> getParameters();

    public abstract Value call(List<Value> args);

    @Override
    public String toString() {
        return "Lambda with " + getParameters().size() + " argument(s): "
                + getParameters().stream()
                .map(Parameter::getName)
                .collect(Collectors.joining(", ", "(", ")"));
    }
}
