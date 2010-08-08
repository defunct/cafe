package com.goodworkalan.cafe.builder;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.goodworkalan.cafe.Production;
import com.goodworkalan.go.go.library.Artifact;

// TODO Document.
public class ProducesClause {
    // TODO Document.
    private final RecipeStatement recipeElement;
    
    // TODO Document.
    private final String recipe;

    // TODO Document.
    private final Set<File> classes;
    
    // TODO Document.
    private final Map<List<String>, Production> artifacts;
    
    // TODO Document.
    public ProducesClause(RecipeStatement recipeElement, String recipe, Set<File> classes, Map<List<String>, Production> artifacts) {
        this.recipeElement = recipeElement;
        this.recipe = recipe;
        this.classes = classes;
        this.artifacts = artifacts;
    }
    
    // TODO Document.
    public ProducesClause classes(File classesDirectory) {
        classes.add(classesDirectory);
        return this;
    }
    
    // TODO Document.
    public ArtifactClause artifact(Artifact artifact) {
        return new ArtifactClause(this, recipe, artifacts, artifact);
    }
    
    // TODO Document.
    public RecipeStatement end() {
        return recipeElement;
    }
}
