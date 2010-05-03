package com.goodworkalan.mix;

import java.util.Collection;

import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.library.Include;
import com.goodworkalan.go.go.library.PathPart;

/**
 * A project dependency.
 * 
 * @author Alan Gutierrez
 */
public interface Dependency {
    /**
     * Get the part parts for the dependency. The path parts are to be used to
     * create a <code>LibraryPath</code>.
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
     * @param project
     *            The project.
     * @return A collection of the immediate artifact dependencies.
     */
    public Collection<String> getRecipes(Project project);
    
    public void build(Mix mix, Environment env);
}
