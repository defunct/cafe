package com.goodworkalan.mix;

import java.util.Map;

import com.goodworkalan.go.go.Artifact;

public class DependsElement {
    private final RecipeElement recipeElement;
    
    private final Map<String, Dependency> dependencies; 
    
    public DependsElement(RecipeElement recipeElement, Map<String, Dependency> dependencies) {
        this.recipeElement = recipeElement;
        this.dependencies = dependencies;
    }

    public DependsElement source(String name, String output) {
        return this;
    }
    
    public DependsElement sibling(String group, String name, String version) {
        return this;
    }

    public DependsElement artifact(String group, String name, String version) {
        Artifact artifact = new Artifact(group, name, version);
        if (!dependencies.containsKey(artifact.getKey())) {
            dependencies.put(artifact.getKey(), new ArtifactDependency(artifact));
        }
        return this;
    }
    
    public RecipeElement end() {
        return recipeElement;
    }
}
