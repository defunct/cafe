package com.goodworkalan.mix;

import java.util.ArrayList;
import java.util.List;

import com.goodworkalan.go.Shell;

/**
 * Execute the Javac compiler.
 *
 * @author Alan Gutierrez
 */
public class Javac {
    /** Disable warnings if false. */
    private boolean warnings;
    
    /** Emit verbose output if true. */
    private boolean verbose;

    /** Enable debugable output if true. */
    private boolean debug;

    /** Fork the Javac compiler if true. */
    private boolean fork;

    /** Warn about deprecation if true. */
    private boolean deprecation;

    /** The Java language version of the source. */
    private String source;

    /** The Java classfile version of the output. */
    private String target;

    public void execute() {
        List<String> arguments = new ArrayList<String>();
        if (!warnings) {
            arguments.add("-nowarn");
        }
        if (verbose) {
            arguments.add("-verbose");
        }
        if (debug) {
            arguments.add("-g");
        }
        if (deprecation) {
            arguments.add("-deprecation");
        }
        if (source != null) {
            arguments.add("-source");
            arguments.add(source);
        }
        if (target != null) {
            arguments.add("-target");
            arguments.add(target);
        }
        String[] args = arguments.toArray(new String[arguments.size()]);
        if (fork) {
            Shell.execute("javac", arguments);
        } else {
            com.sun.tools.javac.Main.compile(args);
        }
    }
}
