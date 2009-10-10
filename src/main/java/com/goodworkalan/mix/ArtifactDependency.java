package com.goodworkalan.mix;

import java.util.Collection;
import java.util.Collections;

import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.go.go.PathPart;
import com.goodworkalan.go.go.ResolutionPart;

/**
 * A single artifact dependency.
 * 
 * @author Alan Gutierrez
 */
public class ArtifactDependency implements Dependency {
    /** The artifact. */
    private final Artifact artifact;

    /**
     * Create a single artifact dependency.
     * 
     * @param artifact
     *            The artifact.
     */
    public ArtifactDependency(Artifact artifact) {
        this.artifact = artifact;
    }

    /**
     * Get a collection that contains a single unresolved path part for the
     * artifact.
     * 
     * @param project
     *            The project.
     */
    public Collection<PathPart> getPathParts(Project project) {
        return Collections.<PathPart>singletonList(new ResolutionPart(artifact));
    }

    /**
     * Get a collection that contains the single artifact.
     * 
     * @param project
     *            The project.
     */
    public Collection<Artifact> getArtifacts(Project project) {
        return Collections.singleton(artifact);
    }

    /**
     * A do nothing make task.
     * 
     * @param project
     *            The project.
     */
    public void make(Project project) {
    }
}
