package com.imkiva.flourine.script.runtime;

import com.imkiva.flourine.script.antlr.FlourineScriptParser.LambdaBodyContext;
import com.imkiva.flourine.script.runtime.types.Lambda;
import com.imkiva.flourine.script.runtime.types.Value;
import com.imkiva.flourine.utils.FlourineStreams;
import com.imkiva.flourine.utils.Pair;

import java.util.Arrays;
import java.util.List;

/**
 * @author kiva
 * @date 2019-07-30
 */
public class LambdaInterpreter implements Lambda.Caller {
    private Interpreter.InterpreterVisitor visitor;
    private List<Parameter> parameterList;
    private LambdaBodyContext lambdaBodyContext;

    public LambdaInterpreter(Scope declaringScope) {
        Scope scope = new Scope(declaringScope);
        visitor = new Interpreter.InterpreterVisitor(scope);
    }

    public void bind(Lambda lambda) {
        parameterList = lambda.getParameters();
        lambdaBodyContext = lambda.getBody();
    }

    private void bindArguments(List<Value> args) {
        Scope scope = visitor.getScope();
        FlourineStreams.zip(parameterList, args, Pair::new)
                .forEach(scope::set);
    }

    @Override
    public Value call(Value[] args) {
        if (args.length != parameterList.size()) {
            throw new ScriptException("Argument size does not match");
        }

        bindArguments(Arrays.asList(args));
        return visitor.visitLambdaBody(lambdaBodyContext);
    }
}
