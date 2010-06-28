package com.goodworkalan.mix;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.goodworkalan.go.go.library.Artifact;

/**
 * A structure that defines how an artifact is produced and where it is emitted.
 * 
 * @author Alan Gutierrez
 */
public class Production {
    /** The artifact. */
    private final Artifact artifact;

    /** The recipe that provides the artifact. */
    private final String recipeName;

    /** The directory where the artifact is emitted. */
    private final File directory;

    /**
     * Create a new artifact source structure.
     * 
     * @param artifact
     *            The artifact.
     * @param recipe
     *            The recipe that provides the artifact.
     * @param directory
     *            The directory where the artifact is emitted.
     */
    public Production(Artifact artifact, String recipe, File directory) {
        this.artifact = artifact;
        this.recipeName = recipe;
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
    public String getRecipeName() {
        return recipeName;
    }

    /**
     * Get the directory where the artifact is emitted.
     * 
     * @return The directory where the artifact is emitted.
     */
    public File getDirectory() {
        return directory;
    }
    
    // TODO Document.
    public static Collection<Production> productionsByName(Collection<Production> productions, String name) {
        List<Production> filtered = new ArrayList<Production>();
        for (Production production : productions) {
            if (production.getArtifact().getName().equals(name)) {
                filtered.add(production);
            }
        }
        return filtered;
    }
    
    // TODO Document.
    public static Collection<Production> productionsByQualifiedName(Collection<Production> productions, String group, String name) {
        List<Production> filtered = new ArrayList<Production>();
        for (Production production : productions) {
            Artifact artifact = production.getArtifact();
            if (artifact.getGroup().equals(group) && artifact.getName().equals(name)) {
                filtered.add(production);
            }
        }
        return filtered;
    }
}
