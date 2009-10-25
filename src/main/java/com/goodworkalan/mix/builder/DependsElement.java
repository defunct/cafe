package com.goodworkalan.mix.builder;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.go.go.Include;
import com.goodworkalan.mix.ArtifactDependency;
import com.goodworkalan.mix.Dependency;
import com.goodworkalan.mix.RecipeDependency;

/**
 * Specify the dependencies of a project.
 * 
 * FIXME I can probably merge all elements into one, after I implement siblings
 * that is all Dependency derived classes, then I can move this to a separate
 * package, it is the only one with a tight binding.
 *  
 * @author Alan Gutierrez
 */
// FIXME Make recipe execution a separate dependency list, that way you can 
// have this dependency be only for paths.
public class DependsElement<P> {
    private final P recipeElement;
    
    private final Map<List<String>, Dependency> dependencies; 
    
    public DependsElement(P recipeElement, Map<List<String>, Dependency> dependencies) {
        this.recipeElement = recipeElement;
        this.dependencies = dependencies;
    }

    public DependsElement<P> classes(String name) {
        if (!dependencies.containsKey(name)) {
            dependencies.put(Collections.singletonList(name), new RecipeDependency(name));
        }
        return this;
    }
    
    public DependsElement<P> sibling(String group, String name, String version) {
        return this;
    }

    /**
     * Add artifacts while also specifying excludes.
     * 
     * @param includes
     *            An artifacts to include.
     * @return This depends language element to continue specifying
     *         dependencies.
     */
    public DependsElement<P> artifact(Include...includes) {
        for (Include include : includes) {
            List<String> key = include.getArtifact().getKey().subList(0, 2);
            if (!dependencies.containsKey(key)) {
                dependencies.put(key, new ArtifactDependency(include));
            }
        }
        return this;
    }
    
    public DependsElement<P> dependencies(Map<List<String>, Dependency> dependencies) {
        this.dependencies.putAll(dependencies);
        return this;
    }

    /**
     * Add artifacts while also specifying excludes.
     * 
     * @param includes
     *            An artifacts to include.
     * @return This depends language element to continue specifying
     *         dependencies.
     */
    public DependsElement<P> artifact(Collection<Include> includes) {
        return artifact(includes.toArray(new Include[includes.size()]));
    }

    public DependsElement<P> artifact(Artifact artifact) {
        return artifact(new Include(artifact));
    }
    
    public P end() {
        return recipeElement;
    }
}
