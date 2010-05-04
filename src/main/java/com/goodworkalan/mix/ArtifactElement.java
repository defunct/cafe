package com.goodworkalan.mix;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.goodworkalan.go.go.library.Artifact;

/**
 * An element in the domain-specific project specification language that
 * defines output artifact. 
 *
 * @author Alan Gutierrez
 */
public class ArtifactElement {
    /** The parent element in the domain-specific language. */
    private final ProducesElement produces;
    
    /** The recipe name. */
    private final String recipe;
    
    /**
     * The project map of artifact keys to artifact details for all the
     * artifacts produced by the project.
     */
    private final Map<List<String>, Production> artifacts;

    /** The artifact. */
    private final Artifact artifact; 

    /**
     * Create a new artifact element.
     * 
     * @param produces
     *            The parent element in the domain-specific language.
     * @param recipe
     *            The recipe name.
     * @param artifacts
     *            The project map of artifact keys to artifact details for all
     *            the artifacts produced by the project.
     * @param group
     *            The artifact group.
     * @param name
     *            The artifact name.
     * @param version
     *            The artifact version.
     */
    ArtifactElement(ProducesElement produces, String recipe, Map<List<String>, Production> artifacts, Artifact artifact) {
        this.produces = produces;
        this.recipe = recipe;
        this.artifacts = artifacts;
        this.artifact = artifact;
    }

    /**
     * Specify the output directory for the generated artifact. The output
     * directory will be structured as an artifact repository.
     * 
     * @param directory
     *            The output directory for the artifact.
     * @return The parent language element.
     */
    public ProducesElement in(File directory) {
        artifacts.put(artifact.getKey(), new Production(artifact, recipe, directory));
        return produces;
    }
}
