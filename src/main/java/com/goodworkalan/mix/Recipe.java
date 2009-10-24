package com.goodworkalan.mix;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.goodworkalan.mix.builder.Executable;

public class Recipe {
    private final List<Executable> program;
    
    private final Map<List<String>, Dependency> dependencies;
    
    private final Set<File> classes;
    
    public Recipe(List<Executable> program, Map<List<String>, Dependency> dependencies, Set<File> classes) {
        this.program = program;
        this.dependencies = dependencies;
        this.classes = classes;
    }
    
    public List<Executable> getProgram() {
        return program;
    }
    
    public List<Dependency> getDependencies() {
        return new ArrayList<Dependency>(dependencies.values());
    }
    
    public Set<File> getClasses() {
        return classes;
    }
}
