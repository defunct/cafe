package com.goodworkalan.mix.task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.List;

import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.go.go.Environment;
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
                LinkedHashMap<List<String>, Artifact> dependencies = new LinkedHashMap<List<String>, Artifact>();
                if (recipe != null) {
                    for (Dependency dependency : project.getRecipe(recipe).getDependencies()) {
                        for (Artifact artifact : dependency.getArtifacts(project)) {
                            dependencies.put(artifact.getKey().subList(0, 2), artifact);
                        }
                    }
                }
                if (!output.getParentFile().isDirectory() && !output.getParentFile().mkdirs()) {
                    throw new MixException(0);
                }
                try {
                    Writer writer = new FileWriter(output);
                    for (Artifact artifact : dependencies.values()) {
                        writer.write(artifact.includeLine());
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
