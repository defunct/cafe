package com.goodworkalan.mix;

import java.io.File;
import java.util.Collections;
import java.util.Set;

import com.goodworkalan.glob.Find;

/**
 * Describes a software project, its source files, outputs and dependencies.
 * 
 * @author Alan Gutierrez
 */
public class Project {
    private final File workingDirectory;
    
    public Project(File workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    /**
     * Get the set of source files for the given directory.
     * 
     * @return The set of source files for the given directory.
     */
    public Set<File> getSourceDirectories() {
        return Collections.singleton(new File(workingDirectory, "src/main/java"));
    }

    /**
     * Get the set of source files for the given source directory.
     * 
     * @return A set of source files.
     */
    public Set<File> getSources(File directory) {
        return new Find().include("**/*.java").find(directory);
    }
}
