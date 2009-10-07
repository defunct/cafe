package com.goodworkalan.mix;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.goodworkalan.go.go.Argument;
import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.Task;

@Command(parent = MixTask.class)
public class MkdirsTask extends Task {
    private final List<File> directories = new ArrayList<File>();
    
    @Argument
    public void addDirectory(File directory) {
        directories.add(directory);
    }

    @Override
    public void execute(Environment env) {
        for (File directory : directories) {
            if (directory.exists()) {
                if (!directory.isDirectory()) {
                    throw new MixException(0);
                }
            } else if (!directory.mkdirs()) {
                throw new MixException(0);
            }
        }
    }
}
