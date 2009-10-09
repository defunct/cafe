package com.goodworkalan.mix;

import java.util.Collection;

import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.go.go.PathPart;

public interface Dependency {
    public Collection<PathPart> getPathParts(Project project);
    
    public Collection<Artifact> getArtifacts(Project project);
    
    public void make(Project project);
}
