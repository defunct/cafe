package com.goodworkalan.mix.task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.Include;
import com.goodworkalan.mix.Dependency;
import com.goodworkalan.mix.MixException;
import com.goodworkalan.mix.Project;
import com.goodworkalan.mix.builder.Executable;
import com.goodworkalan.mix.builder.RecipeElement;

public class Dependencies {
    private final RecipeElement recipeElement;
    
    /** The recipe name. */
    private String recipe;

    /** The output file. */
    private File output;
    
    public Dependencies(RecipeElement recipeElement) {
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

    public RecipeElement end() {
        recipeElement.addExecutable(new Executable() {
            public void execute(Environment env, Project project, String recipeName) {
                LinkedHashMap<List<String>, Include> dependencies = new LinkedHashMap<List<String>, Include>();
                if (recipe != null) {
                    for (Dependency dependency : project.getRecipe(recipe).getDependencies()) {
                        for (Include include : dependency.getIncludes(project)) {
                            List<String> key = include.getArtifact().getKey().subList(0, 2);
                            Include existing = dependencies.get(key);
                            if (existing == null) {
                                dependencies.put(key, include);
                            } else {
                                boolean optional = include.isOptional() && existing.isOptional();
                                Set<Artifact> excludes = new HashSet<Artifact>();
                                excludes.addAll(existing.getExcludes());
                                excludes.addAll(include.getExcludes());
                                dependencies.put(key, new Include(optional, existing.getArtifact(), excludes));
                            }
                            dependencies.put(include.getArtifact().getKey().subList(0, 2), include);
                        }
                    }
                }
                if (!output.getParentFile().isDirectory() && !output.getParentFile().mkdirs()) {
                    throw new MixException(0);
                }
                try {
                    PrintWriter writer = new PrintWriter(new FileWriter(output));
                    for (Include include : dependencies.values()) {
                        for (String line : include.getArtifactFileLines()) {
                            writer.println(line);
                        }
                    }
                    writer.close();
                } catch (IOException e) {
                    throw new MixException(0, e);
                }
            }
        });
        return recipeElement;
    }
}