package com.goodworkalan.mix;

import java.io.File;

import com.goodworkalan.glob.Files;
import com.goodworkalan.go.go.Argument;
import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.Task;

/**
 * Delete files within a directory that match find criteria.
 * 
 * @author Alan Gutierrez
 */
@Command(parent = MixTask.class)
public class PruneTask extends Task {
    /** The find criteria. */
    private final FindList findList = new FindList();

    /**
     * Add a directory to prune.
     * 
     * @param directory
     *            The source directory.
     */
    @Argument
    public void addDirectory(File directory) {
        findList.addDirectory(directory);
    }

    /**
     * Delete the files that match the given glob include pattern.
     * 
     * @param include
     *            Files must match this pattern to be deleted.
     */
    @Argument
    public void addInclude(String include) {
        findList.addInclude(include);
    }

    /**
     * Do not delete the files that match the given glob include pattern
     * 
     * @param exclude
     *            Files must not match this pattern to be deleted.
     */
    @Argument
    public void addExclude(String exclude) {
        findList.addExclude(exclude);
    }

    /**
     * Delete the files and directories that match the find criteria.
     * 
     * @param env
     *            The execution environment.
     */
    @Override
    public void execute(Environment env) {
        for (FindList.Entry entry : findList) {
            for (String fileName : entry.getFileNames()) {
                File source = new File(entry.getDirectory(), fileName);
                if (!Files.delete(source)) {
                    throw new MixException(0);
                }
            }
        }
    }
}
