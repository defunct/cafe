package com.goodworkalan.mix.builder;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goodworkalan.mix.MixException;
import com.goodworkalan.mix.Production;
import com.goodworkalan.mix.Project;
import com.goodworkalan.mix.Recipe;
import com.goodworkalan.reflective.Reflection;

/**
 * Root of a domain specific language used to specify recipies.
 * 
 * @author Alan Gutierrez
 */
public class Builder {
    private final Map<String, Recipe> recipes = new HashMap<String, Recipe>();
    
    private final Map<List<String>, Production> artifacts = new HashMap<List<String>, Production>();

    public Builder() {
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
    
    public <T> T cookbook(final Class<T> cookbookClass) {
        return MixException.reflect(new Reflection<T>() {
            public T reflect() throws SecurityException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
                return cookbookClass.getConstructor(Builder.class).newInstance(Builder.this);
            }
        }, Builder.class, "create.cookbook",  cookbookClass.getCanonicalName());
    }
    
    public void end() {
    }
}
