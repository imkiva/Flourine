package com.imkiva.flourine.script.types;

import com.imkiva.flourine.script.antlr.FlourineScriptParser.ExpressionContext;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kiva
 * @date 2019-07-23
 */
public class ListValue extends LazyValue {
    private List<ExpressionContext> items;

    public ListValue(List<ExpressionContext> items) {
        this.items = items;
    }

    public ExpressionContext getExpItem(int i) {
        return items.get(i);
    }

    public Value evalExpItem(int i) {
        return evaluate(getExpItem(i));
    }

    @Override
    public String toString() {
        return "{" +
                items.stream()
                        .map(m -> hasEvaluatedValue(m) ? evaluate(m).toString() : "_")
                        .collect(Collectors.joining(", ")) +
                "}";
    }
}
