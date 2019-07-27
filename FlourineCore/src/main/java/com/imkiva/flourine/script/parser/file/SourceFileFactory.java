package com.imkiva.flourine.script.parser.file;

import java.io.File;
import java.io.InputStream;

/**
 * @author kiva
 * @date 2018/7/22
 */
public final class SourceFileFactory {
    public static final String SOURCE_NAME_UNKNOWN = "<unknown>";
    public static final String SOURCE_NAME_STDIN = "<stdin>";

    public static SourceFile fromStdin() {
        return fromStream(SOURCE_NAME_STDIN, System.in);
    }

    public static SourceFile fromFile(String file) {
        return fromFile(new File(file));
    }

    public static SourceFile fromFile(File file) {
        return new RegularSourceFile(file);
    }

    public static SourceFile fromStream(InputStream stream) {
        return fromStream(SOURCE_NAME_UNKNOWN, stream);
    }

    public static SourceFile fromStream(String streamName, InputStream stream) {
        return new StreamSourceFile(streamName, stream);
    }

    public static SourceFile fromCode(String code) {
        return new CodeSourceFile(SOURCE_NAME_UNKNOWN, code);
    }

    public static SourceFile fromCode(String sourceName, String code) {
        return new CodeSourceFile(sourceName, code);
    }
}
