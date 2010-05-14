package com.goodworkalan.mix.task;

import java.io.File;

import com.goodworkalan.comfort.io.Files;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.mix.Mix;
import com.goodworkalan.mix.MixError;
import com.goodworkalan.mix.builder.Executable;
import com.goodworkalan.mix.builder.RecipeElement;

/**
 * Delete a file or directory.
 * 
 * @author Alan Gutierrez
 */
public class Delete {
    private final RecipeElement recipeElement;
    
    /** The file to delete. */
    private File file;
    
    /** If true delete recursively. */
    private boolean recurse;
    
    public Delete(RecipeElement recipeElement) {
        this.recipeElement = recipeElement;
    }

    /**
     * The file or directory to delete.
     * 
     * @param file
     *            The file or directory to delete.
     */
    public Delete file(File file) {
        this.file = file;
        return this;
    }

    /**
     * Whether or not to delete recursively if file is a directory.
     * 
     * @param recurse
     *            If true, delete file recursively if it is a directory.
     */
    public Delete recurse(boolean recurse) {
        this.recurse = recurse;
        return this;
    }

    public RecipeElement end() {
        recipeElement.executable(new Executable() {
            public void execute(Environment env) {
                Mix mix = env.get(Mix.class, 0);
                File outgoing = mix.relativize(file);
                if (!recurse) {
                    if (outgoing.exists() && !outgoing.delete()) {
                        throw new MixError(Delete.class, "failure", outgoing);
                    }
                } else if (!Files.delete(outgoing)) {
                    throw new MixError(Delete.class, "failure", outgoing);
                }
            }
        });
        return recipeElement;
    }
}
