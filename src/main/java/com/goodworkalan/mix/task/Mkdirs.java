package com.goodworkalan.mix.task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.goodworkalan.go.go.Environment;
import com.goodworkalan.mix.Mix;
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
            public void execute(Environment env, Mix mix, Project project, String recipeName) {
                env.verbose(Mkdirs.class, "start", directories);
                for (File directory : directories) {
                    directory = mix.relativize(directory);
                    if (directory.exists()) {
                        if (!directory.isDirectory()) {
                            throw new MixError(Mkdirs.class, "file.exists", directory);
                        }
                        env.debug(Mkdirs.class, "exists", directory);
                    } else if (directory.mkdirs()) {
                        env.debug(Mkdirs.class, "create", directory);
                    } else {
                        throw new MixException(Mkdirs.class, "failure", directory);
                    }
                }
            }
        });
        return recipeElement;
    }
}
