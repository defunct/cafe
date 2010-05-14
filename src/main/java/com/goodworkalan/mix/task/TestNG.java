package com.goodworkalan.mix.task;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.goodworkalan.comfort.io.Files;
import com.goodworkalan.comfort.io.Find;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.library.Artifact;
import com.goodworkalan.go.go.library.DirectoryPart;
import com.goodworkalan.go.go.library.PathPart;
import com.goodworkalan.go.go.library.PathParts;
import com.goodworkalan.go.go.library.ResolutionPart;
import com.goodworkalan.mix.Dependency;
import com.goodworkalan.mix.FindList;
import com.goodworkalan.mix.Make;
import com.goodworkalan.mix.MixError;
import com.goodworkalan.mix.Project;
import com.goodworkalan.mix.builder.Executable;
import com.goodworkalan.mix.builder.RecipeElement;
import com.goodworkalan.spawn.Exit;
import com.goodworkalan.spawn.Spawn;

public class TestNG {
    private final FindList findList = new FindList();
    
    private final List<File> classes = new ArrayList<File>();
    
    private final List<Artifact> artifacts = new ArrayList<Artifact>();
    
    private final Map<String, String> systemProperties = new LinkedHashMap<String, String>();
    
    private final RecipeElement recipeElement;
    
    private File output;
    
    public TestNG(RecipeElement recipeElement) {
        this.recipeElement = recipeElement;
    }

    public TestNG classes(File directory) {
        classes.add(directory);
        return this;
    }
    
    public TestNG artifact(Artifact artifact) {
        artifacts.add(artifact);
        return this;
    }
    
    public FindElement<TestNG> source(File directory) {
        return new FindElement<TestNG>(this, findList, directory);
    }
    
    public TestNG define(String name, String value) {
        systemProperties.put(name, value);
        return this;
    }
    
    public TestNG output(File output) {
        this.output = output;
        return this;
    }

    public RecipeElement end() {
        recipeElement.executable(new Executable() {
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
                
                System.out.println(arguments);
                
                ProcessBuilder newProcess = new ProcessBuilder();
                newProcess.command().addAll(arguments);
                
                Exit exit = new Spawn().$(arguments).out(env.io.out).err(env.io.err).run();
                
                if (exit.code != 0) {
                    throw new MixError(TestNG.class, "failure", exit);
                }
            }
        });
        return recipeElement;
    }
}
