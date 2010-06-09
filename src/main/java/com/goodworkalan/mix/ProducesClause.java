package com.goodworkalan.mix;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.goodworkalan.go.go.library.Artifact;
import com.goodworkalan.mix.builder.RecipeStatement;

public class ProducesClause {
    private final RecipeStatement recipeElement;
    
    private final String recipe;

    private final Set<File> classes;
    
    private final Map<List<String>, Production> artifacts;
    
    public ProducesClause(RecipeStatement recipeElement, String recipe, Set<File> classes, Map<List<String>, Production> artifacts) {
        this.recipeElement = recipeElement;
        this.recipe = recipe;
        this.classes = classes;
        this.artifacts = artifacts;
    }
    
    public ProducesClause classes(File classesDirectory) {
        classes.add(classesDirectory);
        return this;
    }
    
    public ArtifactClause artifact(Artifact artifact) {
        return new ArtifactClause(this, recipe, artifacts, artifact);
    }
    
    public RecipeStatement end() {
        return recipeElement;
    }
}
