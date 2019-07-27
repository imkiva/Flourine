package com.imkiva.flourine.script.runtime.types;

import com.imkiva.flourine.script.antlr.FlourineScriptParser;
import com.imkiva.flourine.script.runtime.Parameter;

import java.util.List;

/**
 * @author kiva
 * @date 2019-07-23
 */
public class Lambda {
    private List<Parameter> parameters;
    FlourineScriptParser.LambdaBodyContext lambdaBody;

    public Lambda(List<Parameter> parameters, FlourineScriptParser.LambdaBodyContext lambdaBody) {
        this.parameters = parameters;
        this.lambdaBody = lambdaBody;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return "Lambda with " + parameters.size() + " argument(s)";
    }
}
