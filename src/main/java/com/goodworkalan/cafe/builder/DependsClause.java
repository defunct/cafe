package com.goodworkalan.cafe.builder;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.goodworkalan.cafe.Dependency;
import com.goodworkalan.go.go.library.Include;

/**
 * Specify the dependencies of a project.
 * 
 * @author Alan Gutierrez
 */
public class DependsClause<P> {
    // TODO Document.
    private final P recipeElement;
    
    // TODO Document.
    private final Map<List<String>, Dependency> dependencies; 
    
    // TODO Document.
    DependsClause(P recipeElement, Map<List<String>, Dependency> dependencies) {
        this.recipeElement = recipeElement;
        this.dependencies = dependencies;
    }

    // TODO Document.
    public DependsClause<P> recipe(String name) {
        if (!dependencies.containsKey(name)) {
            dependencies.put(Collections.singletonList(name), new RecipeDependency(name));
        }
        return this;
    }

	/**
	 * Add artifacts while also specifying excludes.
	 * 
	 * @param artifact
	 *            The artifact name.
	 * @param excludes
	 *            A list of artifacts to exclude, specified as unversioned
	 *            artifact strings.
	 * @return This depends language element to continue specifying
	 *         dependencies.
	 */
    public DependsClause<P> include(String artifact, String...excludes) {
        return include(new Include(artifact, excludes));
    }
    
    // TODO Document.
    public DependsClause<P> include(Include include) {
        List<String> key = include.getArtifact().getUnversionedKey();
        if (!dependencies.containsKey(key)) {
            dependencies.put(key, new ArtifactDependency(include));
        }
        return this;
    }
    
    // TODO Document.
    public P end() {
        return recipeElement;
    }
}
