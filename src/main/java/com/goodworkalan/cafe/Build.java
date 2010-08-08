package com.goodworkalan.cafe;

import java.io.File;

/**
 * Arguments common to all Mix tasks.
 * 
 * @author Alan Gutierrez
 */
public class Build {
    /**
     * The project root directory, defaults to the current working
     * directory.
     */
    public final File workingDirectory;
    
    /** Whether or not Mix is being run without an Internet connection. */
    public final boolean offline;
    
    /** Whether or not we use siblings when we can instead of artifacts. */
    public final boolean siblings;
    
        /**
     * 
     * @param workingDirectory
     *            The working directory.
     * @param offline
     *            If true, the application should not use the network.
     * @param siblings
     *            If true, invoke the command on all siblings.
     */
    public Build(File workingDirectory, boolean offline, boolean siblings) {
        this.workingDirectory = workingDirectory;
        this.offline = offline;
        this.siblings = siblings;
    }

    /**
     * Get the project root directory.
     * 
     * @return The project root directory.
     */
    public File getWorkingDirectory() {
        return workingDirectory;
    }

    /**
     * Get whether or not Mix is being run without an Internet connection.
     * 
     * @return True Mix is being run without an Internet connection.
     */
    public boolean isOffline() {
        return offline;
    }
    
    // TODO Document.
    public boolean isSiblings() {
        return siblings;
    }
    
    // TODO Document.
    public File relativize(File file) {
        if (!file.isAbsolute()) {
            return new File(workingDirectory, file.getPath());
        }
        return file;
    }
}
