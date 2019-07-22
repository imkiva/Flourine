package com.imkiva.flourine.script.file;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author kiva
 * @date 2018/7/22
 */
public class RegularSourceFile implements SourceFile {
    private File sourceFile;

    public RegularSourceFile(File sourceFile) {
        this.sourceFile = sourceFile;
    }

    @Override
    public CharStream openCharStream() throws IOException {
        return CharStreams.fromPath(Paths.get(sourceFile.toURI()));
    }

    @Override
    public String getSourceName() {
        return sourceFile.getName();
    }
}
