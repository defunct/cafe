package com.goodworkalan.cafe.task;

import java.io.File;

import com.goodworkalan.cafe.Build;
import com.goodworkalan.cafe.CafeError;
import com.goodworkalan.cafe.builder.RecipeStatement;
import com.goodworkalan.comfort.io.Files;
import com.goodworkalan.go.go.Commandable;
import com.goodworkalan.go.go.Environment;

/**
 * Delete a file or directory.
 * 
 * @author Alan Gutierrez
 */
public class Unlink {
    // TODO Document.
    private final RecipeStatement recipeElement;
    
    /** The file to delete. */
    private File file;
    
    /** If true delete recursively. */
    private boolean recurse;
    
    // TODO Document.
    public Unlink(RecipeStatement recipeElement) {
        this.recipeElement = recipeElement;
    }

    /**
     * The file or directory to delete.
     * 
     * @param file
     *            The file or directory to delete.
     */
    public Unlink file(File file) {
        this.file = file;
        return this;
    }

    /**
     * Whether or not to delete recursively if file is a directory.
     * 
     * @param recurse
     *            If true, delete file recursively if it is a directory.
     */
    public Unlink recurse(boolean recurse) {
        this.recurse = recurse;
        return this;
    }

    /**
     * Terminate the unlink clause and add the unlink operation to the series of
     * operations for the recipe.
     * 
     * @return The recipe statement to continue building the recipe.
     */
    public RecipeStatement end() {
        recipeElement.executable(new Commandable(){
            public void execute(Environment env) {
                Build mix = env.get(Build.class, 0);
                File outgoing = mix.relativize(file);
                if (!recurse) {
                    if (outgoing.exists() && !outgoing.delete()) {
                        throw new CafeError(Unlink.class, "failure", outgoing);
                    }
                } else if (!Files.unlink(outgoing)) {
                    throw new CafeError(Unlink.class, "failure", outgoing);
                }
            }
        });
        return recipeElement;
    }
}
