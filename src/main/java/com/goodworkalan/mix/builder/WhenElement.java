package com.goodworkalan.mix.builder;

import java.io.File;
import java.util.List;

import com.goodworkalan.mix.FindList;
import com.goodworkalan.mix.task.FindElement;

/**
 * An element in the domain-specific language that specifies the source files of
 * a rebuild if dirty test.
 * 
 * @author Alan Guiterrez
 */
public class WhenElement {
    /** The parent recipe element in the domain-specific language. */
    private final RecipeBuilder parent;
    
    /** The list of rebuilds for the recipe. */
    private final List<Rebuild> rebuilds;
    
    /** The set of criteria to match sources. */
    private final FindList sources;

    /**
     * Create a new when element.
     * 
     * @param parent
     *            The parent recipe element in the domain-specific language.
     * @param rebuilds
     *            The list of rebuilds for the recipe.
     */
    public WhenElement(RecipeBuilder parent, List<Rebuild> rebuilds) {
        this.parent = parent;
        this.rebuilds = rebuilds;
        this.sources = new FindList();
    }

    /**
     * Specify the source files to compare to the output files.
     * 
     * @param directory
     *            The root directory in which to perform the find.
     * @return A find element to specify file match criteria for files within
     *         the given directory.
     */
    public FindElement<WhenElement> source(File directory) {
        return new FindElement<WhenElement>(this, sources, directory);
    }

    /**
     * Return a newer than element that is used to specify the output files of
     * the rebuild if dirty test.
     * 
     * @return A newer than element to specify the output files of the rebuild
     *         if dirty test.
     */
    public NewerThanElement newerThan() {
        return new NewerThanElement(parent, rebuilds, sources);
    }
}
