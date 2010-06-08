package com.goodworkalan.mix.builder;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goodworkalan.mix.MixException;
import com.goodworkalan.mix.Production;
import com.goodworkalan.mix.Project;
import com.goodworkalan.mix.Recipe;
import com.goodworkalan.reflective.Reflective;
import com.goodworkalan.reflective.ReflectiveException;

/**
 * Root of a domain specific language used to specify recipies.
 * 
 * @author Alan Gutierrez
 */
public class Builder {
    /** The map of recipe names to recipes. */
    private final Map<String, Recipe> recipes = new HashMap<String, Recipe>();
    
    /** The map of unique keys to productions. */
    private final Map<List<String>, Production> artifacts = new HashMap<List<String>, Production>();

    /**
     * Create an empty project builder.
     */
    public Builder() {
    }

    /**
     * Create a recipe with the given name.
     * 
     * @param name
     *            The name of the recipe.
     * @return A recipe language element to specify project properties.
     */
    public RecipeBuilder recipe(String name) {
        return new RecipeBuilder(this, artifacts, recipes, name);
    }

    /**
     * Create a project in using the given working directory for the project
     * directory.
     * 
     * @param workingDirectory
     *            The working directory.
     * @return A new project containing the recipes defined by this builder.
     */
    public Project createProject(File workingDirectory) {
        return new Project(workingDirectory, artifacts, recipes);
    }
    
    /**
     * Create and apply a cookbook that defines a number of recipes.
     * 
     * @param <T> The cookbook type.
     * @param cookbookClass The cookbook class.
     * @return
     */
    public <T> T cookbook(Class<T> cookbookClass) {
        try {
            try {
                return cookbookClass.getConstructor(Builder.class).newInstance(Builder.this);
            } catch (Throwable e) {
                throw new ReflectiveException(Reflective.encode(e), e);
            }
        } catch (ReflectiveException e) {
            throw new MixException(Builder.class, "create.cookbook", e, cookbookClass.getCanonicalName());
        }
    }

    /**
     * End a statement in the domain specific language.
     * <p>
     * Exists for completeness, so that you can see in your IDE that there are
     * no parent builders to the project builder.
     */
    public void end() {
    }
}
