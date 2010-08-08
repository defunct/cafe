package com.goodworkalan.mix.outline;

import com.goodworkalan.cafe.builder.Builder;

/**
 * Builds the set of project dependencies. This is a shorthand for projects that
 * customize nothing about the default project and only specify their
 * dependencies.
 * 
 * @author Alan Gutierrez
 */
public class DependencyStatement {
    /** The parent Java project builder. */
    private final JavaProject project;
    
    /** The project builder. */
    private final Builder builder;

    /**
     * Create a project dependency builder that records dependencies in the
     * given maps of production and development productions.
     * 
     * @param project
     *            The parent Java project builder.
     */
    public DependencyStatement(JavaProject project, Builder builder) {
        this.project = project;
        this.builder = builder;
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
    public DependencyStatement production(String artifact, String...excludes) {
        builder
            .recipe("production")
                .depends()
                    .include(artifact, excludes)
                    .end()
                .end()
            .end();
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
    public DependencyStatement development(String artifact, String...excludes) {
        builder
            .recipe("development")
                .depends()
                    .include(artifact, excludes)
                    .end()
                .end()
            .end();
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
