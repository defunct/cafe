package com.goodworkalan.mix.builder;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goodworkalan.mix.Production;
import com.goodworkalan.mix.MixException;
import com.goodworkalan.mix.Project;
import com.goodworkalan.mix.Recipe;
import com.goodworkalan.reflective.ReflectiveException;
import com.goodworkalan.reflective.ReflectiveFactory;

/**
 * Root of a domain specific language used to specify recipies.
 * 
 * @author Alan Gutierrez
 */
public class Builder {
    private final ReflectiveFactory reflectiveFactory;
    
    private final Map<String, Recipe> recipes = new HashMap<String, Recipe>();
    
    private final Map<List<String>, Production> artifacts = new HashMap<List<String>, Production>();

    public Builder() {
        this(new ReflectiveFactory());
    }

    Builder(ReflectiveFactory reflectiveFactory) {
        this.reflectiveFactory = reflectiveFactory;
    }

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

    public Project createProject(File workingDirectory) {
        return new Project(workingDirectory, artifacts, recipes);
    }
    
    public <T> T cookbook(Class<T> cookbookClass) {
        try {
            return reflectiveFactory.getConstructor(cookbookClass, Builder.class).newInstance(this);
        } catch (ReflectiveException e) {
            throw new MixException(Builder.class, "create.cookbook", e, cookbookClass.getCanonicalName());
        }
    }
    
    public void end() {
    }
}
