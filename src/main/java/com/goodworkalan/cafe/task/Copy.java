package com.goodworkalan.cafe.task;

import java.io.File;

import com.goodworkalan.cafe.Mix;
import com.goodworkalan.cafe.MixError;
import com.goodworkalan.cafe.builder.FindList;
import com.goodworkalan.cafe.builder.FindStatement;
import com.goodworkalan.cafe.builder.RecipeStatement;
import com.goodworkalan.comfort.io.Files;
import com.goodworkalan.go.go.Commandable;
import com.goodworkalan.go.go.Environment;

/**
 * Recursively copy files from one directory to another.
 * 
 * @author Alan Gutierrez
 */
public class Copy {
    private final RecipeStatement recipeElement;
    
    /** A file query to match files to copy. */
    private final FindList findList = new FindList();

    /** The output directory for the copy. */
    private File outputDirectory;
    
    // TODO Document.
    private final End end = new End();
    
    // TODO Document.
    public Copy(RecipeStatement recipeElement) {
        this.recipeElement = recipeElement;
    }

    // TODO Document.
    public Copy source(FindList findList) {
        end.amend();
        findList.addAll(findList);
        return this;
    }

    // TODO Document.
    public FindStatement<Copy> source(File directory) {
        end.amend();
        return new FindStatement<Copy>(this, findList, directory);
    }

    /**
     * Set the output directory for the copy.
     * 
     * @param outputDirectory
     *            The output directory for the copy.
     */
    public Copy output(File outputDirectory) {
        end.amend();
        this.outputDirectory = outputDirectory;
        return this;
    }

    // TODO Document.
    public RecipeStatement end() {
        end.end();
        return recipeElement.executable(new Commandable() {
            public void execute(Environment env) {
                Mix mix = env.get(Mix.class, 0);
                File output = mix.relativize(outputDirectory);
                for (FindList.Entry entry : findList) {
                    File sourceDirectory = mix.relativize(entry.getDirectory());
                    if (sourceDirectory.isDirectory()) {
                        for (String file : entry.getFind().find(sourceDirectory)) {
                            File source = new File(sourceDirectory, file);
                            File destination = new File(output, file);
                            if (source.isDirectory()) {
                                if (!destination.isDirectory() && !destination.mkdirs()) {
                                    throw new MixError(Copy.class, "mkdirs", destination);
                                }
                            } else {
                                if (!destination.getParentFile().isDirectory() && !destination.getParentFile().mkdirs()) {
                                    throw new MixError(Copy.class, "mkdirs", destination);
                                }
                                Files.copy(source, destination);
                            }
                        }
                    }
                }
            }
        });
    }
}
