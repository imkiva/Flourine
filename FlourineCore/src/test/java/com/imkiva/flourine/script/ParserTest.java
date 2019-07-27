package com.imkiva.flourine.script;

import com.imkiva.flourine.script.parser.file.SourceFile;
import com.imkiva.flourine.script.parser.file.SourceFileFactory;
import com.imkiva.flourine.script.runtime.Interpreter;
import com.imkiva.flourine.script.runtime.Scope;
import com.imkiva.flourine.script.runtime.types.ListValue;
import com.imkiva.flourine.script.runtime.types.Value;
import org.junit.Assert;
import org.junit.Test;


/**
 * @author kiva
 * @date 2019-07-23
 */
public class ParserTest {
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
                "let f = [](r) -> { r * 2 }" +
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
        ListValue list = (ListValue) value.getValue();
        System.out.println(list.toString());
    }
}
