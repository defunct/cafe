package com.goodworkalan.cafe.task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.goodworkalan.cafe.Build;
import com.goodworkalan.cafe.MixError;
import com.goodworkalan.cafe.MixException;
import com.goodworkalan.cafe.builder.RecipeStatement;
import com.goodworkalan.go.go.Commandable;
import com.goodworkalan.go.go.Environment;

// TODO Document.
public class Mkdirs {
    // TODO Document.
    private final List<File> directories = new ArrayList<File>();

    // TODO Document.
    private final RecipeStatement recipeElement;
    
    // TODO Document.
    public Mkdirs(RecipeStatement recipeElement) {
        this.recipeElement = recipeElement;
    }

    // TODO Document.
    public Mkdirs directory(File directory) {
        directories.add(directory);
        return this;
    }
    
    // TODO Document.
    public RecipeStatement end() {
        recipeElement.executable(new Commandable() {
            public void execute(Environment env) {
                Build mix = env.get(Build.class, 0);
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
