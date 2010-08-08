package com.goodworkalan.cafe.builder;

import java.io.File;

/**
 * Specify files to find on the file system in the domain-specific project
 * builder language. The statement builds a find list, which is a collection of
 * file finder paired the the single directory the file finder will search.
 * 
 * @author Alan Gutierrez
 * 
 * @param <T>
 *            The type of the parent domain-specific language element to return
 *            at the end of the statement.
 */
public class FindStatement<T> {
	/** The parent domains-specific language element. */
    private final T parent;

    /** The file list. */
    private final FindList findList;

    // TODO Document.
    public FindStatement(T parent, FindList findList, File directory) {
        this.findList = findList;
        this.parent = parent;
        this.findList.addDirectory(directory);
    }
    
    /**
     * Apply the include criteria to the last directory added to this task.
     * 
     * @param include
     *            The include pattern.
     * @return This domain specific language find element in order to continue
     *         specifying find criteria.
     */
    public FindStatement<T> include(String include) {
        findList.addInclude(include);
        return this;
    }

    /**
     * Apply the exclude criteria to the last directory added to this task.
     * 
     * @param exclude
     *            The exclude pattern.
     * @return This domain specific language find element in order to continue
     *         specifying find criteria.
     */
    public FindStatement<T> exclude(String exclude) {
        findList.addExclude(exclude);
        return this;
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
     * 
     * @return This domain specific language find element in order to continue
     *         specifying find criteria.
     */
    public FindStatement<T> isFile() {
        findList.filesOnly();
        return this;
    }

	/**
	 * Terminate the find clause and return the parent domains-specific language
	 * element.
	 * 
	 * @return The parent domains-specific language element.
	 */
    public T end() {
        return parent;
    }
}
