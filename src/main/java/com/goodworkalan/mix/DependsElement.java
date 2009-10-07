package com.goodworkalan.mix;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.goodworkalan.go.go.Artifact;

public class DependsElement {
    private final RecipeElement recipeElement;
    
    private final Map<List<String>, Dependency> dependencies; 
    
    public DependsElement(RecipeElement recipeElement, Map<List<String>, Dependency> dependencies) {
        this.recipeElement = recipeElement;
        this.dependencies = dependencies;
    }

    // FIXME Rename classes.
    public DependsElement source(String name) {
        if (!dependencies.containsKey(name)) {
            dependencies.put(Collections.singletonList(name), new RecipeDependency(name));
        }
        return this;
    }
    
    public DependsElement sibling(String group, String name, String version) {
        return this;
    }

    public DependsElement artifact(String group, String name, String version) {
        Artifact artifact = new Artifact(group, name, version);
        List<String> key = artifact.getKey().subList(0, 2);
        if (!dependencies.containsKey(key)) {
            dependencies.put(key, new ArtifactDependency(artifact));
        }
        return this;
    }
    
    public RecipeElement end() {
        return recipeElement;
    }
}
