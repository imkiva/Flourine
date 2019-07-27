package com.imkiva.flourine.script.runtime;

import com.imkiva.flourine.script.antlr.FlourineScriptBaseVisitor;
import com.imkiva.flourine.script.antlr.FlourineScriptParser;
import com.imkiva.flourine.script.antlr.FlourineScriptParser.*;
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

        InterpreterVisitor() {
            this.scope = new Scope(null);
        }

        Scope getScope() {
            return scope;
        }

        @Override
        public Object visitVariableDeclStatement(FlourineScriptParser.VariableDeclStatementContext ctx) {
            String varName = ctx.IDENTIFIER().getText();
            Value value = visitExpression(ctx.expression());
            scope.set(varName, value);
            return super.visitVariableDeclStatement(ctx);
        }

        /* expression begin */
        @Override
        public Value visitExpression(FlourineScriptParser.ExpressionContext ctx) {
            return visitConditionalExpression(ctx.conditionalExpression());
        }

        @Override
        public Value visitConditionalExpression(FlourineScriptParser.ConditionalExpressionContext ctx) {
            Value left = visitConditionalLogicExpression(ctx.conditionalLogicExpression());

            if (ctx.QUESTION() != null) {
                if (!left.isBool()) {
                    throw new ScriptException("A bool value is required before '?' and ':'");
                }

                boolean r = ((boolean) left.getValue());
                left = r ? visitExpression(ctx.expression(0)) : visitExpression(ctx.expression(1));
            }
            return left;
        }

        @Override
        public Value visitConditionalLogicExpression(FlourineScriptParser.ConditionalLogicExpressionContext ctx) {
            Value left = visitRelationalExpression(ctx.relationalExpression());
            Value trueValue = Value.of(true);
            Value falseValue = Value.of(false);

            List<ConditionalLogicExpressionRestContext> rests = ctx.conditionalLogicExpressionRest();
            if (rests.size() > 0 && !left.isBool()) {
                throw new ScriptException("Cannot apply `&&, ||` to non-bool expression");
            }

            for (ConditionalLogicExpressionRestContext rest : rests) {
                RelationalExpressionContext rightExp = rest.relationalExpression();

                boolean l = ((boolean) left.getValue());

                if (rest.AND() != null) {
                    left = l ? visitRelationalExpression(rightExp) : falseValue;
                } else if (rest.OR() != null) {
                    left = l ? trueValue : visitRelationalExpression(rightExp);
                }

                if (!left.isBool()) {
                    throw new ScriptException("Cannot apply `&&, ||` to non-bool expression");
                }
            }

            return left;
        }

        @Override
        public Value visitRelationalExpression(RelationalExpressionContext ctx) {
            List<AdditiveExpressionContext> adds = ctx.additiveExpression();
            Value left = visitAdditiveExpression(adds.get(0));

            if (adds.size() > 1) {
                Value right = visitAdditiveExpression(adds.get(1));

                if ((!left.isNumber() || !right.isNumber())
                        && ctx.EQUAL() == null
                        && ctx.NOTEQUAL() == null) {
                    throw new ScriptException("Cannot apply `>, <, >=, <=` to non-number expression");
                }

                if (ctx.EQUAL() == null && ctx.NOTEQUAL() == null) {
                    double l = ((double) left.getValue());
                    double r = ((double) right.getValue());
                    if (ctx.LT() != null) {
                        return Value.of(Double.compare(l, r) < 0);
                    } else if (ctx.LE() != null) {
                        return Value.of(Double.compare(l, r) <= 0);
                    } else if (ctx.GT() != null) {
                        return Value.of(Double.compare(l, r) > 0);
                    } else if (ctx.GE() != null) {
                        return Value.of(Double.compare(l, r) >= 0);
                    }
                }

                if (left.getType() != right.getType()) {
                    throw new ScriptException("Cannot apply `==, !=` to expressions that have different types");
                }

                if (ctx.EQUAL() != null) {
                    return Value.of(left.getValue().equals(right.getValue()));
                } else {
                    // NOT EQUAL
                    return Value.of(!left.getValue().equals(right.getValue()));
                }
            }

            return left;
        }

        @Override
        public Value visitAdditiveExpression(AdditiveExpressionContext ctx) {
            Value left = visitMultiplicativeExpression(ctx.multiplicativeExpression());

            List<AdditiveExpressionRestContext> rests = ctx.additiveExpressionRest();
            if (rests.size() > 0 && !left.isNumber()) {
                throw new ScriptException("Cannot apply `+, -` to non-number expression");
            }

            for (AdditiveExpressionRestContext rest : rests) {
                Value right = visitMultiplicativeExpression(rest.multiplicativeExpression());

                if (!right.isNumber()) {
                    throw new ScriptException("Cannot apply `+, -` to non-number expression");
                }

                double l = ((double) left.getValue());
                double r = ((double) right.getValue());

                if (rest.ADD() != null) {
                    left = Value.of(l + r);
                } else if (rest.SUB() != null) {
                    left = Value.of(l - r);
                }
            }

            return left;
        }

        @Override
        public Value visitMultiplicativeExpression(FlourineScriptParser.MultiplicativeExpressionContext ctx) {
            Value left = visitUnaryExpression(ctx.unaryExpression());

            List<MultiplicativeExpressionRestContext> rests = ctx.multiplicativeExpressionRest();
            if (rests.size() > 0 && !left.isNumber()) {
                throw new ScriptException("Cannot apply `*, /, %, ^` to non-number expression");
            }

            for (MultiplicativeExpressionRestContext rest : rests) {
                Value right = visitUnaryExpression(rest.unaryExpression());
                if (!right.isNumber()) {
                    throw new ScriptException("Cannot apply `*, /, %, ^` to non-number expression");
                }

                double l = ((double) left.getValue());
                double r = ((double) right.getValue());

                if (rest.MUL() != null) {
                    left = Value.of(l * r);

                } else if (rest.DIV() != null) {
                    if (r == 0) {
                        throw new ScriptException("Cannot divide zero");
                    }
                    left = Value.of(l / r);

                } else if (rest.MOD() != null) {
                    left = Value.of(((int) l) % ((int) r));

                } else if (rest.POWER() != null) {
                    if (r < 1.0 && l < 0) {
                        throw new ScriptException("Cannot calculate negative power of non-positive numbers");
                    }
                    left = Value.of(Math.pow(l, r));
                }
            }

            return left;
        }

        @Override
        public Value visitUnaryExpression(FlourineScriptParser.UnaryExpressionContext ctx) {
            if (ctx.ADD() != null || ctx.SUB() != null) {
                Value result = visitUnaryExpression(ctx.unaryExpression());
                if (!result.isNumber()) {
                    throw new ScriptException("Cannot apply `+ (positive)` or `- (negative)` to non-number expression");
                }
                double d = ((double) result.getValue());
                if (d == 0) {
                    return Value.of(0);
                }
                return Value.of(ctx.ADD() != null ? +d : -d);

            } else if (ctx.BANG() != null) {
                Value result = visitUnaryExpression(ctx.unaryExpression());
                if (!result.isBool()) {
                    throw new ScriptException("Cannot apply `! (bang)` to non-bool expression");
                }
                boolean b = (boolean) result.getValue();
                return Value.of(!b);

            } else {
                // primary expression
                return visitPrimaryExpression(ctx.primaryExpression());
            }
        }
        /* expression end */

        /* primaryExpression begin */
        @Override
        public Value visitPrimaryExpression(FlourineScriptParser.PrimaryExpressionContext ctx) {
            // TODO: apply suffixes to prefix
            return (Value) super.visitPrimaryExpression(ctx);
        }
        /* primaryExpression end */

        /* primaryPrefix begin */
        @Override
        public Object visitPrimaryPrefix(FlourineScriptParser.PrimaryPrefixContext ctx) {
            if (ctx.IDENTIFIER() != null) {
                return scope.find(ctx.IDENTIFIER().getText());
            }
            return super.visitPrimaryPrefix(ctx);
        }

        @Override
        public Value visitLiteralExpression(FlourineScriptParser.LiteralExpressionContext ctx) {
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
            throw new IllegalStateException("should not reach here");
        }

        @Override
        public Value visitQuotedExpression(FlourineScriptParser.QuotedExpressionContext ctx) {
            return visitConditionalLogicExpression(ctx.conditionalLogicExpression());
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
        /* primaryPrefix end */

        /* primarySuffix begin */
        @Override
        public Object visitLambdaCallExpression(FlourineScriptParser.LambdaCallExpressionContext ctx) {
            // TODO
            return super.visitLambdaCallExpression(ctx);
        }

        @Override
        public Object visitListVisitExpression(FlourineScriptParser.ListVisitExpressionContext ctx) {
            // TODO
            return super.visitListVisitExpression(ctx);
        }
        /* primarySuffix end */

        @Override
        public List<Parameter> visitParameterList(FlourineScriptParser.ParameterListContext ctx) {
            List<Parameter> parameters = new ArrayList<>(ctx.IDENTIFIER().size());
            for (TerminalNode node : ctx.IDENTIFIER()) {
                parameters.add(new Parameter(node.getText()));
            }
            return parameters;
        }

        @Override
        public List<Value> visitArgumentList(ArgumentListContext ctx) {
            List<Value> values = new ArrayList<>(ctx.expression().size());
            for (ExpressionContext exp : ctx.expression()) {
                values.add(visitExpression(exp));
            }
            return values;
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
