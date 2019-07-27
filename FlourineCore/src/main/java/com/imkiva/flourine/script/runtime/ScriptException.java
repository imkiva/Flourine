package com.imkiva.flourine.script.runtime;

/**
 * @author kiva
 * @date 2019-07-27
 */
public class ScriptException extends RuntimeException {
    public ScriptException() {
    }

    public ScriptException(String message) {
        super(message);
    }

    public ScriptException(String message, Throwable cause) {
        super(message, cause);
    }
}
