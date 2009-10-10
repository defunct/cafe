package com.goodworkalan.mix;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.goodworkalan.go.go.Artifact;

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
    private final Map<List<String>, ArtifactSource> artifacts;

    /** The artifact group. */
    private final String group;

    /** The artifact name. */
    private final String name;

    /** The artifact version. */
    private final String version;

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
    ArtifactElement(ProducesElement produces, String recipe, Map<List<String>, ArtifactSource> artifacts, String group, String name, String version) {
        this.produces = produces;
        this.recipe = recipe;
        this.artifacts = artifacts;
        this.group = group;
        this.name = name;
        this.version = version;
    }

    /**
     * Specify the output directory for the generated artifact. The output
     * directory will be structured as an artifact repository.
     * 
     * @param directory
     *            The output directory for the artifact.
     * @return The parent language element.
     */
    public ProducesElement in(String directory) {
        Artifact artifact = new Artifact(group, name, version);
        artifacts.put(artifact.getKey(), new ArtifactSource(artifact, recipe, new File(directory)));
        return produces;
    }
}
