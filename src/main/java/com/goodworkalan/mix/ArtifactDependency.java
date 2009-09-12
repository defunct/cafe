package com.goodworkalan.mix;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.go.go.Library;

public class ArtifactDependency implements Dependency {
    private final Artifact artifact;
    
    public ArtifactDependency(Artifact artifact) {
        this.artifact = artifact;
    }
    
    public Set<File> getFiles(Library library) {
        return library.getFiles(Collections.singletonList(artifact), new HashSet<String>());
    }
}
