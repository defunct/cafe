package com.goodworkalan.mix;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.goodworkalan.glob.Find;
import com.goodworkalan.go.go.Argument;
import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.Library;
import com.goodworkalan.go.go.Task;
import com.goodworkalan.reflective.Method;
import com.goodworkalan.reflective.ReflectiveException;
import com.goodworkalan.reflective.ReflectiveFactory;

/**
 * Execute the Javac compiler.
 * 
 * @author Alan Gutierrez
 */
@Command(parent = MixTask.class)
public class JavacTask extends Task {
    private final ReflectiveFactory reflectiveFactory = new ReflectiveFactory();
    
    private String recipe;
    
    /** Disable warnings if false. */
    private boolean warnings;
    
    private File sourceDirectory;
    
    private File outputDirectory;

    /** Emit verbose output if true. */
    private boolean verbose;

    /** Enable debugging output if true. */
    private boolean debug;

    /** Warn about deprecation if true. */
    private boolean deprecation;

    /** The Java language version of the source. */
    private String source;

    /** The Java class file version of the output. */
    private String target;
    
    /** Artifacts. */
    private final List<Artifact> artifacts = new ArrayList<Artifact>();
    
    /** The mix wide configuration. */
    private MixTask.Configuration configuration;
    
    private boolean findConditions;
    
    private Find find = new Find();

    @Argument
    public void addRecipe(String recipe) {
        this.recipe = recipe;
    }
    
    @Argument
    public void addInclude(String string) {
        findConditions = true;
        find.include(string);
    }
    
    @Argument
    public void addExclude(String string) {
        findConditions = true;
        find.exclude(string);
    }
    
    public void setArtifact(String artifact) {
        artifacts.add(Artifact.parse(artifact));
    }

    @Argument
    public void addSourceDirectory(File sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
    }
    
    @Argument
    public void addOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    /**
     * Default constructor.
     */
    public JavacTask() {
    }

    /**
     * The the arguments common to all Mix commands which are specified after
     * the mix command on the command line.
     * 
     * @param configuration
     *            The arguments common to all Mix commands.
     */
    public void setConfiguration(MixTask.Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Compile the main resources of the Java project.
     */
    @Override
    public void execute(Environment environment) {
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
        Project project = configuration.getProject();
        if (outputDirectory == null) {
            outputDirectory = new File(project.getOutputDirectory(), "classes");
        }
        if (!outputDirectory.isDirectory() && !outputDirectory.mkdirs()) {
            throw new MixException(0);
        }
        arguments.add("-d");
        arguments.add(outputDirectory.toString());
        List<File> classpath = new ArrayList<File>();
        if (recipe != null) {
            Library library = new Library(new File(System.getProperty("user.home") + "/.m2/repository"));
            for (Dependency dependency : configuration.getProject().getDependencies(recipe)) {
                classpath.addAll(dependency.getFiles(library));
            }
        }
        if (sourceDirectory == null) {
            for (File directory : project.getSourceDirectories()) {
                for (File source : project.getSources(directory)) {
                    arguments.add(new File(directory, source.toString()).toString());
                }
            }
        } else {
            if (!findConditions) {
                find.include("**/*.java");
            }
            for (File source : find.find(sourceDirectory)) {
                arguments.add(new File(sourceDirectory, source.toString()).toString());
            }
        }
        Class<?> compilerClass;
        try {
            compilerClass = Class.forName("com.sun.tools.javac.Main");
        } catch (ClassNotFoundException e) {
            throw new MixException(0, e);
        }
        try {
            Object compiler = reflectiveFactory.getConstructor(compilerClass).newInstance();
            Method method = reflectiveFactory.getMethod(compilerClass, "compile", new String [0].getClass());
            method.invoke(compiler, new Object[] { arguments.toArray(new String[arguments.size()]) });
        } catch (ReflectiveException e) {
            throw new MixException(0, e);
        }
    }
}
