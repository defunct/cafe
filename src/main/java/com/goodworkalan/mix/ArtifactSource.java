package com.goodworkalan.mix;

import java.io.File;

import com.goodworkalan.go.go.library.Artifact;

/**
 * A structure that contains the source recipe and output directory for an
 * artifact produced by a project.
 * 
 * @author Alan Gutierrez
 */
public class ArtifactSource {
    /** The artifact. */
    private final Artifact artifact;

    /** The recipe that provides the artifact. */
    private final String recipe;

    /** The recipe that provides the artifact. */
    private final File directory;

    /**
     * Create a new artifact source structure.
     * 
     * @param artifact
     *            The artifact.
     * @param recipe
     *            The recipe that provides the artifact.
     * @param directory
     *            The recipe that provides the artifact.
     */
    public ArtifactSource(Artifact artifact, String recipe, File directory) {
        this.artifact = artifact;
        this.recipe = recipe;
        this.directory = directory;
    }

    /**
     * Get the artifact.
     * 
     * @return The artifact.
     */
    public Artifact getArtifact() {
        return artifact;
    }

    /**
     * Get the recipe that provides the artifact.
     * 
     * @return The recipe that provides the artifact.
     */
    public String getRecipe() {
        return recipe;
    }

    /**
     * Get the recipe that provides the artifact.
     * 
     * @return The recipe that provides the artifact.
     */
    public File getDirectory() {
        return directory;
    }
}
