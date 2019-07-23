package com.imkiva.flourine.script;

import com.imkiva.flourine.script.antlr.FlourineScriptBaseVisitor;
import com.imkiva.flourine.script.antlr.FlourineScriptParser;
import com.imkiva.flourine.script.antlr.FlourineScriptParser.ExpressionContext;
import com.imkiva.flourine.script.file.SourceFile;
import com.imkiva.flourine.script.file.SourceFileFactory;
import com.imkiva.flourine.script.parser.Parser;
import com.imkiva.flourine.script.parser.ParserFactory;
import com.imkiva.flourine.script.runtime.Parameter;
import com.imkiva.flourine.script.types.Lambda;
import com.imkiva.flourine.script.types.ListValue;
import com.imkiva.flourine.script.types.PointValue;
import com.imkiva.flourine.script.types.Value;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


/**
 * @author kiva
 * @date 2019-07-23
 */
public class ParserTest {
    class TestLetVisitor extends FlourineScriptBaseVisitor {
        @Override
        public Object visitVariableDeclStatement(FlourineScriptParser.VariableDeclStatementContext ctx) {
            String varName = ctx.IDENTIFIER().getText();
            Value value = (Value) visitExpression(ctx.expression());
            System.out.println("变量名: " + varName + ", 值: " + value.getValue() + ", 类型: " + value.getType().getSimpleName());
            return super.visitVariableDeclStatement(ctx);
        }

        @Override
        public Object visitPrimaryPrefix(FlourineScriptParser.PrimaryPrefixContext ctx) {
            if (ctx.IDENTIFIER() != null) {
                // TODO: find variable
                return Value.of("@" + ctx.IDENTIFIER().getText());
            }
            return super.visitPrimaryPrefix(ctx);
        }

        @Override
        public Object visitLiteralExpression(FlourineScriptParser.LiteralExpressionContext ctx) {

            TerminalNode node = ((TerminalNode) ctx.children.get(0));
            switch (node.getSymbol().getType()) {
                case FlourineScriptParser.NumberLiteral:
                    return Value.of(Double.parseDouble(node.getText()));

                case FlourineScriptParser.StringLiteral:
                    return Value.of(node.getText().substring(1, node.getText().length() - 1));

                case FlourineScriptParser.CharacterLiteral:
                    return Value.of(node.getText().charAt(1));

                case FlourineScriptParser.BooleanLiteral:
                    return Value.of(Boolean.parseBoolean(node.getText()));

            }
            return super.visitLiteralExpression(ctx);
        }

        @Override
        public Object visitQuotedExpression(FlourineScriptParser.QuotedExpressionContext ctx) {
            return visitOrExpression(ctx.orExpression());
        }

        @Override
        public Value visitPointExpression(FlourineScriptParser.PointExpressionContext ctx) {
            List<ExpressionContext> exps = ctx.expression();
            if (exps.size() == 3) {
                return Value.of(new PointValue(exps.get(0), exps.get(1), exps.get(2)));
            }
            return Value.of(new PointValue(exps.get(0), exps.get(1), null));
        }

        @Override
        public Value visitListExpression(FlourineScriptParser.ListExpressionContext ctx) {
            return Value.of(new ListValue(ctx.expression()));
        }

        @Override
        public Value visitLambdaExpression(FlourineScriptParser.LambdaExpressionContext ctx) {
            FlourineScriptParser.ParameterListContext paramCtx = ctx.parameterList();
            return Value.of(new Lambda(visitParameterList(paramCtx), ctx.lambdaBody()));
        }

        @Override
        public List<Parameter> visitParameterList(FlourineScriptParser.ParameterListContext ctx) {
            List<Parameter> parameters = new ArrayList<>(ctx.IDENTIFIER().size());
            for (TerminalNode node : ctx.IDENTIFIER()) {
                parameters.add(new Parameter(node.getText()));
            }
            return parameters;
        }
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
                "let f = [](r) -> { r * 2 }" +
                "\n" +
                "let g = (0, 1, 0)" +
                "\n" +
                "let h = (0, 1)" +
                "\n" +
                "let i = {a, b, c, e}");
        Parser parser = ParserFactory.fromSourceFile(sourceFile);

        TestLetVisitor visitor = new TestLetVisitor();
        visitor.visit(parser.compilationUnit());
    }
}
