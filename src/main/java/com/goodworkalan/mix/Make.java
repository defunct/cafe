package com.goodworkalan.mix;

/**
 * Properties of make invocation. 
 *
 * @author Alan Gutierrez
 */
public class Make {
    /** The recipe to make. */
    public final String recipeName;
    
    /**
     * 
     * @param recipeName The recipe to make.
     */
    public Make(String recipeName) {
        this.recipeName = recipeName;
    }
}
