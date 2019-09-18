package com.imkiva.flourine.prove.procedure;

import com.imkiva.flourine.prove.base.DraftPaper;
import com.imkiva.flourine.prove.base.Procedure;
import com.imkiva.flourine.script.runtime.types.Value;

/**
 * @author kiva
 * @date 2019-08-03
 */
public class FindProcedure extends Procedure {
    private Value thing;

    public FindProcedure(DraftPaper draftPaper, Value thing) {
        this.thing = thing;
        setResult(draftPaper.find(thing.cast()));
    }

    @Override
    public String toString() {
        return "∵ " + thing + " = " + getResult();
    }
}
