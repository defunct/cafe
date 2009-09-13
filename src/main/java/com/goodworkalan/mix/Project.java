package com.goodworkalan.mix;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.goodworkalan.glob.Find;
import com.goodworkalan.go.go.CommandPart;
import com.goodworkalan.go.go.Executor;

/**
 * Describes a software project, its source files, outputs and dependencies.
 * 
 * @author Alan Gutierrez
 */
public class Project {
    private final Map<String, Recipe> recipes;
    private final File workingDirectory;
    private final Set<String> made = new HashSet<String>();
    private final Executor executor;
    private final CommandPart mix;
    public Project(File workingDirectory, Map<String, Recipe> recipes, Executor executor, CommandPart mix) {
        this.workingDirectory = workingDirectory;
        this.recipes = recipes;
        this.executor = executor;
        this.mix = mix;
    }
    
    public Recipe getRecipe(String name) {
        Recipe recipe = recipes.get(name);
        if (!made.contains(name)) {
            made.add(name);
            for (List<String> step : recipe.getCommands()) {
                CommandPart next = mix.extend(step);
                if (String.class.equals(next.getArgumentTypes().get("recipe"))) {
                    next = next.argument("recipe", name);
                }
                executor.execute(next);
            }
        }
        return recipe;
    }

    /**
     * Get the set of source files for the given directory.
     * 
     * @return The set of source files for the given directory.
     */
    public Set<File> getSourceDirectories() {
        return Collections.singleton(new File(workingDirectory, "src/main/java"));
    }

    /**
     * Get the set of source files for the given source directory.
     * 
     * @return A set of source files.
     */
    public Set<File> getSources(File directory) {
        return new Find().include("**/*.java").find(directory);
    }

    /**
     * Get the output directory.
     * 
     * @return The output directory.
     */
    public File getOutputDirectory() {
        return new File(workingDirectory, "target");
    }
}
