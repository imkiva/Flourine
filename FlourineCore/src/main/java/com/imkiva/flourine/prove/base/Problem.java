package com.imkiva.flourine.prove.base;

import com.imkiva.flourine.prove.solution.EqualitySolution;
import com.imkiva.flourine.script.runtime.Scope;

import java.util.List;

/**
 * @author kiva
 * @date 2019-08-03
 */
public class Problem {
    public enum ProblemType {
        EQUALITY, COMPARISION
    }

    private ProblemType type;
    private List<String> parameters;

    public Problem(ProblemType type, List<String> parameters) {
        this.type = type;
        this.parameters = parameters;
    }

    public Solution solve(Scope scope) {
        switch (type) {
            case EQUALITY:
                return new EqualitySolution(scope, parameters.get(0), parameters.get(1));
            case COMPARISION:
                break;
        }
        throw new UnsupportedOperationException();
    }
}
