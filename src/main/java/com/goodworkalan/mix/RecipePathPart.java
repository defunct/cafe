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

/**
 * An expanding path part that expands to include a recipes output and all of
 * its dependencies.
 * 
 * @author Alan Gutierrez
 */
public class RecipePathPart extends ExpandingPathPart {
    /** The project. */
    private final Project project;
    
    /** The recipe name. */
    private final String recipeName;

    /**
     * Create a recipe path part with the given project and recipe name.
     * 
     * @param project
     *            The project.
     * @param recipeName
     *            The recipe name.
     */
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

    /**
     * Get an unversioned key for the recipe path part that will never be
     * matched by the library. Unexpanded parts are never kept, and because this
     * expanded part has no directly parallel expanded part, we'll always have
     * to expand it anyway.
     * 
     * @return The unversioned key.
     */
    public Object getUnversionedKey() {
        return new Object();
    }
    
    public Set<Exclude> getExcludes() {
        return Collections.<Exclude>emptySet();
    }
}
