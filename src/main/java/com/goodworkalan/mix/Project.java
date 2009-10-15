package com.goodworkalan.mix;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.goodworkalan.go.go.CommandPart;
import com.goodworkalan.go.go.Executor;
import com.goodworkalan.go.go.InputOutput;

/**
 * Describes a software project, its source files, outputs and dependencies.
 * 
 * @author Alan Gutierrez
 */
public class Project {
    /** The working directory for the project. */
    private final File workingDirectory;

    /** The map of recipes indexed by recipe name. */
    private final Map<String, Recipe> recipes;

    /** The executor for the current execution. */
    private final Executor executor;

    /** The root command part. */
    private final CommandPart mix;

    /** The set of recipes that have been made during this execution. */
    private final Set<String> made = new HashSet<String>();
    
    private final SortedMap<List<String>, ArtifactSource> artifacts = new TreeMap<List<String>, ArtifactSource>(new StringListComparator());

    /**
     * Create a project.
     * 
     * @param workingDirectory
     *            The working directory for the project.
     * @param recipes
     *            The map of recipes indexed by recipe name.
     * @param artifacts
     *            A map of artifacts to their directories.
     * @param executor
     *            The executor for the current execution.
     * @param mix
     *            The root command part.
     */
    public Project(File workingDirectory, Map<List<String>, ArtifactSource> artifacts, Map<String, Recipe> recipes, Executor executor, CommandPart mix) {
        this.workingDirectory = workingDirectory;
        this.recipes = recipes;
        this.executor = executor;
        this.artifacts.putAll(artifacts);
        this.mix = mix;
    }

    /**
     * Get the working directory.
     * 
     * @return The working directory.
     */
    public File getWorkingDirectory() {
        return workingDirectory;
    }

    /**
     * Make the given recipe if it has not already been made.
     * 
     * @param io
     *            The input/output streams.
     * @param name
     *            The name of the recipe to make.
     */
    public void make(InputOutput io, String name) {
        Recipe recipe = recipes.get(name);
        if (!made.contains(name)) {
            made.add(name);
            for (Dependency dependency : recipe.getDependencies()) {
                dependency.make(io, this);
            }
            for (List<String> step : recipe.getCommands()) {
                CommandPart next = mix.extend(step);
                if (String.class.equals(next.getArgumentTypes().get("recipe"))) {
                    next = next.argument("recipe", name);
                }
                executor.execute(io, next);
            }
        }
    }
    
    /**
     * Get a copy of the list of recipes in this project.
     * 
     * @return A copy of the list of recipes.
     */
    public List<Recipe> getRecipes() {
        return new ArrayList<Recipe>(recipes.values());
    }

    public Recipe getRecipe(String name) {
        return recipes.get(name);
    }
    
    public List<ArtifactSource> getArtifactSources() {
        return new ArrayList<ArtifactSource>(artifacts.values());
    }
    
    public List<ArtifactSource> getArtifactSources(String pattern) {
        String[] parts = pattern.split("/");
        if (parts.length > 3) {
            throw new MixException(0);
        }
        List<String> fromKey = Arrays.asList(pattern);
        if (fromKey.size() != 1) {
            fromKey.add(0, fromKey.remove(1));
        }
        List<ArtifactSource> sources = new ArrayList<ArtifactSource>();
        for (Map.Entry<List<String>, ArtifactSource> entry : artifacts.tailMap(fromKey).entrySet()) {
            for (int i = 0, stop = fromKey.size(); i < stop; i++) {
                if (!fromKey.get(i).equals(entry.getKey().get(i))) {
                    return sources;
                }
            }
            sources.add(entry.getValue());
        }
        return sources;
    }
}
