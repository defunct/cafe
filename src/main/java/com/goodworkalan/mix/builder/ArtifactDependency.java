package com.goodworkalan.mix.builder;

import java.util.Collection;
import java.util.Collections;

import com.goodworkalan.go.go.library.Include;
import com.goodworkalan.go.go.library.PathPart;
import com.goodworkalan.go.go.library.ResolutionPart;
import com.goodworkalan.mix.Dependency;
import com.goodworkalan.mix.Project;

/**
 * A single artifact dependency.
 * 
 * @author Alan Gutierrez
 */
class ArtifactDependency implements Dependency {
    /** The artifact and its exclusions. */
    private final Include include;

    /**
     * Create a single artifact dependency.
     * 
     * @param include
     *            The artifact and its exclusions.
     */
    public ArtifactDependency(Include include) {
        this.include = include;
    }

    /**
     * Get a collection that contains a single unresolved path part for the
     * artifact.
     * 
     * @param project
     *            The project.
     */
    public Collection<PathPart> getPathParts(Project project) {
        return Collections.<PathPart>singletonList(new ResolutionPart(include));
    }

    /**
     * Get a collection that contains the single artifact.
     * 
     * @param project
     *            The project.
     */
    public Collection<Include> getIncludes(Project project) {
        return Collections.singleton(include);
    }

    /**
     * Return an empty collection indicating no dependent recipes.
     * 
     * @return An empty list.
     */
    public Collection<String> getRecipeNames() {
        return Collections.emptyList();
    }
}
