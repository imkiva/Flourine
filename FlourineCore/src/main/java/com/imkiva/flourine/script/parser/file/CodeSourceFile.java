package com.imkiva.flourine.script.parser.file;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;

import java.io.IOException;

/**
 * @author kiva
 * @date 2018/7/22
 */
public class CodeSourceFile implements SourceFile {
    private String sourceName;
    private String code;

    public CodeSourceFile(String sourceName, String code) {
        this.sourceName = sourceName;
        this.code = code;
    }

    @Override
    public CharStream openCharStream() throws IOException {
        return CharStreams.fromString(code);
    }

    @Override
    public String getSourceName() {
        return sourceName;
    }
}
