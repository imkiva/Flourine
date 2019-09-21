package com.imkiva.flourine.script.runtime;

import com.imkiva.flourine.script.antlr.FlourineScriptBaseVisitor;
import com.imkiva.flourine.script.antlr.FlourineScriptParser;
import com.imkiva.flourine.script.antlr.FlourineScriptParser.*;
import com.imkiva.flourine.script.parser.Parser;
import com.imkiva.flourine.script.parser.ParserFactory;
import com.imkiva.flourine.script.parser.file.SourceFile;
import com.imkiva.flourine.script.runtime.statement.LambdaCall;
import com.imkiva.flourine.script.runtime.statement.ListVisit;
import com.imkiva.flourine.script.runtime.types.*;
import com.imkiva.flourine.utils.FlourineStreams;
import com.imkiva.flourine.utils.Pair;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kiva
 * @date 2019-07-26
 */
public class Interpreter {
    static class InterpreterVisitor extends FlourineScriptBaseVisitor {
        private Scope scope;
        private SolveOutput solveOutput;

        InterpreterVisitor() {
            this(new Scope(null));
        }

        InterpreterVisitor(Scope scope) {
            this.scope = scope;
        }

        Scope getScope() {
            return scope;
        }

        SolveOutput getSolveOutput() {
            return solveOutput;
        }

        void setSolveOutput(SolveOutput solveOutput) {
            this.solveOutput = solveOutput;
        }

        /* statement begin */
        @Override
        public Object visitVariableDeclStatement(FlourineScriptParser.VariableDeclStatementContext ctx) {
            String varName = ctx.IDENTIFIER().getText();
            Value value = visitExpression(ctx.expression());
            scope.set(varName, value);
            return super.visitVariableDeclStatement(ctx);
        }

        @Override
        public Object visitSolveStatement(SolveStatementContext ctx) {
            Value value = null;
            if (ctx.IDENTIFIER() != null) {
                value = getScope().find(ctx.IDENTIFIER().getText());
            } else if (ctx.expression() != null) {
                value = visitExpression(ctx.expression());
            }
            SolveOutput solveOutput = getSolveOutput();
            if (solveOutput != null) {
                solveOutput.solveOutput(value);
            }
            return super.visitSolveStatement(ctx);
        }

        /* statement end */

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

            List<ConditionalLogicExpressionRestContext> rests = ctx.conditionalLogicExpressionRest();
            if (rests.size() > 0) {
                Value trueValue = Value.of(true);
                Value falseValue = Value.of(false);

                if (!left.isBool()) {
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
            Value result = visitPrimaryPrefix(ctx.primaryPrefix());

            for (PrimarySuffixContext suffixContext : ctx.primarySuffix()) {
                Object caller = visitPrimarySuffix(suffixContext);
                if (caller instanceof LambdaCall) {
                    if (!result.isLambda()) {
                        throw new ScriptException("Cannot call non-lambda value");
                    }
                    List<Argument> args = ((LambdaCall) caller).getArgumentList();
                    Lambda lambda = ((Lambda) result.getValue());
                    result = lambda.call(args);

                } else if (caller instanceof ListVisit) {
                    if (!result.isList()) {
                        throw new ScriptException("Cannot visit non-list value with [index]");
                    }
                    int index = ((ListVisit) caller).getIndex();
                    ListValue listValue = ((ListValue) result.getValue());
                    if (index < 0 || index >= listValue.size()) {
                        throw new ScriptException("Index " + index + " out of bounds: list size is "
                                + listValue.size());
                    }
                    result = listValue.getItem(index);
                }
            }

            return result;
        }
        /* primaryExpression end */

        /* primaryPrefix begin */
        @Override
        public Value visitPrimaryPrefix(FlourineScriptParser.PrimaryPrefixContext ctx) {
            if (ctx.IDENTIFIER() != null) {
                Value value = getScope().find(ctx.IDENTIFIER().getText());
                if (value == null) {
                    throw new ScriptException("Variable not found: " +
                            ctx.IDENTIFIER().getText());
                }
                return value;
            }
            return (Value) super.visitPrimaryPrefix(ctx);
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
            LambdaInterpreter interpreter = new LambdaInterpreter(getScope());
            ScriptLambda lambda = new ScriptLambda(visitParameterList(paramCtx), ctx.lambdaBody(), interpreter);
            interpreter.bind(lambda);
            return Value.of(lambda);
        }
        /* primaryPrefix end */

        /* primarySuffix begin */
        @Override
        public LambdaCall visitLambdaCallExpression(FlourineScriptParser.LambdaCallExpressionContext ctx) {
            return new LambdaCall(visitArgumentList(ctx.argumentList()));
        }

        @Override
        public ListVisit visitListVisitExpression(FlourineScriptParser.ListVisitExpressionContext ctx) {
            Value v = visitAdditiveExpression(ctx.additiveExpression());
            if (!v.isNumber()) {
                throw new ScriptException("Visit list by non-integer is not allowed");
            }

            return new ListVisit((int) (double) v.getValue());
        }
        /* primarySuffix end */

        @Override
        public List<Parameter> visitParameterList(FlourineScriptParser.ParameterListContext ctx) {
            List<Parameter> parameters = new ArrayList<>(ctx.IDENTIFIER().size());
            for (TerminalNode node : ctx.IDENTIFIER()) {
                parameters.add(new Parameter(node.getText()));
            }
            if (ctx.ELLIPSIS() != null) {
                parameters.add(Parameter.VARARGS);
            }
            return parameters;
        }

        @Override
        public List<Argument> visitArgumentList(ArgumentListContext ctx) {
            List<Argument> values = new ArrayList<>(ctx.expression().size());
            for (ExpressionContext exp : ctx.expression()) {
                values.add(new Argument(visitExpression(exp)));
            }
            if (ctx.ELLIPSIS() != null) {
                ListValue varargs = getScope().find(Parameter.VARARGS.getName()).cast();
                varargs.stream()
                        .map(Argument::new)
                        .forEach(values::add);
            }
            return values;
        }

        @Override
        public Value visitLambdaBody(LambdaBodyContext ctx) {
            ctx.statement().forEach(this::visitStatement);
            return visitExpression(ctx.expression());
        }
    }

