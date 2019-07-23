package com.imkiva.flourine.script.types;

import com.imkiva.flourine.script.runtime.ScriptConfig;

/**
 * @author kiva
 * @date 2019-07-23
 */
public class Value {
    public static final Class TYPE_NUMBER = Double.class;
    public static final Class TYPE_STRING = String.class;
    public static final Class TYPE_CHAR = Character.class;
    public static final Class TYPE_BOOL = Boolean.class;
    public static final Class TYPE_LAMBDA = Lambda.class;
    public static final Class TYPE_POINT = PointValue.class;
    public static final Class TYPE_LIST = ListValue.class;

    public static Value of(double value) {
        return new Value(value);
    }

    public static Value of(char value) {
        return new Value(value);
    }

    public static Value of(boolean value) {
        return new Value(value);
    }

    public static Value of(String value) {
        if (value == null) {
            throw new IllegalArgumentException("variables cannot be null");
        }

        return new Value(value);
    }

    public static Value of(Lambda value) {
        if (value == null) {
            throw new IllegalArgumentException("variables cannot be null");
        }

        return new Value(value);
    }

    public static Value of(PointValue value) {
        if (value == null) {
            throw new IllegalArgumentException("variables cannot be null");
        }

        return new Value(value);
    }

    public static Value of(ListValue value) {
        if (value == null) {
            throw new IllegalArgumentException("variables cannot be null");
        }

        return new Value(value);
    }

    public static Value of(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("variables cannot be null");
        }

        if (isNumericType(value.getClass())) {
            return of(Double.parseDouble(value.toString()));
        }

        if (ScriptConfig.ENABLE_EXPORT_JAVA) {
            return new Value(value);
        }

        throw new IllegalArgumentException("unsupported variable type " + value.getClass().getName());
    }

    private Class type;
    private Object value;

    public Value(Object value) {
        this.value = value;
        this.type = value.getClass();
    }

    public Class getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public boolean isNumericType() {
        return isNumericType(type);
    }

    private static boolean isNumericType(Class type) {
        return type == Double.class
                || type == Short.class
                || type == Long.class
                || type == Integer.class
                || type == Float.class;
    }
}
