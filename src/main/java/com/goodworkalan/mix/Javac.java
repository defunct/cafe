package com.goodworkalan.mix;

import java.util.ArrayList;
import java.util.List;

import com.goodworkalan.go.Shell;
import com.goodworkalan.go.Task;

@Task("javac")
public class Javac {
    private boolean warnings;

    private boolean verbose;

    private boolean debug;

    private boolean fork;

    private boolean deprecation;

    private String source;

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
