package com.goodworkalan.mix;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.List;

import com.goodworkalan.go.go.Argument;
import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.Task;

@Command(parent = MixTask.class)
public class DependenciesTask extends Task {
    /** The recipe name. */
    private String recipe;

    /** The Mix configuration. */
    private MixTask.Configuration configuration;

    /** The output file. */
    private File outputFile;

    /**
     * Set the output file.
     * 
     * @param outputFile
     *            The output file.
     */
    @Argument
    public void addOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    /**
     * Get the recipe name.
     * 
     * @param recipe
     *            The recipe name.
     */
    @Argument
    public void addRecipe(String recipe) {
        this.recipe = recipe;
    }

    /**
     * Set the Mix configuration.
     * 
     * @param configuration
     *            The Mix configuration.
     */
    public void setConfiguration(MixTask.Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void execute(Environment env) {
        Project project = configuration.getProject();
        
        LinkedHashMap<List<String>, Artifact> dependencies = new LinkedHashMap<List<String>, Artifact>();
        if (recipe != null) {
            for (Dependency dependency : project.getRecipe(recipe).getDependencies()) {
                for (Artifact artifact : dependency.getArtifacts(project)) {
                    dependencies.put(artifact.getKey().subList(0, 2), artifact);
                }
            }
        }

        try {
            Writer writer = new FileWriter(outputFile);
            for (Artifact artifact : dependencies.values()) {
                writer.write(artifact.includeLine());
            }
            writer.close();
        } catch (IOException e) {
            throw new MixException(0, e);
        }
    }
}
