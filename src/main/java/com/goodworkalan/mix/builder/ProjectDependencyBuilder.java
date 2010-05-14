package com.goodworkalan.mix.builder;

import java.util.List;
import java.util.Map;

import com.goodworkalan.go.go.library.Include;
import com.goodworkalan.mix.Dependency;

/**
 * Builds the set of project dependencies. This is a shorthand for projects that
 * customize nothing about the default project and only specify their
 * dependencies.
 * 
 * @author Alan Gutierrez
 */
public class ProjectDependencyBuilder {
    /** The parent Java project builder. */
    private final JavaProject project;

    /** The map of unversioned artifact keys to production dependency definitions. */
    private final Map<List<String>, Dependency> production;
    
    /** The map of unversioned artifact keys to development dependency definitions. */
    private final Map<List<String>, Dependency> development;

    /**
     * Create a project dependency builder that records dependencies in the
     * given maps of production and development productions.
     * 
     * @param project
     *            The parent Java project builder.
     * @param production
     *            The map of unversioned artifact keys to production dependency
     *            definitions.
     * @param development
     *            The map of unversioned artifact keys to development dependency
     *            definitions.
     */
    public ProjectDependencyBuilder(JavaProject project, Map<List<String>, Dependency> production, Map<List<String>, Dependency> development) {
        this.project = project;
        this.production = production;
        this.development = development;
    }

    /**
     * Add a single artifact dependency that will be used to build all of the
     * primary source code, and will be included in the list of production
     * dependencies. The given list of excludes specify which of the artifacts
     * in the given artifacts dependencies to exclude by artifact group and
     * name.
     * 
     * @param artifact
     *            The artifact to add to the project.
     * @param excludes
     *            The artifacts to exclude from the project.
     * @return This project builder to continue building the project.
     */
    public ProjectDependencyBuilder production(String artifact, String...excludes) {
        Include include = new Include(artifact, excludes);
        List<String> key = include.getArtifact().getUnversionedKey();
        if (!production.containsKey(key)) {
            production.put(key, new ArtifactDependency(include));
        }
        return this;
    }

    /**
     * Add a single artifact dependency that will be used to build all of the
     * supporting source code and tests. The dependency will not be included in
     * the list of production dependencies. The given list of excludes specify
     * which of the artifacts in the given artifacts dependencies to exclude by
     * artifact group and name.
     * 
     * @param artifact
     *            The artifact to add to the project.
     * @param excludes
     *            The artifacts to exclude from the project.
     * @return This project builder to continue building the project.
     */
    public ProjectDependencyBuilder development(String artifact, String...excludes) {
        Include include = new Include(artifact, excludes);
        List<String> key = include.getArtifact().getUnversionedKey();
        if (!development.containsKey(key)) {
            development.put(key, new ArtifactDependency(include));
        }
        return this;
    }
 
    /**
     * Get the parent Java project builder.
     * 
     * @return The parent Java project builder.
     */
    public JavaProject end()  {
        return project;
    }
}
