package com.goodworkalan.mix;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.goodworkalan.glob.Files;
import com.goodworkalan.glob.Find;
import com.goodworkalan.go.go.Argument;
import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.Library;
import com.goodworkalan.go.go.Task;
import com.goodworkalan.spawn.Redirect;
import com.goodworkalan.spawn.Spawn;

@Command(name = "test-ng", parent = MixTask.class)
public class TestNGTask extends Task {
    private String recipe;
    
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
    public void execute(Environment environment) {
        Project project = configuration.getProject();
        
        List<String> arguments = new ArrayList<String>();
        
        arguments.add("java");
        Set<File> classpath = new LinkedHashSet<File>();
        if (recipe != null) {
            Library library = new Library(new File(System.getProperty("user.home") + "/.m2/repository"));
            for (Dependency dependency : project.getDependencies(recipe)) {
                classpath.addAll(dependency.getFiles(project, library));
            }
        }
        arguments.add("-classpath");
        arguments.add(Files.path(classpath));
        arguments.add("org.testng.TestNG");
        arguments.add("-testclass");
        arguments.add("com.goodworkalan.mix.CompileTest");
        ProcessBuilder newProcess = new ProcessBuilder();
        newProcess.command().addAll(arguments);
        
        Spawn<Redirect, Redirect> spawn;
        spawn = Spawn.spawn(new Redirect(environment.out), new Redirect(environment.err));

        spawn.getProcessBuilder().command().addAll(arguments);
        spawn.execute();
    }
}
