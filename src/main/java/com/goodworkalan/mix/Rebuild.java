package com.goodworkalan.mix;

import java.io.File;

import com.goodworkalan.comfort.io.Find;
import com.goodworkalan.mix.task.FindList;

/**
 * Test whether a set of source files is newer than a set of output files. The
 * test returns true if any of the sources are newer than any of the outputs, or
 * if there are no outputs, or if there are no sources.
 * 
 * @author Alan Gutierrez
 */
public class Rebuild {
    /** The set of criteria to match sources. */
    private final FindList sources;

    /** The set of criteria to match outputs. */
    private final FindList outputs;

    /**
     * Construct a rebuild test that will return true if the given sources are
     * newer than the given outputs.
     * 
     * @param sources
     *            The set of criteria to match sources.
     * @param outputs
     *            The set of criteria to match outputs.
     */
    public Rebuild(FindList sources, FindList outputs) {
        this.sources = sources;
        this.outputs = outputs;
    }

    /**
     * Return true if any of the sources are newer than any of the outputs, or
     * if there are no outputs, or if there are no sources.
     * 
     * @return True if a rebuild is required.
     */
    public boolean isDirty(Mix mix) {
        long newest = 0L;
        for (FindList.Entry entry : sources) {
            Find find = entry.getFind();
            File directory = mix.relativize(entry.getDirectory());
            for (String fileName : find.find(directory)) {
                File source = new File(directory, fileName);
                if (source.lastModified() > newest) {
                    newest = source.lastModified();
                }
            }
        }
        if (newest == 0L) {
            return true;
        }
        int count = 0;
        for (FindList.Entry entry : outputs) {
            Find find = entry.getFind();
            File directory = mix.relativize(entry.getDirectory());
            for (String fileName : find.find(directory)) {
                count++;
                File output = new File(directory, fileName);
                if (output.lastModified() < newest) {
                    return true;
                }
            }
        }
        return count == 0;
    }
}
