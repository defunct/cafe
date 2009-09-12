package com.goodworkalan.mix;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.goodworkalan.glob.Find;

/**
 * Describes a software project, its source files, outputs and dependencies.
 * 
 * @author Alan Gutierrez
 */
public class Project {
    private final Map<String, Recipe> recipes;
    private final File workingDirectory;

    public Project(File workingDirectory, Map<String, Recipe> recipes) {
        this.workingDirectory = workingDirectory;
        this.recipes = recipes;
    }
    
    public List<List<String>> getCommands(String recipe) {
        return recipes.get(recipe).getCommands();
    }

    public List<Dependency> getDependencies(String recipe) {
        return recipes.get(recipe).getDependencies();
    }

    /**
     * Get the set of source files for the given directory.
     * 
     * @return The set of source files for the given directory.
     */
    public Set<File> getSourceDirectories() {
        return Collections.singleton(new File(workingDirectory, "src/main/java"));
    }

    /**
     * Get the set of source files for the given source directory.
     * 
     * @return A set of source files.
     */
    public Set<File> getSources(File directory) {
        return new Find().include("**/*.java").find(directory);
    }

    /**
     * Get the output directory.
     * 
     * @return The output directory.
     */
    public File getOutputDirectory() {
        return new File(workingDirectory, "target");
    }
}
