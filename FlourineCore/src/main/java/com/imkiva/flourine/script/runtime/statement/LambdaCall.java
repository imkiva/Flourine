package com.imkiva.flourine.script.runtime.statement;

import com.imkiva.flourine.script.runtime.Argument;

import java.util.List;

/**
 * @author kiva
 * @date 2019-07-27
 */
public class LambdaCall {
    private List<Argument> argumentList;

    public LambdaCall(List<Argument> argumentList) {
        this.argumentList = argumentList;
    }

    public List<Argument> getArgumentList() {
        return argumentList;
    }
}
