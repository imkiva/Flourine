package com.imkiva.flourine.utils;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Formatter;
import java.util.function.Supplier;

/**
 * @author kiva
 * @date 2017/12/16
 */
public final class Log {
    private static boolean LOG_ENABLED = true;

    public static void setLogEnabled(boolean enabled) {
        Log.LOG_ENABLED = enabled;
    }

    private static String getCallerHeader(String logType) {
        StackTraceElement targetElement = new Throwable().getStackTrace()[3];
        String className = targetElement.getClassName();
        String[] classNameInfo = Arrays.stream(className.split("\\."))
                .filter(it -> !it.isEmpty())
                .toArray(String[]::new);

        if (classNameInfo.length != 0) {
            className = classNameInfo[classNameInfo.length - 1];
        }

        if (className.contains("$")) {
            className = Arrays.stream(className.split("\\$"))
                    .filter(it -> !it.isEmpty())
                    .toArray(String[]::new)[0];
        }

        return new Formatter()
                .format("[Thread:%s @ %s] %s.%s (%s:%d): ",
                        Thread.currentThread().getName(),
                        logType,
                        className,
                        targetElement.getMethodName(),
                        targetElement.getFileName(),
                        targetElement.getLineNumber())
                .toString();
    }

    private static void log(PrintStream printStream, String logType, Supplier<String> message) {
        if (LOG_ENABLED) {
            printStream.print(getCallerHeader(logType));
            printStream.println(message.get());
        }
    }

    public static void e(Supplier<String> message) {
        log(System.err, "Error", message);
    }

    public static void d(Supplier<String> message) {
        log(System.out, "Debug", message);
    }

    public static void i(Supplier<String> message) {
        log(System.out, "Info", message);
    }

    public static void w(Supplier<String> message) {
        log(System.err, "Warn", message);
    }
}
