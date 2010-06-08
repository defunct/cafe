package com.goodworkalan.mix;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.goodworkalan.go.go.library.Artifact;
import com.goodworkalan.mix.builder.RecipeStatement;

public class ProducesElement {
    private final RecipeStatement recipeElement;
    
    private final String recipe;

    private final Set<File> classes;
    
    private final Map<List<String>, Production> artifacts;
    
    public ProducesElement(RecipeStatement recipeElement, String recipe, Set<File> classes, Map<List<String>, Production> artifacts) {
        this.recipeElement = recipeElement;
        this.recipe = recipe;
        this.classes = classes;
        this.artifacts = artifacts;
    }
    
    public ProducesElement classes(File classesDirectory) {
        classes.add(classesDirectory);
        return this;
    }
    
    public ArtifactElement artifact(Artifact artifact) {
        return new ArtifactElement(this, recipe, artifacts, artifact);
    }
    
    public RecipeStatement end() {
        return recipeElement;
    }
}
