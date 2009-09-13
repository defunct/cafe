package com.goodworkalan.mix;

import java.io.File;
import java.util.List;

import com.goodworkalan.go.go.DirectoryPart;
import com.goodworkalan.go.go.PathPart;

public class ProducesElement {
    private final RecipeElement recipeElement;

    private final List<PathPart> produces;
    
    public ProducesElement(RecipeElement recipeElement, List<PathPart> produces) {
        this.recipeElement = recipeElement;
        this.produces = produces;
    }
    
    public ProducesElement classes(String classesDirectory) {
        produces.add(new DirectoryPart(new File(classesDirectory)));
        return this;
    }
    
    public RecipeElement end() {
        return recipeElement;
    }
}
