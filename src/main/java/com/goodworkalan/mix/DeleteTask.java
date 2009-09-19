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
     * The directory to delete.
     * 
     * @param directory
     *            The directory to delete.
     */
    @Argument
    public void addDirectory(File directory) {
        this.file = directory;
    }

    /**
     * Delete the file.
     * 
     * @param env
     *            The execution environment.
     */
    @Override
    public void execute(Environment environment) {
        if (!Files.delete(file)) {
            throw new MixException(0);
        }
    }
}
