package com.imkiva.flourine.prove.base;

import com.imkiva.flourine.script.runtime.Scope;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kiva
 * @date 2019-08-03
 */
public class Solution {
    private DraftPaper draftPaper = new DraftPaper();
    private List<Procedure> procedures = new ArrayList<>();

    protected Solution(Scope scope) {
        updateDrafts(scope);
    }

    protected <T extends Procedure> T procedure(T procedure) {
        procedures.add(procedure);
        return procedure;
    }

    public List<Procedure> getProcedures() {
        return procedures;
    }

    protected DraftPaper getDraftPaper() {
        return draftPaper;
    }

    protected void updateDrafts(Scope scope) {
        getDraftPaper().setScope(scope);
    }
}
