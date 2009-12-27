package com.goodworkalan.mix.task;

import java.io.File;

import com.goodworkalan.comfort.io.Files;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.mix.FindList;
import com.goodworkalan.mix.MixCommand;
import com.goodworkalan.mix.MixError;
import com.goodworkalan.mix.Project;
import com.goodworkalan.mix.builder.Executable;
import com.goodworkalan.mix.builder.RecipeElement;

/**
 * Delete files within a directory that match find criteria.
 * 
 * @author Alan Gutierrez
 */
public class Prune {
    /** The find criteria. */
    private final FindList findList = new FindList();
    
    private final RecipeElement recipeElement;

    public Prune(RecipeElement recipeElement) {
        this.recipeElement = recipeElement;
    }

    public FindElement<Prune> directory(File directory) {
        return new FindElement<Prune>(this, findList, directory);
    }
    
    public RecipeElement end() {
        recipeElement.addExecutable(new Executable() {
            public void execute(Environment env, MixCommand.Arguments mix, Project project, String recipeName) {
                for (FindList.Entry entry : findList) {
                    File directory = mix.relativize(entry.getDirectory());
                    for (String fileName : entry.getFind().find(directory)) {
                        File source = new File(directory, fileName);
                        if (!Files.delete(source)) {
                            throw new MixError(Prune.class, "failure", source);
                        }
                    }
                }
            }
        });
        return recipeElement;
    }
}
