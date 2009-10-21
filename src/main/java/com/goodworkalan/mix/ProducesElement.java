package com.goodworkalan.mix;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.goodworkalan.go.go.DirectoryPart;
import com.goodworkalan.go.go.PathPart;
import com.goodworkalan.mix.builder.RecipeElement;

public class ProducesElement {
    private final RecipeElement recipeElement;
    
    private final String recipe;

    private final List<PathPart> produces;
    
    private final Map<List<String>, ArtifactSource> artifacts;
    
    public ProducesElement(RecipeElement recipeElement, String recipe, List<PathPart> produces, Map<List<String>, ArtifactSource> artifacts) {
        this.recipeElement = recipeElement;
        this.recipe = recipe;
        this.produces = produces;
        this.artifacts = artifacts;
    }
    
    public ProducesElement classes(File classesDirectory) {
        produces.add(new DirectoryPart(classesDirectory));
        return this;
    }
    
    public ArtifactElement artifact(String group, String name, String version) {
        return new ArtifactElement(this, recipe, artifacts, group, name, version);
    }
    
    public RecipeElement end() {
        return recipeElement;
    }
}
