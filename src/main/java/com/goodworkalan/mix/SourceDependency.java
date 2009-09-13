package com.goodworkalan.mix;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;

import com.goodworkalan.go.go.CommandPart;
import com.goodworkalan.go.go.Executor;
import com.goodworkalan.go.go.Library;

// FIXME OutputDepdenency and RecipeDependency
public class SourceDependency implements Dependency {
    private final String recipe;
    
    public SourceDependency(String recipe) {
        this.recipe = recipe;
    }

    public Set<File> getFiles(Project project, Library library) {
        Set<File> files = new LinkedHashSet<File>();
        // FIXME BOGUS!!!
        files.add(new File("smotchkiss/classes"));
        files.add(new File("smotchkiss/test-classes"));
        if (recipe != null) {
            for (Dependency dependency : project.getDependencies(recipe)) {
                files.addAll(dependency.getFiles(project, library));
            }
        }
        return files;
    }
    
    public void make(Executor executor, CommandPart mix) {
        executor.equals(mix.extend("make", recipe));
    }
}
