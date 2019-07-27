package com.imkiva.flourine.script.runtime.statement;

import com.imkiva.flourine.script.runtime.types.Value;

import java.util.List;

/**
 * @author kiva
 * @date 2019-07-27
 */
public class LambdaCall {
    private List<Value> argumentList;

    public LambdaCall(List<Value> argumentList) {
        this.argumentList = argumentList;
    }

    public List<Value> getArgumentList() {
        return argumentList;
    }
}
