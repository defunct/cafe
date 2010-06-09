package com.goodworkalan.mix.task;

import java.io.File;


public class FindElement<T> {
    private final T parent;

    private final FindList findList;

    public FindElement(T parent, FindList findList, File directory) {
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
    public FindElement<T> include(String include) {
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
    public FindElement<T> exclude(String exclude) {
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
    public FindElement<T> isFile() {
        findList.isFile();
        return this;
    }

    public T end() {
        return parent;
    }
}
