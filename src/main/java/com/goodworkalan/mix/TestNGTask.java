package com.goodworkalan.mix;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.goodworkalan.glob.Files;
import com.goodworkalan.glob.Find;
import com.goodworkalan.go.go.Argument;
import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.Library;
import com.goodworkalan.go.go.PathPart;
import com.goodworkalan.go.go.Task;
import com.goodworkalan.spawn.Redirect;
import com.goodworkalan.spawn.Spawn;

// FIXME Can you do the right thing with the camel case name?
@Command(name = "test-ng", parent = MixTask.class)
public class TestNGTask extends Task {
    private String recipe;
    
    private final FindList findList = new FindList();
    
    @Argument
    public void addRecipe(String recipe) {
        this.recipe = recipe;
    }
    
    @Argument
    public void addSourceDirectory(File sourceDirectory) {
        findList.addDirectory(sourceDirectory);
    }

    @Argument
    public void addInclude(String string) {
        findList.addInclude(string);
    }
    
    @Argument
    public void addExclude(String string) {
        findList.addExclude(string);
    }

    /** The Mix configuration. */
    private MixTask.Configuration configuration;

    /**
     * Set the Mix configuration.
     * 
     * @param configuration
     *            The Mix configuration.
     */
    public void setConfiguration(MixTask.Configuration configuration) {
        this.configuration = configuration;
    }
    
    /**
     * Execute the TestNG tests.
     */
    @Override
    public void execute(Environment env) {
        Project project = configuration.getProject();
        
        List<String> arguments = new ArrayList<String>();
        
        Set<File> classpath = new LinkedHashSet<File>();
        if (recipe != null) {
            Collection<PathPart> parts = new ArrayList<PathPart>();
            for (Dependency dependency : project.getRecipe(recipe).getDependencies()) {
                parts.addAll(dependency.getPathParts(project));
            }
            Library library = env.part.getCommandInterpreter().getLibrary();
            classpath.addAll(library.resolve(parts, new HashSet<Object>()).getFiles());
        }
        
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
        arguments.add("org.testng.TestNG");
        arguments.add("-testclass");
        arguments.addAll(testClasses);
        
        ProcessBuilder newProcess = new ProcessBuilder();
        newProcess.command().addAll(arguments);
        
        Spawn<Redirect, Redirect> spawn;
        spawn = Spawn.spawn(new Redirect(env.io.out), new Redirect(env.io.err));

        spawn.getProcessBuilder().command().addAll(arguments);

        if (spawn.execute() != 0) {
            throw new MixError(0);
        }
    }
}
