package com.goodworkalan.mix.builder;

import java.io.File;
import java.util.List;

import com.goodworkalan.mix.Rebuild;

/**
 * An element in the domain-specific language that specifies the output files of
 * a rebuild if dirty test.
 * 
 * @author Alan Guiterrez
 */
public class NewerThanClause {
    /** The parent recipe element in the domain-specific language. */
    private final RecipeStatement parent;
    
    /** The list of rebuilds for the recipe. */
    private final List<Rebuild> rebuilds;
    
    /** The set of criteria to match sources. */
    private final FindList sources;
    
    /** The set of criteria to match outputs. */
    private final FindList outputs;

    /**
     * Create a new newer than element.
     * 
     * @param parent
     *            The parent recipe element in the domain-specific language.
     * @param rebuilds
     *            The list of rebuilds for the recipe.
     * @param sources
     *            The set of criteria to match sources.
     */
    NewerThanClause(RecipeStatement parent, List<Rebuild> rebuilds, FindList sources) {
        this.parent = parent;
        this.rebuilds = rebuilds;
        this.sources = sources;
        this.outputs = new FindList();
    }

    /**
     * Specify the output files to compare to the source files.
     * 
     * @param directory
     *            The root directory in which to perform the find.
     * @return A find element to specify file match criteria for files within
     *         the given directory.
     */
    public FindStatement<NewerThanClause> output(File directory) {
        return new FindStatement<NewerThanClause>(this, outputs, directory);
    }

    /**
     * Add a rebuild test to the list of rebuild tests for the recipe.
     * 
     * @return The parent recipe element.
     */
    public RecipeStatement end() {
        rebuilds.add(new Rebuild(sources, outputs));
        return parent;
    }
}
