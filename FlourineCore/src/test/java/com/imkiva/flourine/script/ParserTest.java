package com.imkiva.flourine.script;

import com.imkiva.flourine.script.parser.file.SourceFile;
import com.imkiva.flourine.script.parser.file.SourceFileFactory;
import com.imkiva.flourine.script.runtime.Interpreter;
import com.imkiva.flourine.script.runtime.Scope;
import com.imkiva.flourine.script.runtime.ScriptException;
import com.imkiva.flourine.script.runtime.types.ListValue;
import com.imkiva.flourine.script.runtime.types.Value;
import org.junit.Assert;
import org.junit.Test;


/**
 * @author kiva
 * @date 2019-07-23
 */
public class ParserTest {
    public void testExp(String exp, Value expected) {
        SourceFile sourceFile = SourceFileFactory.fromCode("let t = " + exp);
        Interpreter interpreter = new Interpreter();
        interpreter.evaluate(sourceFile);

        Scope scope = interpreter.getScope();
        Value t = scope.find("t");
        Assert.assertEquals(t, expected);
    }

    @Test
    public void testLet() {
        SourceFile sourceFile = SourceFileFactory.fromCode("let a = 100" +
                "\n" +
                "let b = \"hello world\"" +
                "\n" +
                "let c = (true)" +
                "\n" +
                "let d = a" +
                "\n" +
                "let e = 'A'" +
                "\n" +
                "let f = [](a, b, c) -> { a * b * c }" +
                "\n" +
                "let g = (0, 1, 0)" +
                "\n" +
                "let h = (0, 1)" +
                "\n" +
                "let i = {a, b, c, e}" +
                "\n" +
                "let j = {g, h, i}");

        Interpreter interpreter = new Interpreter();
        interpreter.evaluate(sourceFile);

        Scope scope = interpreter.getScope();
        Value value = scope.find("j");
        Assert.assertTrue(value.isList());
        System.out.println(value.toString());

        value = scope.find("f");
        Assert.assertTrue(value.isLambda());
        System.out.println(value.toString());
    }

    @Test
    public void testUnaryExpression() {
        testExp("10", Value.of(10));
        testExp("+500", Value.of(500));
        testExp("-1", Value.of(-1));
        testExp("!true", Value.of(false));
        testExp("!false", Value.of(true));
    }

    @Test
    public void testMultiplicativeExpression() {
        testExp("10 * 20", Value.of(200));
        testExp("10 / 20", Value.of(0.5));
        testExp("20 % 10", Value.of(0));
        testExp("20 % 6", Value.of(2));
        testExp("2 ^ 5", Value.of(32));
    }

    @Test(expected = ScriptException.class)
    public void testMultiplicativeExpressionEx1() {
        testExp("10 / 0", Value.of(false));
    }

    @Test(expected = ScriptException.class)
    public void testMultiplicativeExpressionEx2() {
        testExp("-1 ^ 0.5", Value.of(false));
    }

    @Test
    public void testAdditiveExpression() {
        testExp("10 * 20 + 10", Value.of(210));
        testExp("10 + 10 / 20", Value.of(10.5));
        testExp("10 + 20 % 10", Value.of(10));
        testExp("20 % 6 + 10", Value.of(12));
        testExp("2 ^ 5 + 10", Value.of(42));
    }

    @Test
    public void testRelationalExpression() {
        Value t = Value.of(true);
        Value f = Value.of(false);
        testExp("100 == 100", t);
        testExp("-1.5 == -1.5", t);
        testExp("51.1 == 51.1", t);
        testExp("10.5 > 10.4", t);
        testExp("10.5 < 10.6", t);
        testExp("1 > -1", t);
        testExp("-10.111 < -5.999", t);
        testExp("+0 == -0", t);
        testExp("+0 != -0", f);
        testExp("true == true", t);
        testExp("true == false", f);
        testExp("\"hello\" == \"hello\"", t);
        testExp("\"hello\" != \"hello\"", f);
        testExp("\"hello\" == \"world\"", f);
        testExp("\"hello\" != \"world\"", t);
        testExp("'A' == 'a'", f);
        testExp("'A' != 'a'", t);
        testExp("'a' != 'a'", f);
        testExp("'a' == 'a'", t);
    }

    @Test(expected = ScriptException.class)
    public void testRelationalExpressionEx1() {
        Value t = Value.of(true);
        Value f = Value.of(false);
        testExp("true == 1", t);
    }

    @Test(expected = ScriptException.class)
    public void testRelationalExpressionEx2() {
        Value t = Value.of(true);
        Value f = Value.of(false);
        testExp("true > 4", t);
    }

    @Test
    public void testConditionalLogicExpression() {
        Value t = Value.of(true);
        Value f = Value.of(false);
        testExp("0 == 0 && 1 == 1 && 2 == 2", t);
        testExp("1 > 0 || 0 > -10", t);
        testExp("1 > 0 || 0 < -10", t);
        testExp("1 < 0 || 0 > -10", t);
        testExp("1 < 0 || 0 < -10", f);

        testExp("1 > 0 || 10", t);
        testExp("1 < 0 && 10", f);
        testExp("true || 1", t);
    }

    @Test(expected = ScriptException.class)
    public void testConditionalLogicExpressionEx1() {
        Value t = Value.of(true);
        Value f = Value.of(false);
        testExp("1 && 2", f);
    }

    @Test
    public void testConditionalExpression() {
        testExp("(0 == 0 && 1 == 1 && 2 == 2) ? 10 : 5", Value.of(10));
        testExp("(0 != 0 && 1 == 1 && 2 == 2) ? 10 : 5", Value.of(5));

        testExp("1 > 0 || 10 ? 1 : 0", Value.of(1));
        testExp("1 < 0 && 10 ? 1 : 0", Value.of(0));

        testExp("(1 > 0 || 10) ? 1 : 0", Value.of(1));
        testExp("(1 < 0 && 10) ? 1 : 0", Value.of(0));
    }

    @Test(expected = ScriptException.class)
    public void testConditionalExpressionEx1() {
        testExp("1 ? 1 : 0", Value.of(0));
    }
}
