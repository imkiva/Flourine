package com.imkiva.flourine.prove.base;

import com.imkiva.flourine.script.runtime.types.Value;

/**
 * @author kiva
 * @date 2019-08-03
 */
public abstract class Procedure {
    private Value cachedResult;

    public final Value getResult() {
        return cachedResult;
    }

    protected void setResult(Value cachedResult) {
        this.cachedResult = cachedResult;
    }
}
