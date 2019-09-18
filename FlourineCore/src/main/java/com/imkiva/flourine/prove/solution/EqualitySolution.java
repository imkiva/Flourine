package com.imkiva.flourine.prove.solution;

import com.imkiva.flourine.prove.base.Solution;
import com.imkiva.flourine.prove.procedure.CompareProcedure;
import com.imkiva.flourine.prove.procedure.FindProcedure;
import com.imkiva.flourine.script.runtime.Scope;
import com.imkiva.flourine.script.runtime.types.Value;

/**
 * @author kiva
 * @date 2019-08-03
 */
public class EqualitySolution extends Solution {
    public EqualitySolution(Scope scope, String one, String two) {
        super(scope);
        FindProcedure p1 = procedure(new FindProcedure(getDraftPaper(), Value.of(one)));
        FindProcedure p2 = procedure(new FindProcedure(getDraftPaper(), Value.of(two)));
        procedure(new CompareProcedure(p1.getResult(), p2.getResult()));
    }
}
