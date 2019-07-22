package com.imkiva.flourine.script.parser;

import com.imkiva.flourine.script.antlr.FlourineScriptParser;
import com.imkiva.flourine.script.file.SourceFile;
import org.antlr.v4.runtime.TokenStream;

/**
 * @author kiva
 * @date 2018/2/4
 */
public class Parser extends FlourineScriptParser {
    private SourceFile sourceFile;

    public Parser(SourceFile sourceFile, TokenStream input) {
        super(input);
        this.sourceFile = sourceFile;
    }

    public SourceFile getSourceFile() {
        return sourceFile;
    }
}
