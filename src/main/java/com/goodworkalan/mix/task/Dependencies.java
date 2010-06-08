package com.goodworkalan.mix.task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.library.Exclude;
import com.goodworkalan.go.go.library.Include;
import com.goodworkalan.mix.Dependency;
import com.goodworkalan.mix.Mix;
import com.goodworkalan.mix.MixError;
import com.goodworkalan.mix.Project;
import com.goodworkalan.mix.builder.Executable;
import com.goodworkalan.mix.builder.RecipeStatement;

public class Dependencies {
    private final RecipeStatement recipeElement;
    
    /** The recipe name. */
    private String recipe;

    /** The output file. */
    private File output;
    
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

    public RecipeStatement end() {
        recipeElement.executable(new Executable() {
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
                Mix mix = env.get(Mix.class, 0);
                File relativized = mix.relativize(output);
                if (!relativized.getParentFile().isDirectory() && !relativized.getParentFile().mkdirs()) {
                    throw new MixError(Dependencies.class, "mkdirs", relativized.getParentFile());
                }
                try {
                    PrintWriter writer = new PrintWriter(new FileWriter(relativized));
                    for (Include include : dependencies.values()) {
                        writer.println(include.getArtifactFileLine());
                    }
                    writer.close();
                } catch (IOException e) {
                    throw new MixError(Dependencies.class, "write", e, relativized);
                }
            }
        });
        return recipeElement;
    }
}
