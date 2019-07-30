package com.imkiva.flourine.script;

import com.imkiva.flourine.script.parser.file.SourceFile;
import com.imkiva.flourine.script.parser.file.SourceFileFactory;
import com.imkiva.flourine.script.runtime.Interpreter;
import com.imkiva.flourine.script.runtime.Scope;
import com.imkiva.flourine.script.runtime.SolveOutput;
import com.imkiva.flourine.script.runtime.types.*;

import java.util.List;
import java.util.function.Function;

/**
 * @author kiva
 * @date 2019-07-30
 */
public class FlourineScriptRuntime {
    private static final class GlobalScope extends Scope {
        GlobalScope() {
            super(null);
        }

        Scope newChildScope() {
            return new Scope(this);
        }
    }

    private GlobalScope globalScope = new GlobalScope();
    private SolveOutput solveOutput;

    public FlourineScriptRuntime() {
        BuiltinLambda.init(this);
    }

    public void setSolveOutput(SolveOutput solveOutput) {
        this.solveOutput = solveOutput;
    }

    public SolveOutput getSolveOutput() {
        return solveOutput;
    }

    public Interpreter newInterpreter() {
        return new Interpreter(globalScope.newChildScope(), solveOutput);
    }

    public void runFile(String file, String[] args) {
        // TODO: bind args
        SourceFile sourceFile = SourceFileFactory.fromFile(file);
        Interpreter interpreter = newInterpreter();
        interpreter.evaluate(sourceFile);
    }

    public void defineLambda(String name, String[] parameters, JavaLambda.Caller caller) {
        JavaLambda lambda = JavaLambda.from(caller, parameters);
        globalScope.set(name, Value.of(lambda));
    }

    public void defineVariable(String name, double value) {
        defineVariable(name, Value.of(value));
    }

    public void defineVariable(String name, char value) {
        defineVariable(name, Value.of(value));
    }

    public void defineVariable(String name, boolean value) {
        defineVariable(name, Value.of(value));
    }

    public void defineVariable(String name, String value) {
        defineVariable(name, Value.of(value));
    }

    public void defineVariable(String name, Lambda value) {
        defineVariable(name, Value.of(value));
    }

    public void defineVariable(String name, PointValue value) {
        defineVariable(name, Value.of(value));
    }

    public void defineVariable(String name, ListValue value) {
        defineVariable(name, Value.of(value));
    }

    public void defineVariable(String name, Value value) {
        globalScope.set(name, value);
    }
}
