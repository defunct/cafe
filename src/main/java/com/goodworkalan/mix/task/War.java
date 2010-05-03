package com.goodworkalan.mix.task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.zip.ZipFile;

import com.goodworkalan.comfort.io.Find;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.library.PathPart;
import com.goodworkalan.go.go.library.PathParts;
import com.goodworkalan.mix.Dependency;
import com.goodworkalan.mix.MixError;
import com.goodworkalan.mix.Project;
import com.goodworkalan.mix.Recipe;
import com.goodworkalan.mix.builder.RecipeElement;

/**
 * Create a web application archive copying artifact dependencies to the
 * <code>WEB-INF/lib</code> path in the web archive file.
 * 
 * @author Alan Gutierrez
 */
public class War extends Zip {
    public War(RecipeElement program) {
        super(program);
    }

    /**
     * Add the artifacts to the <code>WEB-INF/lib</code> path in the web archive
     * file.
     * 
     * @param env
     *            The execution environment.
     */
    @Override
    protected void addAdditionalEntries(Environment env, Project project, String recipeName) throws IOException {
        Collection<PathPart> parts = new ArrayList<PathPart>();
        for (Dependency dependency : project.getRecipe(recipeName).getDependencies()) {
            parts.addAll(dependency.getPathParts(project));
        }
        Recipe recipe = project.getRecipe(recipeName);
        for (File file : recipe.getClasses()) {
            addFind(new Find(), file);
        }
        for (Dependency dependency : recipe.getDependencies()) {
            parts.addAll(dependency.getPathParts(project));
        }
        for (File file : PathParts.fileSet(env.library.resolve(parts))) {
            if (file.isDirectory()) {
                addFind(new Find(), file, "WEB-INF/classes");
            } else {
                try {
                    new ZipFile(file);
                } catch (IOException e) {
                    throw new MixError(War.class, "invalid.jar", e, file);
                }
                addFile(file, "WEB-INF/lib/" + file.getName());
            }
        }
    }
}
