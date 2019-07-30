package com.imkiva.flourine.script;

/**
 * @author kiva
 * @date 2019-07-30
 */
public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: fs <file> [args]");
            System.exit(0);
        }

        FlourineScriptRuntime runtime = new FlourineScriptRuntime();
        runtime.setSolveOutput(System.out::println);
        runtime.runFile(args[0], args);
    }
}
