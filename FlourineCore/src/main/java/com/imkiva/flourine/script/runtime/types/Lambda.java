package com.imkiva.flourine.script.runtime.types;

import com.imkiva.flourine.script.antlr.FlourineScriptParser.LambdaBodyContext;
import com.imkiva.flourine.script.runtime.Parameter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kiva
 * @date 2019-07-23
 */
public class Lambda {
    public interface Caller {
        Value call(List<Value> args);
    }

    private List<Parameter> parameters;
    private LambdaBodyContext lambdaBody;
    private Caller caller;

    public Lambda(List<Parameter> parameters, LambdaBodyContext lambdaBody, Caller caller) {
        this.parameters = parameters;
        this.lambdaBody = lambdaBody;
        this.caller = caller;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public LambdaBodyContext getBody() {
        return lambdaBody;
    }

    public Value call(List<Value> args) {
        return caller.call(args);
    }

    @Override
    public String toString() {
        return "Lambda with " + parameters.size() + " argument(s): "
                + parameters.stream()
                .map(Parameter::getName)
                .collect(Collectors.joining(", ", "(", ")"));
    }
}
