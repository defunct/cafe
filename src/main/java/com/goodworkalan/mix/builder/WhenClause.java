package com.goodworkalan.mix.builder;

import java.io.File;
import java.util.List;

import com.goodworkalan.mix.Rebuild;

/**
 * An element in the domain-specific language that specifies the source files of
 * a rebuild if dirty test.
 * 
 * @author Alan Guiterrez
 */
public class WhenClause {
    /** The parent recipe element in the domain-specific language. */
    private final RecipeStatement parent;
    
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
    public WhenClause(RecipeStatement parent, List<Rebuild> rebuilds) {
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
    public FindStatement<WhenClause> source(File directory) {
        return new FindStatement<WhenClause>(this, sources, directory);
    }

    /**
     * Return a newer than element that is used to specify the output files of
     * the rebuild if dirty test.
     * 
     * @return A newer than element to specify the output files of the rebuild
     *         if dirty test.
     */
    public NewerThanClause newerThan() {
        return new NewerThanClause(parent, rebuilds, sources);
    }
}
