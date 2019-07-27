package com.imkiva.flourine.script;

import com.imkiva.flourine.script.antlr.FlourineScriptBaseVisitor;
import com.imkiva.flourine.script.antlr.FlourineScriptParser;
import com.imkiva.flourine.script.antlr.FlourineScriptParser.ExpressionContext;
import com.imkiva.flourine.script.parser.Parser;
import com.imkiva.flourine.script.parser.ParserFactory;
import com.imkiva.flourine.script.parser.file.SourceFile;
import com.imkiva.flourine.script.parser.file.SourceFileFactory;
import com.imkiva.flourine.script.runtime.Parameter;
import com.imkiva.flourine.script.runtime.Scope;
import com.imkiva.flourine.script.runtime.types.Lambda;
import com.imkiva.flourine.script.runtime.types.ListValue;
import com.imkiva.flourine.script.runtime.types.PointValue;
import com.imkiva.flourine.script.runtime.types.Value;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


/**
 * @author kiva
 * @date 2019-07-23
 */
public class ParserTest {
    static class TestLetVisitor extends FlourineScriptBaseVisitor {
        private Scope scope;

        public TestLetVisitor() {
            this.scope = new Scope(null);
        }

        public Scope getScope() {
            return scope;
        }

        @Override
        public Value visitExpression(ExpressionContext ctx) {
            return (Value) super.visitExpression(ctx);
        }

        @Override
        public Object visitVariableDeclStatement(FlourineScriptParser.VariableDeclStatementContext ctx) {
            String varName = ctx.IDENTIFIER().getText();
            Value value = visitExpression(ctx.expression());
            System.out.println("变量名: " + varName + ", 值: " + value.getValue() + ", 类型: " + value.getType().getSimpleName());
            System.out.println("");
            scope.set(varName, value);
            return super.visitVariableDeclStatement(ctx);
        }

        @Override
        public Object visitPrimaryPrefix(FlourineScriptParser.PrimaryPrefixContext ctx) {
            if (ctx.IDENTIFIER() != null) {
                // TODO: find variable
                return scope.find(ctx.IDENTIFIER().getText());
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
            Value x = visitExpression(exps.get(0));
            Value y = visitExpression(exps.get(1));
            if (exps.size() == 3) {
                Value z = visitExpression(exps.get(2));
                return Value.of(new PointValue(x, y, z));
            }
            return Value.of(new PointValue(x, y, null));
        }

        @Override
        public Value visitListExpression(FlourineScriptParser.ListExpressionContext ctx) {
            List<ExpressionContext> exps = ctx.expression();
            List<Value> values = new ArrayList<>(exps.size());
            for (ExpressionContext exp : exps) {
                values.add(visitExpression(exp));
            }
            return Value.of(new ListValue(values));
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
                "let i = {a, b, c, e}" +
                "\n" +
                "let j = {g, h, i}");
        Parser parser = ParserFactory.fromSourceFile(sourceFile);

        TestLetVisitor visitor = new TestLetVisitor();
        visitor.visit(parser.compilationUnit());

        Scope scope = visitor.getScope();
        Value value = scope.find("j");
        Assert.assertTrue(value.isList());
        ListValue list = (ListValue) value.getValue();
        System.out.println(list.toString());
    }
}
