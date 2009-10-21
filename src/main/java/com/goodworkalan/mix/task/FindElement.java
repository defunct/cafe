package com.goodworkalan.mix.task;

import java.io.File;

import com.goodworkalan.mix.FindList;

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
     */
    public FindElement<T> exclude(String include) {
        findList.addExclude(include);
        return this;
    }

    public T end() {
        return parent;
    }
}
