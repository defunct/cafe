package com.goodworkalan.mix;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.goodworkalan.go.go.Argument;
import com.goodworkalan.go.go.Task;

/**
 * Execute the Javac compiler.
 * 
 * @author Alan Gutierrez
 */
public class Compile extends Task {
    /** Disable warnings if false. */
    private boolean warnings;

    /** Emit verbose output if true. */
    private boolean verbose;

    /** Enable debugging output if true. */
    private boolean debug;

    /** Fork the Javac compiler if true. */
    private boolean fork;

    /** Warn about deprecation if true. */
    private boolean deprecation;

    /** The Java language version of the source. */
    private String source;

    /** The Java class file version of the output. */
    private String target;
    
    /** The mix wide configuration. */
    private Mix.Arguments configuration;

    /** The mix command. */
    private Mix mix;

    /**
     * Default constructor.
     */
    public Compile() {
    }

    /**
     * The the arguments common to all Mix commands which are specified after
     * the mix command on the command line.
     * 
     * @param configuration
     *            The arguments common to all Mix commands.
     */
    public void setConfiguration(Mix.Arguments configuration) {
        this.configuration = configuration;
    }

    /**
     * Set the mix parent task.
     * 
     * @param mix
     *            The mix parent task.
     */
    @Argument
    public void setMix(Mix mix) {
        this.mix = mix;
    }

    /**
     * Get the project model.
     * 
     * @return The project model.
     */
    public Project getProject() {
        return new Project(configuration.getWorkingDirectory());
    }

    /**
     * Compile the main resources of the Java project.
     */
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
        new File("src/test/project/target/classes").mkdirs();
        arguments.add("-d");
        arguments.add("src/test/project/target/classes");
        Project project = getProject();
        for (File directory : project.getSourceDirectories()) {
            for (File source : project.getSources(directory)) {
                arguments.add(new File(directory, source.toString()).toString());
            }
        }
        com.sun.tools.javac.Main.compile(arguments.toArray(new String[arguments.size()]));
    }
}
