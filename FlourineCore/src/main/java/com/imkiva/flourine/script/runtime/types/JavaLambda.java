package com.imkiva.flourine.script.runtime.types;

import com.imkiva.flourine.script.runtime.Parameter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kiva
 * @date 2019-07-30
 */
public class JavaLambda extends Lambda {
    public interface Caller {
        Object call(List<Value> args);
    }

    public static JavaLambda from(Caller caller, String[] parameters) {
        return new JavaLambda(caller, parameters);
    }

    private Caller caller;
    private List<Parameter> parameters;

    private JavaLambda(Caller caller, String[] parameters) {
        this.caller = caller;
        this.parameters = Arrays.stream(parameters).map(Parameter::new).collect(Collectors.toList());
    }

    @Override
    public List<Parameter> getParameters() {
        return parameters;
    }

    @Override
    public Value call(List<Value> args) {
        return Value.of(caller.call(args));
    }
}
