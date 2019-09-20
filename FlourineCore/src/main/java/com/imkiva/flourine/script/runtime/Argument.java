package com.imkiva.flourine.script.runtime;

import com.imkiva.flourine.script.runtime.types.Value;

/**
 * @author kiva
 * @date 2019-09-19
 */
public class Argument {
    private Value argumentValue;

    public Argument(Value argumentValue) {
        this.argumentValue = argumentValue;
    }

    public Value getArgumentValue() {
        return argumentValue;
    }
}
