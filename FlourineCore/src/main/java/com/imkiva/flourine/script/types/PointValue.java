package com.imkiva.flourine.script.types;

import com.imkiva.flourine.script.antlr.FlourineScriptParser.ExpressionContext;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author kiva
 * @date 2019-07-23
 */
public class PointValue extends LazyValue {
    private ExpressionContext expX;
    private ExpressionContext expY;
    private ExpressionContext expZ;

    public PointValue(ExpressionContext expX, ExpressionContext expY, ExpressionContext expZ) {
        this.expX = expX;
        this.expY = expY;
        this.expZ = expZ;
    }

    public Value evalX() {
        return evaluate(getExpX());
    }

    public Value evalY() {
        return evaluate(getExpY());
    }

    public Value evalZ() {
        return evaluate(getExpZ());
    }

    public ExpressionContext getExpX() {
        return expX;
    }

    public ExpressionContext getExpY() {
        return expY;
    }

    public ExpressionContext getExpZ() {
        return expZ;
    }

    @Override
    public String toString() {
        return "(" +
                Stream.of(expX, expY, expZ)
                        .filter(Objects::nonNull)
                        .map(m -> hasEvaluatedValue(m) ? evaluate(m).toString() : "_")
                        .collect(Collectors.joining(", ")) +
                ")";
    }
}
