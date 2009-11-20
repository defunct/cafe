package com.goodworkalan.mix.builder;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.goodworkalan.mix.ArtifactSource;
import com.goodworkalan.mix.Dependency;
import com.goodworkalan.mix.MixException;
import com.goodworkalan.mix.ProducesElement;
import com.goodworkalan.mix.Recipe;
import com.goodworkalan.mix.RecipeModule;
import com.goodworkalan.reflective.ReflectiveException;
import com.goodworkalan.reflective.ReflectiveFactory;

public class RecipeElement {
    private final ReflectiveFactory reflectiveFactory = new ReflectiveFactory();

    private final Builder builder;
    
    private final String name;
    
    private final Map<String, Recipe> recipes;
    
    private final List<Executable> program = new ArrayList<Executable>();
    
    private final Map<List<String>, Dependency> dependencies = new LinkedHashMap<List<String>, Dependency>();
    
    private final Set<File> classes = new LinkedHashSet<File>();
    
    private final Map<List<String>, ArtifactSource> artifacts; 
    
    public RecipeElement(Builder builder, Map<List<String>, ArtifactSource> artifacts, Map<String, Recipe> recipes, String name) {
        this.builder = builder;
        this.recipes = recipes;
        this.artifacts = artifacts;
        this.name = name;
    }
    
    public RecipeElement make(String recipe) {
        return this;
    }
    
    public void addExecutable(Executable executable) {
        program.add(executable);
    }
    
    /** Here's an idea on reuse and extension. */
    public RecipeElement reset() {
        return this;
    }

    /**
     * Call the configure method of the given recipe module passing this recipe
     * element.
     * 
     * @param recipeModule
     *            A pre-defined recipe.
     * @return This recipe language element in order to continue to specify
     *         recipe properties.
     */
    public RecipeElement apply(RecipeModule recipeModule) {
        recipeModule.configure(this);
        return this;
    }

    public <T> T task(Class<T> taskClass) {
        try {
            return reflectiveFactory.getConstructor(taskClass, RecipeElement.class).newInstance(this);
        } catch (ReflectiveException e) {
            throw new MixException(0, e);
        }
    }

    /**
     * Specify the dependencies for this recipe.
     * 
     * @return A depends language element to specify project dependencies.
     */
    public DependsElement<RecipeElement> depends() {
        return new DependsElement<RecipeElement>(this, dependencies);
    }
    
    public ProducesElement produces() {
        return new ProducesElement(this, name, classes, artifacts);
    }

    public Builder end() {
        recipes.put(name, new Recipe(program, dependencies, classes));
        return builder;
    }
}
