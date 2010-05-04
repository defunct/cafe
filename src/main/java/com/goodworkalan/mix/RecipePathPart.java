package com.goodworkalan.mix;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import com.goodworkalan.go.go.library.DirectoryPart;
import com.goodworkalan.go.go.library.Exclude;
import com.goodworkalan.go.go.library.ExpandingPathPart;
import com.goodworkalan.go.go.library.Library;
import com.goodworkalan.go.go.library.PathPart;

public class RecipePathPart extends ExpandingPathPart {
    private final Project project;
    
    private final String recipeName;
    
    public RecipePathPart(Project project, String recipeName) {
        this.project = project;
        this.recipeName = recipeName;
    }
    
    public void expand(Library library, Collection<PathPart> expanded, Collection<PathPart> expand) {
        Recipe recipe = project.getRecipe(recipeName);
        for (Dependency dependency : recipe.getDependencies()) {
            expand.addAll(dependency.getPathParts(project));
        }
        for (File classes : recipe.getClasses()) {
            expanded.add(new DirectoryPart(classes.getAbsoluteFile()));
        }
    }

    public Object getUnversionedKey() {
        return project.getRecipe(recipeName);
    }
    
    public Set<Exclude> getExcludes() {
        return Collections.<Exclude>emptySet();
    }
}
