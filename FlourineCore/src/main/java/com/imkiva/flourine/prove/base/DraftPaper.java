package com.imkiva.flourine.prove.base;

import com.imkiva.flourine.script.runtime.Scope;
import com.imkiva.flourine.script.runtime.types.Value;

/**
 * @author kiva
 * @date 2019-08-03
 */
public class DraftPaper {
    private Scope scope;

    void setScope(Scope scope) {
        this.scope = scope;
    }

    public Value find(String name) {
        return scope.find(name);
    }
}
