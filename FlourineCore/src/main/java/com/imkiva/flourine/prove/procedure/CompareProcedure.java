package com.imkiva.flourine.prove.procedure;

import com.imkiva.flourine.prove.base.Procedure;
import com.imkiva.flourine.script.runtime.types.Value;

/**
 * @author kiva
 * @date 2019-08-03
 */
public class CompareProcedure extends Procedure {
    public static final int EQUAL = 0;
    public static final int NOT_EQUAL = 2;
    public static final int LESS_THAN = -1;
    public static final int GREATER_THAN = 1;
    public static final int INCOMPARABLE = -2;

    private Value one;
    private Value two;

    public CompareProcedure(Value one, Value two) {
        this.one = one;
        this.two = two;

        if (one.isNumber() && two.isNumber()) {
            setResult(Value.of(Double.compare(one.cast(), two.cast())));
            return;
        }

        if (one.getType() != two.getType()) {
            setResult(Value.of(INCOMPARABLE));
            return;
        }

        setResult(Value.of(one.equals(two) ? EQUAL : NOT_EQUAL));
    }

    private String getOperator() {
        int result = (int) (double) getResult().cast();
        switch (result) {
            case CompareProcedure.EQUAL:
                return "=";
            case CompareProcedure.NOT_EQUAL:
            case CompareProcedure.INCOMPARABLE:
                return "!=";
            case CompareProcedure.LESS_THAN:
                return "<";
            case CompareProcedure.GREATER_THAN:
                return ">";
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public String toString() {
        return "∴ " + one + " " + getOperator() + " " + two;
    }
}
