package com.imkiva.flourine.script.parser.file;

import org.antlr.v4.runtime.CharStream;

import java.io.IOException;

/**
 * @author kiva
 * @date 2018/7/22
 */
public interface SourceFile {
    CharStream openCharStream() throws IOException;

    String getSourceName();
}
