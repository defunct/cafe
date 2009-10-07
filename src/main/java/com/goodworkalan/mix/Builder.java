package com.goodworkalan.mix;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goodworkalan.go.go.CommandPart;
import com.goodworkalan.go.go.Executor;

/**
 * Root of a domain specific language used to specify recipies.
 * 
 * @author Alan Gutierrez
 */
public class Builder {
    private final Map<String, Recipe> recipes = new HashMap<String, Recipe>();
    
    private final Map<List<String>, ArtifactSource> artifacts = new HashMap<List<String>, ArtifactSource>();

    /**
     * Create a recipe with the given name.
     * 
     * @param name
     *            The name of the recipe.
     * @return A recipe language element to specify project properties.
     */
    public RecipeElement recipe(String name) {
        return new RecipeElement(this, artifacts, recipes, name);
    }

    public ProvidesElement provides(String group, String name, String version) {
        return null;
    }
    
    public Project createProject(File workingDirectory, Executor executor, CommandPart mix) {
        return new Project(workingDirectory, artifacts, recipes, executor, mix);
    }
    
    public void end() {
    }
}
