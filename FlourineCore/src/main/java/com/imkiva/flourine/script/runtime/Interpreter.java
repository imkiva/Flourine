package com.imkiva.flourine.script.runtime;

import com.imkiva.flourine.script.antlr.FlourineScriptParser.CompilationUnitContext;

/**
 * @author kiva
 * @date 2019-07-26
 */
public class Interpreter {
    private Scope currentScope;

    public Interpreter() {
        this.currentScope = new Scope(null);
    }

    public void evaluate(CompilationUnitContext unit) {
    }
}
