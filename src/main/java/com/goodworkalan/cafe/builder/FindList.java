package com.goodworkalan.cafe.builder;

import java.io.File;
import java.util.ArrayList;

import com.goodworkalan.comfort.io.Find;

/**
 * A collection of {@link Find} instances each paired a single directory to
 * search using the <code>Find</code> instance.
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
	 * Add the given directory to the find list paired with an empty
	 * {@link Find} instance.
	 * 
	 * @param directory
	 *            The directory to add.
	 */
    public void addDirectory(File directory) {
        add(new Entry(directory));
    }

	/**
	 * Apply the include criteria to the associated {@link Find} of the last
	 * directory added to this find list.
	 * 
	 * @param include
	 *            The include pattern.
	 */
    public void addInclude(String include) {
        get(size() - 1).find.include(include);
    }

	/**
	 * Apply the exclude criteria to the associated {@link Find} of the last
	 * directory added to this find list.
	 * 
	 * @param exclude
	 *            The exclude pattern.
	 */
    public void addExclude(String exclude) {
        get(size() - 1).find.exclude(exclude);
    }

	/**
	 * Apply the file type criteria to the find of the last directory added to
	 * this find list.
	 * <p>
	 * This will include only files that are <em>normal</em> files according to
	 * <code>File.isFile</code>. A files is <em>normal</em> if it is not a
	 * directory and satisfies additional system specific criteria. Files
	 * created in Java that are not directories are considered <em>normal</em>
	 * files.
	 */
    public void filesOnly() {
        get(size() - 1).find.filesOnly();
    }
}
