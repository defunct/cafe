package com.goodworkalan.mix;

import java.io.File;
import java.util.Collection;
import java.util.Collections;

import com.goodworkalan.comfort.io.Files;
import com.goodworkalan.go.go.ArgumentList;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.library.Include;
import com.goodworkalan.go.go.library.PathPart;
import com.goodworkalan.go.go.library.ResolutionPart;

/**
 * A single artifact dependency.
 * 
 * @author Alan Gutierrez
 */
public class ArtifactDependency implements Dependency {
    /** The artifact and its exclusions. */
    private final Include include;

    /**
     * Create a single artifact dependency.
     * 
     * @param include
     *            The artifact and its exclusions.
     */
    public ArtifactDependency(Include include) {
        this.include = include;
    }

    /**
     * Get a collection that contains a single unresolved path part for the
     * artifact.
     * 
     * @param project
     *            The project.
     */
    public Collection<PathPart> getPathParts(Project project) {
        return Collections.<PathPart>singletonList(new ResolutionPart(include));
    }

    /**
     * Get a collection that contains the single artifact.
     * 
     * @param project
     *            The project.
     */
    public Collection<Include> getIncludes(Project project) {
        return Collections.singleton(include);
    }

    /**
     * Return an empty collection indicating no dependent recipes.
     * 
     * @param project
     *            The project.
     * @return An empty list.
     */
    public Collection<String> getRecipes(Project project) {
        return Collections.emptyList();
    }
    
    public void build(Mix mix, Environment env) {
        if (mix.isSiblings()) {
            File sibling = new File(mix.getWorkingDirectory().getParentFile(), include.getArtifact().getName());
            if (sibling.isDirectory()) {
                File source = new File(sibling, Files.file("src", "mix", "java"));
                if (source.isDirectory()) {
                    ArgumentList arguments = new ArgumentList(env.arguments.get(0));
                    arguments.removeArgument("mix:working-directory");
                    arguments.addArgument("mix:working-directory", sibling.getAbsolutePath());
                    env.debug(ArtifactDependency.class, "fork");
                    env.executor.fork(env.io, "mix", arguments, env.commands.get(1), env.arguments.get(1));
                }
            }
        }
    }
}
