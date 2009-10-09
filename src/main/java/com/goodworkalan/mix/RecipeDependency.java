package com.goodworkalan.mix;

import java.util.ArrayList;
import java.util.Collection;

import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.go.go.PathPart;

// FIXME OutputDepdenency and RecipeDependency
public class RecipeDependency implements Dependency {
    private final String recipeName;
    
    public RecipeDependency(String recipeName) {
        this.recipeName = recipeName;
    }

    public Collection<PathPart> getPathParts(Project project) {
        Collection<PathPart> parts = new ArrayList<PathPart>();
        Recipe recipe = project.getRecipe(recipeName);
        parts.addAll(recipe.getProduce());
        for (Dependency dependency : recipe.getDependencies()) {
            parts.addAll(dependency.getPathParts(project));
        }
        return parts;
    }
    
    public Collection<Artifact> getArtifacts(Project project) {
        Collection<Artifact> artifacts = new ArrayList<Artifact>();
        Recipe recipe = project.getRecipe(recipeName);
        for (Dependency dependency : recipe.getDependencies()) {
            artifacts.addAll(dependency.getArtifacts(project));
        }
        return artifacts;
    }
    
    public void make(Project project) {
        project.make(recipeName);
    }
}
