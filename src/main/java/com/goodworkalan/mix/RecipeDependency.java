package com.goodworkalan.mix;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.goodworkalan.go.go.Include;
import com.goodworkalan.go.go.PathPart;

// FIXME OutputDepdenency and RecipeDependency
/**
 * A recipe dependency that includes the recipe output and also includes the
 * dependencies of any recipes the given recipe depends upon.
 * 
 * @author Alan Gutierrez
 */
public class RecipeDependency implements Dependency {
    /** The recipe name. */
    private final String name;

    /**
     * Create a recipe dependency for the recipe with the given name.
     * 
     * @param name
     *            The recipe name.
     */
    public RecipeDependency(String name) {
        this.name = name;
    }

    /**
     * Create a collection of unexpanded path parts that includes the path parts
     * of recipe productions and the path parts of all of the recipe
     * dependencies.
     * 
     * @param project
     *            The project.
     */
    public Collection<PathPart> getPathParts(Project project) {
        return Collections.<PathPart>singletonList(new RecipePathPart(project, name));
    }

    /**
     * Create a collection of the unexpanded artifacts including all the
     * artifact dependencies of any recipes this recipe depends upon.
     * 
     * @param project
     *            The project.
     */
    public Collection<Include> getIncludes(Project project) {
        Collection<Include> artifacts = new ArrayList<Include>();
        Recipe recipe = project.getRecipe(name);
        for (Dependency dependency : recipe.getDependencies()) {
            artifacts.addAll(dependency.getIncludes(project));
        }
        return artifacts;
    }

    public Collection<String> getRecipes(Project project) {
        return Collections.singleton(name);
    }
}
