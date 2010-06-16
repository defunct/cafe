package com.goodworkalan.mix;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.goodworkalan.go.go.Argument;
import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.Commandable;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.library.Artifact;
import com.goodworkalan.go.go.library.Include;
import com.goodworkalan.go.go.library.PathPart;
import com.goodworkalan.ilk.Ilk;

/**
 * A command that prints a listing of dependencies as artifact strings to
 * standard out.
 * 
 * @author Alan Gutierrez
 */
@Command(parent = MixCommand.class)
public class DependenciesCommand implements Commandable {
	/** Whether or not to show just immediate dependencies. */
	@Argument
	public boolean immediate;
	
	@Argument
	public boolean development;
	
    public void execute(Environment env) {
        Project project = env.get(Project.class, 0);
        Map<String, Production> byRecipeName = new HashMap<String, Production>();
        for (Production production : project.getProductions()) {
            byRecipeName.put(production.getRecipeName(), production);
        }
        Map<List<String>, Include> includes = new LinkedHashMap<List<String>, Include>();
        if (immediate) {
        	if (development) {
        		for (Recipe recipe : project.getRecipes()) {
        			for (Dependency dependency : recipe.getDependencies()) {
        				for (Include include : dependency.getIncludes(project)) {
        					includes.put(include.getArtifact().getUnversionedKey(), include);
        				}
        			}
        		}
        	} else {
        		for (Production production : project.getProductions()) {
        			Recipe recipe = project.getRecipe(production.getRecipeName());
        			for (Dependency dependency : recipe.getDependencies()) {
        				for (Include include : dependency.getIncludes(project)) {
        					includes.put(include.getArtifact().getUnversionedKey(), include);
        				}
        			}
        		}
        	}
        } else {
	        Map<Object, PathPart> path = new LinkedHashMap<Object, PathPart>();
	        List<Dependency> dependencies = new ArrayList<Dependency>();
	        if (development) {
	        	for (Recipe recipe : project.getRecipes()) {
	        		for (Dependency dependency : recipe.getDependencies()) {
	        			dependencies.add(dependency);
	        		}
	        	}
	        } else {
	        	for (Production production : project.getProductions()) {
	        		Recipe recipe = project.getRecipe(production.getRecipeName());
	        		for (Dependency dependency : recipe.getDependencies()) {
	        			dependencies.add(dependency);
	        		}
	        	}
	        }
	        for (Dependency dependency : dependencies) {
		        Collection<PathPart> unexpanded = dependency.getPathParts(project);
		        for (PathPart expanded : env.library.resolve(unexpanded, path.keySet())) {
		        	path.put(expanded.getUnversionedKey(), expanded);
		        }
	        }
	        for (PathPart pathPart : path.values()) {
	            Artifact artifact = pathPart.getArtifact();
	            if (artifact != null) {
	                includes.put(artifact.getUnversionedKey(), new Include(artifact, pathPart.getExcludes()));
	            }
	        }
        }
        for (Include include : includes.values()) {
        	env.io.out.println(include.getArtifactFileLine());
        }
        env.output(new Ilk<List<Include>>() {}, new ArrayList<Include>(includes.values()));
    }
}
