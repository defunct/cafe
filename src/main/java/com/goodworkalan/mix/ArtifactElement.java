package com.goodworkalan.mix;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.goodworkalan.go.go.Artifact;

public class ArtifactElement {
    private final ProducesElement produces;
    
    private final String recipe;
    
    private final Map<List<String>, ArtifactSource> artifacts;

    private final String group;

    private final String name;

    private final String version;

    public ArtifactElement(ProducesElement produces, String recipe, Map<List<String>, ArtifactSource> artifacts, String group, String name, String version) {
        this.produces = produces;
        this.recipe = recipe;
        this.artifacts = artifacts;
        this.group = group;
        this.name = name;
        this.version = version;
    }

    public ProducesElement in(String directory) {
        Artifact artifact = new Artifact(group, name, version);
        artifacts.put(artifact.getKey(), new ArtifactSource(artifact, recipe, new File(directory)));
        return produces;
    }
}
