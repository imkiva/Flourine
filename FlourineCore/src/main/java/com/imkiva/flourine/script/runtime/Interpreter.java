package com.imkiva.flourine.script.runtime;

import com.imkiva.flourine.script.antlr.FlourineScriptBaseVisitor;
import com.imkiva.flourine.script.antlr.FlourineScriptParser;
import com.imkiva.flourine.script.parser.Parser;
import com.imkiva.flourine.script.parser.ParserFactory;
import com.imkiva.flourine.script.parser.file.SourceFile;
import com.imkiva.flourine.script.runtime.types.Lambda;
import com.imkiva.flourine.script.runtime.types.ListValue;
import com.imkiva.flourine.script.runtime.types.PointValue;
import com.imkiva.flourine.script.runtime.types.Value;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kiva
 * @date 2019-07-26
 */
public class Interpreter {
    private static class InterpreterVisitor extends FlourineScriptBaseVisitor {
        private Scope scope;

        public InterpreterVisitor() {
            this.scope = new Scope(null);
        }

        public Scope getScope() {
            return scope;
        }

        @Override
        public Value visitExpression(FlourineScriptParser.ExpressionContext ctx) {
            return (Value) super.visitExpression(ctx);
        }

        @Override
        public Object visitVariableDeclStatement(FlourineScriptParser.VariableDeclStatementContext ctx) {
            String varName = ctx.IDENTIFIER().getText();
            Value value = visitExpression(ctx.expression());
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
            List<FlourineScriptParser.ExpressionContext> exps = ctx.expression();
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
            List<FlourineScriptParser.ExpressionContext> exps = ctx.expression();
            List<Value> values = new ArrayList<>(exps.size());
            for (FlourineScriptParser.ExpressionContext exp : exps) {
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

    private InterpreterVisitor visitor;

    public Interpreter() {
        this.visitor = new InterpreterVisitor();
    }

    public void evaluate(SourceFile sourceFile) {
        Parser parser = ParserFactory.fromSourceFile(sourceFile);
        visitor.visit(parser.compilationUnit());
    }

    public Scope getScope() {
        return visitor.getScope();
    }
}
