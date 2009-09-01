package com.goodworkalan.mix;

import java.io.File;

import com.goodworkalan.go.go.Task;

/**
 * Root command for Mix.
 * 
 * @author Alan Gutierrez
 */
public class Mix extends Task {
    /**
     * Arguments common to all Mix tasks.
     * 
     * @author Alan Gutierrez
     */
    public static class Arguments {
        /**
         * The project root directory, defaults to the current working
         * directory.
         */
        private File workingDirectory;

        /**
         * Set the project root directory.
         * 
         * @param workingDirectory
         *            The project root directory.
         */
        public void setWorkingDirectory(File workingDirectory) {
            this.workingDirectory = workingDirectory;
        }

        /**
         * Get the project root directory.
         * 
         * @return The project root directory.
         */
        public File getWorkingDirectory() {
            return workingDirectory;
        }
    }

    public void execute() {
    }
}
