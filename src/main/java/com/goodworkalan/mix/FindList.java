package com.goodworkalan.mix;

import java.io.File;
import java.util.ArrayList;

import com.goodworkalan.glob.Find;

/**
 * Build a list of file finds to execute for a task from the command line.
 * 
 * @author Alan Gutierrez
 */
public class FindList extends ArrayList<FindList.Entry> {
    /** Serial version id. */
    private static final long serialVersionUID = 1L;

    /**
     * A group of files for a particular directory.
     */
    public final static class Entry {
        /** The file directory. */
        private final File directory;

        /** The criteria for included files. */
        private final Find find = new Find();

        /**
         * Create an entry with the given directory.
         * 
         * @param directory
         *            The file directory.
         */
        public Entry(File directory) {
            this.directory = directory;
        }

        /**
         * Get the directory.
         * 
         * @return The directory.
         */
        public File getDirectory() {
            return directory;
        }

        /**
         * Get the criteria for included files.
         * 
         * @return The criteria for included files.
         */
        public Find getFind() {
            return find;
        }
    }

    /**
     * Add the given directory to the find list.
     * 
     * @param directory
     *            The directory to add.
     */
    public void addDirectory(File directory) {
        add(new Entry(directory));
    }

    /**
     * Apply the include criteria to the find of the last directory added to
     * this find list.
     * 
     * @param include
     *            The include pattern.
     */
    public void addInclude(String include) {
        get(size() - 1).find.include(include);
    }

    /**
     * Apply the exclude criteria to the find of the last directory added to
     * this find list.
     * 
     * @param exclude
     *            The exclude pattern.
     */
    public void addExclude(String exclude) {
        get(size() - 1).find.exclude(exclude);
    }
}
