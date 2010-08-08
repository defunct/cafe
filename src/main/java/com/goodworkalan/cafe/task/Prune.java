package com.goodworkalan.cafe.task;

import java.io.File;

import com.goodworkalan.cafe.Build;
import com.goodworkalan.cafe.CafeError;
import com.goodworkalan.cafe.builder.FindList;
import com.goodworkalan.cafe.builder.FindStatement;
import com.goodworkalan.cafe.builder.RecipeStatement;
import com.goodworkalan.comfort.io.Files;
import com.goodworkalan.go.go.Commandable;
import com.goodworkalan.go.go.Environment;

/**
 * Delete files within a directory that match find criteria.
 * 
 * @author Alan Gutierrez
 */
public class Prune {
    /** The find criteria. */
    private final FindList findList = new FindList();
    
    // TODO Document.
    private final RecipeStatement recipeElement;

    // TODO Document.
    public Prune(RecipeStatement recipeElement) {
        this.recipeElement = recipeElement;
    }

    // TODO Document.
    public FindStatement<Prune> directory(File directory) {
        return new FindStatement<Prune>(this, findList, directory);
    }
    
    // TODO Document.
    public RecipeStatement end() {
        recipeElement.executable(new Commandable() {
            public void execute(Environment env) {
                Build mix = env.get(Build.class, 0);
                for (FindList.Entry entry : findList) {
                    File directory = mix.relativize(entry.getDirectory());
                    for (String fileName : entry.getFind().find(directory)) {
                        File source = new File(directory, fileName);
                        if (!Files.unlink(source)) {
                            throw new CafeError(Prune.class, "failure", source);
                        }
                    }
                }
            }
        });
        return recipeElement;
    }
}
