package com.goodworkalan.mix;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Root of a domain specific language used to specify recipies.
 * 
 * @author Alan Gutierrez
 */
public class Builder {
    private final Map<String, Recipe> recipes = new HashMap<String, Recipe>();
    
    /**
     * Create a recipe with the given name.
     * 
     * @param name
     *            The name of the recipe.
     * @return A recipe language element to specify project properties.
     */
    public RecipeElement recipe(String name) {
        return new RecipeElement(this, recipes, name);
    }

    public ProvidesElement provides(String group, String name, String version) {
        return null;
    }
    
    public Project createProject(File workingDirectory) {
        return new Project(workingDirectory, recipes);
    }
}
