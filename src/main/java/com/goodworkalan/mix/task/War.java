package com.goodworkalan.mix.task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipFile;

import com.goodworkalan.glob.Find;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.Library;
import com.goodworkalan.go.go.PathPart;
import com.goodworkalan.mix.Dependency;
import com.goodworkalan.mix.MixException;
import com.goodworkalan.mix.MixTask;
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
    /**
     * List or recipe names paired to a flag to indicate whether to include the
     * dependencies or the produce of the recipe.
     */
    private final List<Map<String, Boolean>> recipes = new ArrayList<Map<String, Boolean>>();

    /** The Mix configuration. */
    private MixTask.Configuration configuration;

    public War(RecipeElement program) {
        super(program);
    }

    /**
     * Set the Mix configuration output.
     * 
     * @param configuration
     *            The Mix configuration.
     */
    public void setConfiguration(MixTask.Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Add the artifact dependencies of the given recipe.
     * 
     * @param recipeName
     *            The recipe name.
     */
    public War depdenecies(String recipeName) {
        recipes.add(Collections.singletonMap(recipeName, false));
        return this;
    }

    /**
     * Add the output given recipe.
     * 
     * @param recipeName
     *            The recipe name.
     */
    public War classes(String recipeName) {
        recipes.add(Collections.singletonMap(recipeName, true));
        return this;
    }

    /**
     * Add the artifacts to the <code>WEB-INF/lib</code> path in the web archive
     * file.
     * 
     * @param env
     *            The execution environment.
     */
    @Override
    protected void addAdditionalEntries(Environment env) throws IOException {
        Collection<PathPart> parts = new ArrayList<PathPart>();
        Project project = configuration.getProject();
        for (Map<String, Boolean> pair : recipes) {
            Map.Entry<String, Boolean> entry = pair.entrySet().iterator().next();
            Recipe recipe = project.getRecipe(entry.getKey());
            if (entry.getValue()) {
                for (Dependency dependency : recipe.getDependencies()) {
                    parts.addAll(dependency.getPathParts(project));
                }
            } else {
                for (File file : recipe.getClasses()) {
                    addFind(new Find(), file);
                }
            }
        }
        Library library = env.part.getCommandInterpreter().getLibrary();
        for (File file : library.resolve(parts).getFiles()) {
            try {
                new ZipFile(file);
            } catch (IOException e) {
                throw new MixException(0, e);
            }
            addFile(file, "WEB-INF/lib/" + file.getName());
        }
    }
}
