package com.imkiva.flourine.script.runtime.types;

import com.imkiva.flourine.script.antlr.FlourineScriptParser;
import com.imkiva.flourine.script.runtime.Argument;
import com.imkiva.flourine.script.runtime.Parameter;

import java.util.List;

/**
 * @author kiva
 * @date 2019-07-30
 */
public class ScriptLambda extends Lambda {
    public interface Caller {
        Value call(List<Argument> args);
    }

    private List<Parameter> parameters;
    private FlourineScriptParser.LambdaBodyContext lambdaBody;
    private Caller caller;

    public ScriptLambda(List<Parameter> parameters, FlourineScriptParser.LambdaBodyContext lambdaBody, Caller caller) {
        this.parameters = parameters;
        this.lambdaBody = lambdaBody;
        this.caller = caller;
    }

    @Override
    public List<Parameter> getParameters() {
        return parameters;
    }

    @Override
    public Value call(List<Argument> args) {
        return caller.call(args);
    }

    public FlourineScriptParser.LambdaBodyContext getBody() {
        return lambdaBody;
    }
}
