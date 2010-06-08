package com.goodworkalan.mix;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.goodworkalan.go.go.Commandable;

public class Recipe {
    private final List<Commandable> program;
    
    private final Map<List<String>, Dependency> dependencies;
    
    private final Set<File> classes;
    
    /** The list of rebuild test conditions for the recipe. */
    private final List<Rebuild> rebuilds;

    /**
     * Create a new recipe.
     * 
     * @param program
     *            The list of executables.
     * @param dependencies
     *            A map of dependencies.
     * @param classes
     *            The list of output class directories.
     * @param rebuilds
     *            The list of rebuild test conditions for the recipe.
     */
    public Recipe(List<Commandable> program, Map<List<String>, Dependency> dependencies, Set<File> classes, List<Rebuild> rebuilds) {
        this.program = program;
        this.dependencies = dependencies;
        this.classes = classes;
        this.rebuilds = rebuilds;
    }

    /**
     * Get the list of rebuild test conditions for the recipe.
     * 
     * @return The list of rebuild test conditions for the recipe.
     */
    public List<Rebuild> getRebuilds() {
        return rebuilds;
    }
    
    public List<Commandable> getProgram() {
        return program;
    }
    
    public List<Dependency> getDependencies() {
        return new ArrayList<Dependency>(dependencies.values());
    }
    
    public Set<File> getClasses() {
        return classes;
    }
}
