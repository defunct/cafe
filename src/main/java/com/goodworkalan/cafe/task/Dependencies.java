package com.goodworkalan.cafe.task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.goodworkalan.cafe.Build;
import com.goodworkalan.cafe.Dependency;
import com.goodworkalan.cafe.CafeError;
import com.goodworkalan.cafe.Project;
import com.goodworkalan.cafe.builder.RecipeStatement;
import com.goodworkalan.go.go.Commandable;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.library.Exclude;
import com.goodworkalan.go.go.library.Include;

// TODO Document.
public class Dependencies {
    // TODO Document.
    private final RecipeStatement recipeElement;
    
    /** The recipe name. */
    private String recipe;

    /** The output file. */
    private File output;
    
    // TODO Document.
    public Dependencies(RecipeStatement recipeElement) {
        this.recipeElement = recipeElement;
    }

    /**
     * Set the output file.
     * 
     * @param output
     *            The output file.
     */
    public Dependencies output(File output) {
        this.output = output;
        return this;
    }

    /**
     * Get the recipe name.
     * 
     * @param recipe
     *            The recipe name.
     */
    public Dependencies recipe(String recipe) {
        this.recipe = recipe;
        return this;
    }

    // TODO Document.
    public RecipeStatement end() {
        recipeElement.executable(new Commandable() {
            public void execute(Environment env) {
                Project project = env.get(Project.class, 0);
                LinkedHashMap<List<String>, Include> dependencies = new LinkedHashMap<List<String>, Include>();
                if (recipe != null) {
                    for (Dependency dependency : project.getRecipe(recipe).getDependencies()) {
                        for (Include include : dependency.getIncludes(project)) {
                            List<String> key = include.getArtifact().getKey().subList(0, 2);
                            Include existing = dependencies.get(key);
                            if (existing == null) {
                                dependencies.put(key, include);
                            } else {
                                Set<Exclude> excludes = new HashSet<Exclude>();
                                excludes.addAll(existing.getExcludes());
                                excludes.addAll(include.getExcludes());
                                dependencies.put(key, new Include(existing.getArtifact(), excludes));
                            }
                            dependencies.put(include.getArtifact().getKey().subList(0, 2), include);
                        }
                    }
                }
                Build mix = env.get(Build.class, 0);
                File relativized = mix.relativize(output);
                if (!relativized.getParentFile().isDirectory() && !relativized.getParentFile().mkdirs()) {
                    throw new CafeError(Dependencies.class, "mkdirs", relativized.getParentFile());
                }
                try {
                    PrintWriter writer = new PrintWriter(new FileWriter(relativized));
                    for (Include include : dependencies.values()) {
                        writer.println(include.getArtifactFileLine());
                    }
                    writer.close();
                } catch (IOException e) {
                    throw new CafeError(Dependencies.class, "write", e, relativized);
                }
            }
        });
        return recipeElement;
    }
}
