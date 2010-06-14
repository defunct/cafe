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
	
    public void execute(Environment env) {
        Project project = env.get(Project.class, 0);
        Map<String, Production> byRecipeName = new HashMap<String, Production>();
        for (Production production : project.getProductions()) {
            byRecipeName.put(production.getRecipeName(), production);
        }
        List<Include> includes = new ArrayList<Include>();
        if (immediate) {
	        for (Production production : project.getProductions()) {
	            Recipe recipe = project.getRecipe(production.getRecipeName());
	            for (Dependency dependency : recipe.getDependencies()) {
	            	includes.addAll(dependency.getIncludes(project));
	            }
	        }
        } else {
	        Map<Object, PathPart> depenendcies = new LinkedHashMap<Object, PathPart>();
	        for (Production production : project.getProductions()) {
	            Recipe recipe = project.getRecipe(production.getRecipeName());
	            for (Dependency dependency : recipe.getDependencies()) {
	                Collection<PathPart> unexpanded = dependency.getPathParts(project);
	                for (PathPart expanded : env.library.resolve(unexpanded, depenendcies.keySet())) {
	                    depenendcies.put(expanded.getUnversionedKey(), expanded);
	                }
	            }
	        }
	        for (PathPart pathPart : depenendcies.values()) {
	            Artifact artifact = pathPart.getArtifact();
	            if (artifact != null) {
	                includes.add(new Include(artifact, pathPart.getExcludes()));
	                env.io.out.println(artifact.getArtifactsFileLine(pathPart.getExcludes()));
	            }
	        }
        }
        env.output(new Ilk<List<Include>>() {}, includes);
    }
}
