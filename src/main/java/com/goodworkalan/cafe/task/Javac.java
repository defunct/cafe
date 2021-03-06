package com.goodworkalan.cafe.task;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.goodworkalan.cafe.Build;
import com.goodworkalan.cafe.Dependency;
import com.goodworkalan.cafe.Make;
import com.goodworkalan.cafe.CafeError;
import com.goodworkalan.cafe.Project;
import com.goodworkalan.cafe.builder.FindList;
import com.goodworkalan.cafe.builder.FindStatement;
import com.goodworkalan.cafe.builder.RecipeStatement;
import com.goodworkalan.comfort.io.Files;
import com.goodworkalan.comfort.io.Find;
import com.goodworkalan.danger.Danger;
import com.goodworkalan.go.go.Commandable;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.library.Artifact;
import com.goodworkalan.go.go.library.PathPart;
import com.goodworkalan.go.go.library.PathParts;
import com.goodworkalan.go.go.library.ResolutionPart;
import com.goodworkalan.spawn.Exit;
import com.goodworkalan.spawn.Spawn;

/**
 * Execute the Javac compiler.
 * 
 * @author Alan Gutierrez
 */
public class Javac extends JavacOptionsElement<RecipeStatement, Javac>{
    /** Disable warnings if false. */
    private boolean warnings;
    
    // TODO Document.
    private File output;
    
    /** Artifacts. */
    private final List<Artifact> artifacts = new ArrayList<Artifact>();
    
    // TODO Document.
    private FindList findList = new FindList();
    
    // TODO Document.
    public Javac(RecipeStatement recipeElement) {
        super(recipeElement, new SelfServer<Javac>(), null);
        ending = new JavacEnd() {
            public void end(JavacConfiguration configuration) {
                configure(configuration);
                parent.executable(new Commandable() {
                    public void execute(Environment env) {
                        List<String> arguments = new ArrayList<String>();
                        if (!warnings) {
                            arguments.add("-nowarn");
                        }
                        if (verbose != null && verbose) {
                            arguments.add("-verbose");
                        }
                        if (debug == null || debug) {
                            arguments.add("-g");
                        } else {
                            arguments.add("-g:none");
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
                        if (output == null) {
                            throw new CafeError(Javac.class, "output");
                        }
                        Build mix = env.get(Build.class, 0);
                        File workingOutput = mix.relativize(output);
                        if (!(workingOutput.isDirectory() || workingOutput.mkdirs())) {
                            throw new Danger(Javac.class, "mkdirs", workingOutput);
                        }
                        arguments.add("-d");
                        arguments.add(workingOutput.getPath());
                        Collection<PathPart> parts = new ArrayList<PathPart>();
                        for (Artifact artifact : artifacts) {
                            parts.add(new ResolutionPart(artifact));
                        }
                        Project project = env.get(Project.class, 0);
                        Make make = env.get(Make.class, 1);
                        for (Dependency dependency : project.getRecipe(make.recipeName).getDependencies()) {
                            parts.addAll(dependency.getPathParts(project));
                        }
                        Set<File> classpath = PathParts.fileSet(env.library.resolve(parts));
                        if (!classpath.isEmpty()) {
                            arguments.add("-cp");
                            arguments.add(Files.path(classpath));
                        }
                        for (FindList.Entry entry : findList) {
                            Find find = entry.getFind();
                            if (!find.hasFilters()) {
                                find.include("**/*.java");
                            }
                            File directory = mix.relativize(entry.getDirectory());
                            env.debug(Javac.class, "sources", directory);
                            for (String fileName : find.find(directory)) {
                                arguments.add(new File(directory, fileName).toString());
                            }
                        }
                        env.debug(Javac.class, "arguments", arguments);
                        arguments.add(0, "javac");
                        
                        Exit exit = new Spawn().$(arguments).out(env.io.out).err(env.io.err).run();
                        
                        if (!exit.isSuccess()) {
                            throw new Danger(Javac.class, "invoke", mix.getWorkingDirectory(), arguments);
                        }
                    }
                });
            }
        };
        self.setSelf(this);
    }
    
    /**
     * Add an artifact to the Javac compilation classpath.
     * 
     * @param artifact
     *            The artifact.
     * @return This Javac builder to continue construction.
     */
    public Javac artifact(String artifact) {
        artifacts.add(new Artifact(artifact));
        return this;
    }

    // TODO Document.
    public FindStatement<Javac> source(File directory) {
        return new FindStatement<Javac>(this, findList, directory);
    }
    
    // TODO Document.
    public Javac output(File output) {
        this.output = output;
        return this;
    }

    // TODO Document.
    public Javac source(FindList findList) {
        findList.addAll(findList);
        return this;
    }
    
    // TODO Document.
    public Javac configure(JavacConfiguration...configurations) {
        return configure(Arrays.asList(configurations));
    }
    
    // TODO Document.
    public Javac configure(List<JavacConfiguration> configurations) {
        for (JavacConfiguration javacConfiguration : configurations) {
            javacConfiguration.configure(this);
        }
        return this;
    }
}
