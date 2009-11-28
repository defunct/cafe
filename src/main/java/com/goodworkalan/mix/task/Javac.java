package com.goodworkalan.mix.task;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.goodworkalan.comfort.io.Files;
import com.goodworkalan.comfort.io.Find;
import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.Library;
import com.goodworkalan.go.go.PathPart;
import com.goodworkalan.go.go.ResolutionPart;
import com.goodworkalan.mix.Dependency;
import com.goodworkalan.mix.FindList;
import com.goodworkalan.mix.MixException;
import com.goodworkalan.mix.Project;
import com.goodworkalan.mix.builder.Executable;
import com.goodworkalan.mix.builder.RecipeElement;
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
public class Javac extends JavacOptionsElement<RecipeElement, Javac>{
    private final ReflectiveFactory reflectiveFactory = new ReflectiveFactory();
    
    /** Disable warnings if false. */
    private boolean warnings;
    
    private File output;
    
    /** Artifacts. */
    private final List<Artifact> artifacts = new ArrayList<Artifact>();
    
    private FindList findList = new FindList();

    public Javac(RecipeElement recipeElement) {
        super(recipeElement, new SelfServer<Javac>(), null);
        ending = new JavacEnd() {
            public void end(JavacConfiguration configuration) {
                configure(configuration);
                parent.addExecutable(new Executable() {
                    public void execute(Environment env, Project project, String recipeName) {
                        List<String> arguments = new ArrayList<String>();
                        if (!warnings) {
                            arguments.add("-nowarn");
                        }
                        if (verbose != null && verbose) {
                            arguments.add("-verbose");
                        }
                        if (debug == null || debug) {
                            arguments.add("-g");
                        }
                        if (deprecation != null && deprecation) {
                            arguments.add("-deprecation");
                            arguments.add("-Xlint:deprecation");
                        }
                        if (unchecked != null && unchecked) {
                            arguments.add("-Xlint:unchecked");
                        }
                        if (source != null) {
                            arguments.add("-source");
                            arguments.add(source);
                        }
                        if (target != null) {
                            arguments.add("-target");
                            arguments.add(target);
                        }
                        if (output == null || !(output.isDirectory() || output.mkdirs())) {
                            throw new MixException(0);
                        }
                        arguments.add("-d");
                        arguments.add(output.toString());
                        Collection<PathPart> parts = new ArrayList<PathPart>();
                        for (Artifact artifact : artifacts) {
                            parts.add(new ResolutionPart(artifact));
                        }
                        for (Dependency dependency : project.getRecipe(recipeName).getDependencies()) {
                            parts.addAll(dependency.getPathParts(project));
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
                        Class<?> compilerClass = null;
                        try {
                            compilerClass = Class.forName("com.sun.tools.javac.Main");
                        } catch (ClassNotFoundException e) {
                        }
                        if ((fork != null && fork) || compilerClass == null) {
                            arguments.add(0, "javac");
                            
                            ProcessBuilder newProcess = new ProcessBuilder();
                            newProcess.command().addAll(arguments);
                            
                            Spawn<Redirect, Redirect> spawn;
                            spawn = Spawn.spawn(new Redirect(env.io.out), new Redirect(env.io.err));
                            
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
                });
            }
        };
        self.setSelf(this);
    }
    
    public Javac artifact(Artifact artifact) {
        artifacts.add(artifact);
        return this;
    }

    public FindElement<Javac> source(File directory) {
        return new FindElement<Javac>(this, findList, directory);
    }
    
    public Javac output(File output) {
        this.output = output;
        return this;
    }

    public Javac source(FindList findList) {
        findList.addAll(findList);
        return this;
    }
    
    public Javac configure(JavacConfiguration...configurations) {
        return configure(Arrays.asList(configurations));
    }
    
    public Javac configure(List<JavacConfiguration> configurations) {
        for (JavacConfiguration javacConfiguration : configurations) {
            javacConfiguration.configure(this);
        }
        return this;
    }
}