    static class LambdaInterpreter implements ScriptLambda.Caller {
        private Interpreter.InterpreterVisitor visitor;
        private List<Parameter> parameterList;
        private LambdaBodyContext lambdaBodyContext;

        LambdaInterpreter(Scope declaringScope) {
            Scope scope = new Scope(declaringScope);
            visitor = new Interpreter.InterpreterVisitor(scope);
        }

        void bind(ScriptLambda lambda) {
            parameterList = lambda.getParameters();
            lambdaBodyContext = lambda.getBody();
        }

        private void bindArguments(List<Argument> args) {
            if (hasVarargs()) {
                bindVarargs(args);

            } else {
                bindNormalArgs(args);
            }
        }

        private void bindNormalArgs(List<Argument> args) {
            Scope scope = visitor.getScope();

            if (args.size() != parameterList.size()) {
                throw new ScriptException("Argument size does not match: " +
                        "expected "
                        + parameterList.size()
                        + ", but got "
                        + args.size());
            }

            FlourineStreams.zip(parameterList, args, Pair::new)
                    .forEach(scope::set);
        }

        private void bindVarargs(List<Argument> args) {
            // construct a ListValue named <varargs> the rest of the argument
            Scope scope = visitor.getScope();

            int exactParamCount = parameterList.size() - 1;
            if (exactParamCount > args.size()) {
                throw new ScriptException("Argument count mismatch: " +
                        "expected "
                        + exactParamCount
                        + " at least, but got "
                        + args.size());
            }

            List<Argument> varargValues = args.subList(exactParamCount, args.size());
            scope.set(Parameter.VARARGS, Value.of(buildVarargValue(varargValues)));

            List<Parameter> exactParams = parameterList.subList(0, exactParamCount);
            List<Argument> exactArgs = args.subList(0, exactParamCount);
            FlourineStreams.zip(exactParams, exactArgs, Pair::new)
                    .forEach(scope::set);
        }

        private ListValue buildVarargValue(List<Argument> arguments) {
            return new ListValue(
                    arguments.stream()
                            .map(Argument::getArgumentValue)
                            .collect(Collectors.toList())
            );
        }

        private boolean hasVarargs() {
            if (parameterList.size() > 0) {
                // We only allow the last parameter to be varargs
                return parameterList.get(parameterList.size() - 1) == Parameter.VARARGS;
            }
            return false;
        }

        @Override
        public Value call(List<Argument> args) {
            bindArguments(args);
            return visitor.visitLambdaBody(lambdaBodyContext);
        }
    }

    private InterpreterVisitor visitor;

    public Interpreter() {
        this.visitor = new InterpreterVisitor();
    }

    public Interpreter(Scope scope) {
        this(scope, null);
    }

    public Interpreter(Scope scope, SolveOutput solveOutput) {
        this.visitor = new InterpreterVisitor(scope);
        setSolveOutput(solveOutput);
    }

    public void setSolveOutput(SolveOutput solveOutput) {
        visitor.setSolveOutput(solveOutput);
    }

    public void evaluate(SourceFile sourceFile) {
        Parser parser = ParserFactory.fromSourceFile(sourceFile);
        visitor.visit(parser.compilationUnit());
    }

    public Scope getScope() {
        return visitor.getScope();
    }
}
