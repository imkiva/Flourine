package com.imkiva.flourine.script.parser;

import com.imkiva.flourine.script.antlr.FlourineScriptLexer;
import com.imkiva.flourine.script.file.SourceFile;
import com.imkiva.flourine.utils.Log;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;

/**
 * @author kiva
 * @date 2018/2/4
 */
public final class ParserFactory {
    public static Parser fromSourceFile(SourceFile sourceFile) {
        try {
            CharStream charStream = sourceFile.openCharStream();
            FlourineScriptLexer lexer = new FlourineScriptLexer(charStream);
            return new Parser(sourceFile, new CommonTokenStream(lexer));
        } catch (IOException e) {
            Log.e(() -> "Create parser failed: " + e.getLocalizedMessage());
            throw new ParseFailedException("Failed to create parser", e);
        }
    }
}
