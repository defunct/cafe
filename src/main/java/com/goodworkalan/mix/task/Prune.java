package com.goodworkalan.mix.task;

import java.io.File;

import com.goodworkalan.comfort.io.Files;
import com.goodworkalan.go.go.Commandable;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.mix.Mix;
import com.goodworkalan.mix.MixError;
import com.goodworkalan.mix.builder.FindStatement;
import com.goodworkalan.mix.builder.FindList;
import com.goodworkalan.mix.builder.RecipeStatement;

/**
 * Delete files within a directory that match find criteria.
 * 
 * @author Alan Gutierrez
 */
public class Prune {
    /** The find criteria. */
    private final FindList findList = new FindList();
    
    private final RecipeStatement recipeElement;

    public Prune(RecipeStatement recipeElement) {
        this.recipeElement = recipeElement;
    }

    public FindStatement<Prune> directory(File directory) {
        return new FindStatement<Prune>(this, findList, directory);
    }
    
    public RecipeStatement end() {
        recipeElement.executable(new Commandable() {
            public void execute(Environment env) {
                Mix mix = env.get(Mix.class, 0);
                for (FindList.Entry entry : findList) {
                    File directory = mix.relativize(entry.getDirectory());
                    for (String fileName : entry.getFind().find(directory)) {
                        File source = new File(directory, fileName);
                        if (!Files.unlink(source)) {
                            throw new MixError(Prune.class, "failure", source);
                        }
                    }
                }
            }
        });
        return recipeElement;
    }
}
