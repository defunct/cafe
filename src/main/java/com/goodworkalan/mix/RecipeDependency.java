package com.goodworkalan.mix;

import java.util.ArrayList;
import java.util.Collection;

import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.go.go.InputOutput;
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
        Collection<PathPart> parts = new ArrayList<PathPart>();
        Recipe recipe = project.getRecipe(name);
        parts.addAll(recipe.getProduce());
        for (Dependency dependency : recipe.getDependencies()) {
            parts.addAll(dependency.getPathParts(project));
        }
        return parts;
    }

    /**
     * Create a collection of the unexpanded artifacts including all the
     * artifact dependencies of any recipes this recipe depends upon.
     * 
     * @param project
     *            The project.
     */
    public Collection<Artifact> getArtifacts(Project project) {
        Collection<Artifact> artifacts = new ArrayList<Artifact>();
        Recipe recipe = project.getRecipe(name);
        for (Dependency dependency : recipe.getDependencies()) {
            artifacts.addAll(dependency.getArtifacts(project));
        }
        return artifacts;
    }

    /**
     * Make the recipe by calling the make method of the given project.
     * 
     * @param io
     *            The input/output streams.
     * @param project
     *            The project.
     */
    public void make(InputOutput io, Project project) {
        project.make(io, name);
    }
}
