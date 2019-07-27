package com.imkiva.flourine.script.parser.file;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author kiva
 * @date 2019-07-23
 */
public class StreamSourceFile implements SourceFile {
    private final String streamName;
    private final InputStream stream;

    public StreamSourceFile(String streamName, InputStream stream) {
        this.stream = stream;
        this.streamName = streamName;
    }

    @Override
    public CharStream openCharStream() throws IOException {
        return CharStreams.fromStream(stream);
    }

    @Override
    public String getSourceName() {
        return streamName;
    }
}
