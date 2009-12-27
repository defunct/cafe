package com.goodworkalan.mix.task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.goodworkalan.go.go.Environment;
import com.goodworkalan.mix.MixError;
import com.goodworkalan.mix.MixException;
import com.goodworkalan.mix.Project;
import com.goodworkalan.mix.builder.Executable;
import com.goodworkalan.mix.builder.RecipeElement;

public class Mkdirs {
    private final List<File> directories = new ArrayList<File>();

    private final RecipeElement recipeElement;
    
    public Mkdirs(RecipeElement recipeElement) {
        this.recipeElement = recipeElement;
    }

    public Mkdirs directory(File directory) {
        directories.add(directory);
        return this;
    }
    
    public RecipeElement end() {
        recipeElement.addExecutable(new Executable() {
            public void execute(Environment env, Project project, String recipeName) {
                for (File directory : directories) {
                    if (directory.exists()) {
                        if (!directory.isDirectory()) {
                            throw new MixError(Mkdirs.class, "file.exists", directory);
                        }
                    } else if (!directory.mkdirs()) {
                        throw new MixException(Mkdirs.class, "failure", directory);
                    }
                }
            }
        });
        return recipeElement;
    }
}
