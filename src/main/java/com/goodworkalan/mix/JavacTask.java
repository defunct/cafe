package com.goodworkalan.mix;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.goodworkalan.glob.Files;
import com.goodworkalan.glob.Find;
import com.goodworkalan.go.go.Argument;
import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.Library;
import com.goodworkalan.go.go.PathPart;
import com.goodworkalan.go.go.ResolutionPart;
import com.goodworkalan.go.go.Task;
import com.goodworkalan.reflective.Method;
import com.goodworkalan.reflective.ReflectiveException;
import com.goodworkalan.reflective.ReflectiveFactory;
import com.goodworkalan.spawn.Redirect;
import com.goodworkalan.spawn.Spawn;

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
    
    private File outputDirectory;
    
    /** Fork if true. */
    private boolean fork;

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
    
    private FindList findList = new FindList();

    @Argument
    public void addArtifact(Artifact artifact) {
        artifacts.add(artifact);
    }

    @Argument
    public void addRecipe(String recipe) {
        this.recipe = recipe;
    }
    
    @Argument
    public void addInclude(String string) {
        findList.addInclude(string);
    }
    
    @Argument
    public void addExclude(String string) {
        findList.addExclude(string);
    }
    
    @Argument
    public void addSourceDirectory(File sourceDirectory) {
        findList.addDirectory(sourceDirectory);
    }
    
    @Argument
    public void addOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }
    
    @Argument
    public void addDebug(boolean debug) {
        this.debug = debug;
    }
    
    @Argument
    public void addFork(boolean fork) {
        this.fork = fork;
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
    public void execute(Environment env) {
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
        if (outputDirectory == null || !(outputDirectory.isDirectory() || outputDirectory.mkdirs())) {
            throw new MixException(0);
        }
        arguments.add("-d");
        arguments.add(outputDirectory.toString());
        Collection<PathPart> parts = new ArrayList<PathPart>();
        for (Artifact artifact : artifacts) {
            parts.add(new ResolutionPart(artifact));
        }
        if (recipe != null) {
            for (Dependency dependency : project.getRecipe(recipe).getDependencies()) {
                parts.addAll(dependency.getPathParts(project));
            }
        }
        Set<File> classpath = new LinkedHashSet<File>();
        Library library = env.part.getCommandInterpreter().getLibrary();
        classpath.addAll(library.resolve(parts).getFiles());
        if (!classpath.isEmpty()) {
            arguments.add("-cp");
            arguments.add(Files.path(classpath));
        }
        for (FindList.Entry entry : findList) {
            Find find = entry.getFind();
            if (!find.hasFilters()) {
                find.include("**/*.java");
            }
            for (String fileName : find.find(entry.getDirectory())) {
                arguments.add(new File(entry.getDirectory(), fileName).toString());
            }
        }
        Class<?> compilerClass;
        try {
            compilerClass = Class.forName("com.sun.tools.javac.Main");
        } catch (ClassNotFoundException e) {
            throw new MixException(0, e);
        }
        if (fork) {
            arguments.add(0, "javac");
            
            ProcessBuilder newProcess = new ProcessBuilder();
            newProcess.command().addAll(arguments);
            
            Spawn<Redirect, Redirect> spawn;
            spawn = Spawn.spawn(new Redirect(env.out), new Redirect(env.err));

            spawn.getProcessBuilder().command().addAll(arguments);
            spawn.execute();
        } else {
            try {
                Object compiler = reflectiveFactory.getConstructor(compilerClass).newInstance();
                Method method = reflectiveFactory.getMethod(compilerClass, "compile", new String [0].getClass());
                method.invoke(compiler, new Object[] { arguments.toArray(new String[arguments.size()]) });
            } catch (ReflectiveException e) {
                throw new MixException(0, e);
            }
        }
    }
}
