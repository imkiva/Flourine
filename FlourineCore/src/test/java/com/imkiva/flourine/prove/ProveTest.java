package com.imkiva.flourine.prove;

import com.imkiva.flourine.prove.base.Problem;
import com.imkiva.flourine.prove.base.Procedure;
import com.imkiva.flourine.prove.base.Solution;
import com.imkiva.flourine.script.FlourineScriptRuntime;
import com.imkiva.flourine.script.parser.file.SourceFile;
import com.imkiva.flourine.script.parser.file.SourceFileFactory;
import com.imkiva.flourine.script.runtime.Interpreter;
import com.imkiva.flourine.script.runtime.Scope;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author kiva
 * @date 2019-08-03
 */
public class ProveTest {
    public Scope eval(String code) {
        FlourineScriptRuntime runtime = new FlourineScriptRuntime();
        SourceFile sourceFile = SourceFileFactory.fromCode(code);
        Interpreter interpreter = runtime.newInterpreter();
        interpreter.evaluate(sourceFile);

        return interpreter.getScope();
    }

    @Test
    public void test() {
        Scope scope = eval("let a = 100\nlet b = 10 * 10\nlet c = b");
        Problem problem = new Problem(Problem.ProblemType.EQUALITY, Arrays.asList("a", "c"));
        Solution solution = problem.solve(scope);
        for (Procedure p : solution.getProcedures()) {
            System.out.println(p.toString());
        }
    }
}
