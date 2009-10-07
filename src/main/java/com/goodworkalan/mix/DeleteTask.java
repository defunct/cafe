package com.goodworkalan.mix;

import java.io.File;

import com.goodworkalan.glob.Files;
import com.goodworkalan.go.go.Argument;
import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.Task;

/**
 * Delete a file or directory.
 * <p>
 * Delete is pretty blunt instrument in
 * 
 * @author Alan Gutierrez
 */
@Command(parent = MixTask.class)
public class DeleteTask extends Task {
    /** The file to delete. */
    private File file;
    
    /** If true delete recursively. */
    private boolean recurse;

    /**
     * The file or directory to delete.
     * 
     * @param file
     *            The file or directory to delete.
     */
    @Argument
    public void addFile(File file) {
        this.file = file;
    }

    /**
     * Whether or not to delete recursively if file is a directory.
     * 
     * @param recurse
     *            If true, delete file recursively if it is a directory.
     */
    @Argument
    public void addRecurse(boolean recurse) {
        this.recurse = recurse;
    }

    /**
     * Delete the file.
     * 
     * @param env
     *            The execution environment.
     */
    @Override
    public void execute(Environment environment) {
        if (!recurse) {
            if (file.exists() && !file.delete()) {
                throw new MixError(0);
            }
        } else if (!Files.delete(file)) {
            throw new MixError(0);
        }
    }
}
