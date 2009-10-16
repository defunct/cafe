package com.goodworkalan.mix;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.go.go.Include;

public class DependsElement {
    private final RecipeElement recipeElement;
    
    private final Map<List<String>, Dependency> dependencies; 
    
    public DependsElement(RecipeElement recipeElement, Map<List<String>, Dependency> dependencies) {
        this.recipeElement = recipeElement;
        this.dependencies = dependencies;
    }

    // FIXME Rename classes.
    public DependsElement source(String name) {
        if (!dependencies.containsKey(name)) {
            dependencies.put(Collections.singletonList(name), new RecipeDependency(name));
        }
        return this;
    }
    
    public DependsElement sibling(String group, String name, String version) {
        return this;
    }

    /**
     * Add artifacts while also specifying excludes.
     * 
     * @param include
     *            An artifact to include.
     * @return This depends language element to continue specifying
     *         dependencies.
     */
    public DependsElement artifact(Include...includes) {
        for (Include include : includes) {
            List<String> key = include.getArtifact().getKey().subList(0, 2);
            if (!dependencies.containsKey(key)) {
                dependencies.put(key, new ArtifactDependency(include));
            }
        }
        return this;
    }
    
    /**
     * Add artifacts while also specifying excludes.
     * 
     * @param include
     *            An artifact to include.
     * @return This depends language element to continue specifying
     *         dependencies.
     */
    public DependsElement artifact(Collection<Include> includes) {
        return artifact(includes.toArray(new Include[includes.size()]));
    }

    public DependsElement artifact(String group, String name, String version) {
        Artifact artifact = new Artifact(group, name, version);
        List<String> key = artifact.getKey().subList(0, 2);
        if (!dependencies.containsKey(key)) {
            dependencies.put(key, new ArtifactDependency(new Include(artifact)));
        }
        return this;
    }
    
    public RecipeElement end() {
        return recipeElement;
    }
}
