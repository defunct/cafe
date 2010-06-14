package com.goodworkalan.mix;

import java.util.Collection;

import com.goodworkalan.go.go.library.Include;
import com.goodworkalan.go.go.library.PathPart;

/**
 * A project dependency.
 * 
 * @author Alan Gutierrez
 */
public interface Dependency {
    /**
     * Get the part parts for the dependency.
     * 
     * @param project
     *            The project.
     * @return A collection of path parts.
     */
    public Collection<PathPart> getPathParts(Project project);

    /**
     * Get a collection of the immediate artifact dependencies.
     * 
     * @param project
     *            The project.
     * @return A collection of the immediate artifact dependencies.
     */
    public Collection<Include> getIncludes(Project project);

    /**
     * Get a collection of the immediate recipe dependencies.
     * 
     * @return A collection of the immediate artifact dependencies.
     */
    public Collection<String> getRecipeNames();
}
