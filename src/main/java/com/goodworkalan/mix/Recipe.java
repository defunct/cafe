package com.goodworkalan.mix;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.goodworkalan.go.go.PathPart;
import com.goodworkalan.mix.builder.Executable;

public class Recipe {
    private final List<Executable> program;
    
    private final Map<List<String>, Dependency> dependencies;
    
    private final Collection<PathPart> produce;
    
    public Recipe(List<Executable> program, Map<List<String>, Dependency> dependencies, Collection<PathPart> produce) {
        this.program = program;
        this.dependencies = dependencies;
        this.produce = produce;
    }
    
    public List<Executable> getProgram() {
        return program;
    }
    
    public List<Dependency> getDependencies() {
        return new ArrayList<Dependency>(dependencies.values());
    }
    
    public Collection<PathPart> getProduce() {
        return produce;
    }
}
