package com.goodworkalan.mix.task;

import java.io.File;

import com.goodworkalan.comfort.io.Files;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.mix.FindList;
import com.goodworkalan.mix.Mix;
import com.goodworkalan.mix.MixError;
import com.goodworkalan.mix.builder.Executable;
import com.goodworkalan.mix.builder.RecipeStatement;

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
    
    private final End end = new End();
    
    public Copy(RecipeStatement recipeElement) {
        this.recipeElement = recipeElement;
    }

    public Copy source(FindList findList) {
        end.amend();
        findList.addAll(findList);
        return this;
    }

    public FindElement<Copy> source(File directory) {
        end.amend();
        return new FindElement<Copy>(this, findList, directory);
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

    public RecipeStatement end() {
        end.end();
        return recipeElement.executable(new Executable() {
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
