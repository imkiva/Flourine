package com.imkiva.flourine.script.parser;

/**
 * @author kiva
 * @date 2019-07-23
 */
public class ParseFailedException extends RuntimeException {
    public ParseFailedException(String message) {
        super(message);
    }

    public ParseFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
