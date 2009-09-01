package com.goodworkalan.mix;

import java.io.File;

import com.goodworkalan.go.go.Task;

public class Mix extends Task {
    public static class Arguments {
        private File workingDirectory;
        
        public void setWorkingDirectory(File workingDirectory) {
            this.workingDirectory = workingDirectory;
        }

        public File getWorkingDirectory() {
            return workingDirectory;
        }
    }
    
    public void execute() {
    }
}
