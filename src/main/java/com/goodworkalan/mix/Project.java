package com.goodworkalan.mix;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

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
     */
    public Project(File workingDirectory, Map<List<String>, ArtifactSource> artifacts, Map<String, Recipe> recipes) {
        this.workingDirectory = workingDirectory;
        this.recipes = recipes;
        this.artifacts.putAll(artifacts);
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
