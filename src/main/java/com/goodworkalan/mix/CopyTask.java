package com.goodworkalan.mix;

import java.io.File;

import com.goodworkalan.glob.Files;
import com.goodworkalan.glob.Find;
import com.goodworkalan.go.go.Argument;
import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.Task;

/**
 * Recursively copy files from one directory to another.
 * 
 * @author Alan Gutierrez
 */
@Command(parent = MixTask.class)
public class CopyTask extends Task {
    /** A file query to match files to copy. */
    private final Find find = new Find();

    /** The source directory for the copy. */
    private File sourceDirectory;

    /** The output directory for the copy. */
    private File outputDirectory;
    
    /**
     * Set the source directory for the copy.
     * 
     * @param sourceDirectory
     *            The source directory for the copy.
     */
    @Argument
    public void addSourceDirectory(File sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
    }

    /**
     * Set the output directory for the copy.
     * 
     * @param outputDirectory
     *            The output directory for the copy.
     */
    @Argument
    public void addOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    /**
     * Include files that match the given glob include pattern.
     * 
     * @param include
     *            Files must match this pattern to be included.
     */
    @Argument
    public void addInclude(String include) {
        find.include(include);
    }

    /**
     *  Exclude files that match the given glob include pattern.
     * 
     * @param exclude
     *            Files that match this pattern are excluded.
     */
    @Argument
    public void addExclude(String exclude) {
        find.exclude(exclude);
    }

    /**
     * Copy files from the source to the destination.
     * 
     * @param env
     *            The environment.
     */
    @Override
    public void execute(Environment env) {
        if (sourceDirectory.isDirectory()) {
            if (!outputDirectory.isDirectory() && !outputDirectory.mkdirs()) {
                throw new MixException(0);
            }
            for (String file : find.find(sourceDirectory)) {
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
