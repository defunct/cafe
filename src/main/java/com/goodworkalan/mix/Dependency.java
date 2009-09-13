package com.goodworkalan.mix;

import java.util.Collection;

import com.goodworkalan.go.go.PathPart;

public interface Dependency {
    public Collection<PathPart> getPathParts(Project project);
}
