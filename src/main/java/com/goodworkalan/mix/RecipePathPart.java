package com.goodworkalan.mix;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.go.go.DirectoryPart;
import com.goodworkalan.go.go.Library;
import com.goodworkalan.go.go.PathPart;

public class RecipePathPart implements PathPart {
    private final Project project;
    
    private final String recipeName;
    
    public RecipePathPart(Project project, String recipeName) {
        this.project = project;
        this.recipeName = recipeName;
    }
    
    public Collection<PathPart> expand(Library library, Collection<PathPart> expand) {
        Recipe recipe = project.getRecipe(recipeName);
        for (Dependency dependency : recipe.getDependencies()) {
            expand.addAll(dependency.getPathParts(project));
        }
        List<PathPart> parts = new ArrayList<PathPart>();
        for (File classes : recipe.getClasses()) {
            parts.add(new DirectoryPart(classes));
        }
        return parts;
    }

    public Artifact getArtifact() {
        throw new UnsupportedOperationException();
    }

    public File getFile() {
        throw new UnsupportedOperationException();
    }

    public Object getUnversionedKey() {
        return project.getRecipe(recipeName);
    }

    public URL getURL() throws MalformedURLException {
        throw new UnsupportedOperationException();
    }
}
