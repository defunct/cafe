package com.goodworkalan.mix.task;

import java.io.File;

import com.goodworkalan.glob.Files;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.mix.FindList;
import com.goodworkalan.mix.MixException;
import com.goodworkalan.mix.Project;
import com.goodworkalan.mix.builder.Executable;
import com.goodworkalan.mix.builder.RecipeElement;

/**
 * Recursively copy files from one directory to another.
 * 
 * @author Alan Gutierrez
 */
public class Copy {
    private final RecipeElement recipeElement;
    
    /** A file query to match files to copy. */
    private final FindList findList = new FindList();

    /** The output directory for the copy. */
    private File outputDirectory;
    
    public Copy(RecipeElement recipeElement) {
        this.recipeElement = recipeElement;
    }

    public Copy source(FindList findList) {
        findList.addAll(findList);
        return this;
    }

    public FindElement<Copy> source(File directory) {
        return new FindElement<Copy>(this, findList, directory);
    }

    /**
     * Set the output directory for the copy.
     * 
     * @param outputDirectory
     *            The output directory for the copy.
     */
    public Copy output(File outputDirectory) {
        this.outputDirectory = outputDirectory;
        return this;
    }

    public RecipeElement end() {
        recipeElement.addExecutable(new Executable() {
            public void execute(Project project, Environment env) {
                for (FindList.Entry entry : findList) {
                    File sourceDirectory = entry.getDirectory();
                    if (sourceDirectory.isDirectory()) {
                        if (!outputDirectory.isDirectory() && !outputDirectory.mkdirs()) {
                            throw new MixException(0);
                        }
                        for (String file : entry.getFind().find(sourceDirectory)) {
                            File source = new File(sourceDirectory, file);
                            File destination = new File(outputDirectory, file);
                            if (source.isDirectory()) {
                                if (!destination.isDirectory() && !destination.mkdirs()) {
                                    throw new MixException(0);
                                }
                            } else {
                                if (!destination.getParentFile().isDirectory() && !destination.getParentFile().mkdirs()) {
                                    throw new MixException(0);
                                }
                                Files.copy(source, destination);
                            }
                        }
                    }
                }
            }
        });
        return recipeElement;
    }
}
