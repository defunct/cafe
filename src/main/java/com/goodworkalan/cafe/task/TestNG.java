package com.goodworkalan.cafe.task;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.goodworkalan.cafe.Dependency;
import com.goodworkalan.cafe.Make;
import com.goodworkalan.cafe.MixError;
import com.goodworkalan.cafe.Project;
import com.goodworkalan.cafe.builder.FindList;
import com.goodworkalan.cafe.builder.FindStatement;
import com.goodworkalan.cafe.builder.RecipeStatement;
import com.goodworkalan.comfort.io.Files;
import com.goodworkalan.comfort.io.Find;
import com.goodworkalan.go.go.Commandable;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.library.Artifact;
import com.goodworkalan.go.go.library.DirectoryPart;
import com.goodworkalan.go.go.library.PathPart;
import com.goodworkalan.go.go.library.PathParts;
import com.goodworkalan.go.go.library.ResolutionPart;
import com.goodworkalan.spawn.Exit;
import com.goodworkalan.spawn.Spawn;

// TODO Document.
public class TestNG {
    // TODO Document.
    private final FindList findList = new FindList();
    
    // TODO Document.
    private final List<File> classes = new ArrayList<File>();
    
    // TODO Document.
    private final List<Artifact> artifacts = new ArrayList<Artifact>();
    
    // TODO Document.
    private final Map<String, String> systemProperties = new LinkedHashMap<String, String>();
    
    // TODO Document.
    private final RecipeStatement recipeElement;
    
    // TODO Document.
    private File output;
    
    // TODO Document.
    public TestNG(RecipeStatement recipeElement) {
        this.recipeElement = recipeElement;
    }

    // TODO Document.
    public TestNG classes(File directory) {
        classes.add(directory);
        return this;
    }
    
    // TODO Document.
    public TestNG artifact(Artifact artifact) {
        artifacts.add(artifact);
        return this;
    }
    
    // TODO Document.
    public FindStatement<TestNG> source(File directory) {
        return new FindStatement<TestNG>(this, findList, directory);
    }
    
    // TODO Document.
    public TestNG define(String name, String value) {
        systemProperties.put(name, value);
        return this;
    }
    
    // TODO Document.
    public TestNG output(File output) {
        this.output = output;
        return this;
    }

    // TODO Document.
    public RecipeStatement end() {
        recipeElement.executable(new Commandable() {
            public void execute(Environment env) {
                TestNGCommand additional = env.executor.run(TestNGCommand.class, env.io, "mix", env.arguments.get(0), "test-ng", env.arguments.get(1));

                for (Map.Entry<String, String> entry : additional.defines.entrySet()) {
                    define(entry.getKey(), entry.getValue());
                }
                
                classes.addAll(0, additional.classes);
                
                artifacts.addAll(0, additional.artifacts);
                
                List<String> arguments = new ArrayList<String>();
                
                Collection<PathPart> parts = new ArrayList<PathPart>();
                for (File directory : classes) {
                    parts.add(new DirectoryPart(directory.getAbsoluteFile()));
                }
                for (Artifact artifact : artifacts) {
                    parts.add(new ResolutionPart(artifact));
                }
                Project project = env.get(Project.class, 0);
                Make make = env.get(Make.class, 1);
                for (Dependency dependency : project.getRecipe(make.recipeName).getDependencies()) {
                    parts.addAll(dependency.getPathParts(project));
                }
                Set<File> classpath = PathParts.fileSet(env.library.resolve(parts));
                
                List<String> testClasses = new ArrayList<String>();
                for (FindList.Entry entry : findList) {
                    Find find = entry.getFind();
                    if (!find.hasFilters()) {
                        find.include("**/*Test.java");
                    }
                    for (String className : find.find(entry.getDirectory())) {
                        className = className.replace("/", ".");
                        className = className.substring(0, className.length() - ".java".length());
                        testClasses.add(className);
                    }
                }
                
                arguments.add("java");
                arguments.add("-classpath");
                arguments.add(Files.path(classpath));
                
                for (Map.Entry<String, String> entry : systemProperties.entrySet()) {
                    arguments.add("-D" + entry.getKey() + "=" + entry.getValue());
                }

                arguments.add("-ea");
                arguments.add("org.testng.TestNG");
                
                if (output != null) {
                    arguments.add("-d");
                    arguments.add(output.toString());
                }

                arguments.add("-testclass");
                arguments.addAll(testClasses);
                
                Exit exit = new Spawn().$(arguments).out(env.io.out).err(env.io.err).run();
                
                if (exit.code != 0) {
                    throw new MixError(TestNG.class, "failure", exit);
                }
            }
        });
        return recipeElement;
    }
}
