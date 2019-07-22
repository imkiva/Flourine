package com.imkiva.flourine.script;

import com.imkiva.flourine.script.antlr.FlourineScriptBaseVisitor;
import com.imkiva.flourine.script.antlr.FlourineScriptParser;
import com.imkiva.flourine.script.file.SourceFile;
import com.imkiva.flourine.script.file.SourceFileFactory;
import com.imkiva.flourine.script.parser.Parser;
import com.imkiva.flourine.script.parser.ParserFactory;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.junit.Test;


/**
 * @author kiva
 * @date 2019-07-23
 */
public class ParserTest {
    class TestLetVisitor extends FlourineScriptBaseVisitor {
        @Override
        public Object visitVariableDeclStatement(FlourineScriptParser.VariableDeclStatementContext ctx) {
            String varName = ctx.IDENTIFIER().getText();
            Object value = visit(ctx.expression());
            System.out.println("变量名: " + varName + ", 值: " + value + ", 类型: " + value.getClass().getSimpleName());
            return super.visitVariableDeclStatement(ctx);
        }

        @Override
        public Object visitLiteralExpression(FlourineScriptParser.LiteralExpressionContext ctx) {

            TerminalNode node = ((TerminalNode) ctx.children.get(0));
            switch (node.getSymbol().getType()) {
                case FlourineScriptParser.NumberLiteral:
                    return Integer.parseInt(node.getText());

                case FlourineScriptParser.StringLiteral:
                    return node.getText().substring(1, node.getText().length() - 1);

                case FlourineScriptParser.CharacterLiteral:
                    return node.getText().charAt(1);

                case FlourineScriptParser.BooleanLiteral:
                    return Boolean.parseBoolean(node.getText());

            }
            return super.visitLiteralExpression(ctx);
        }
    }

    @Test
    public void testLet() {
        SourceFile sourceFile = SourceFileFactory.fromCode("let a = 100" +
                "\n" +
                "let b = \"hello world\"" +
                "\n" +
                "let c = true" +
                "\n" +
                "let d = false" +
                "\n" +
                "let e = 'A'");
        Parser parser = ParserFactory.fromSourceFile(sourceFile);

        TestLetVisitor visitor = new TestLetVisitor();
        visitor.visit(parser.compilationUnit());
    }
}
