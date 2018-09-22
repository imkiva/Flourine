package com.imkiva.flourine.core.theorem;

import com.imkiva.flourine.core.misc.Parameter;
import com.imkiva.flourine.core.misc.ParameterType;
import com.imkiva.flourine.core.solution.Solution;

/**
 * @author kiva
 * @date 2018/9/23
 */
public abstract class AbstractTheorem {
    abstract ParameterType<?>[] getParameterTypes();

    abstract boolean solve(Solution current, Parameter<?>[] parameters);
}
