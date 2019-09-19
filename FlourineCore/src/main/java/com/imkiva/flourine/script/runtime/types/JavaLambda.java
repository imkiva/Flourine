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
    public interface Body {
        Object call(List<Value> args);
    }

    public static JavaLambda from(Body body, String[] parameters) {
        return new JavaLambda(body, parameters);
    }

    private Body body;
    private List<Parameter> parameters;

    private JavaLambda(Body body, String[] parameters) {
        this.body = body;
        this.parameters = Arrays.stream(parameters).map(Parameter::new).collect(Collectors.toList());
    }

    @Override
    public List<Parameter> getParameters() {
        return parameters;
    }

    @Override
    public Value call(List<Value> args) {
        return Value.of(body.call(args));
    }
}
