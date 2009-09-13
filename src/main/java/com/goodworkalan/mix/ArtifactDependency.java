package com.goodworkalan.mix;

import java.util.Collection;
import java.util.Collections;

import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.go.go.PathPart;
import com.goodworkalan.go.go.ResolutionPart;

public class ArtifactDependency implements Dependency {
    private final Artifact artifact;
    
    public ArtifactDependency(Artifact artifact) {
        this.artifact = artifact;
    }
    
    public Collection<PathPart> getPathParts(Project project) {
        return Collections.<PathPart>singletonList(new ResolutionPart(artifact));
    }
}
