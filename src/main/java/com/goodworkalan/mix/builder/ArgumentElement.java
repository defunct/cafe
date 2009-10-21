package com.goodworkalan.mix.builder;

import java.util.List;

/**
 * An argument specification element.
 * 
 * @author Alan Gutierrez
 */
public class ArgumentElement<P> {
    /** The parent language element. */
    private final P parent;
    
    /** The list of named values. */
    private final List<NamedValue> namedValue;

    /**
     * Create an argument language element that appends arguments to the given
     * list of named values and returns the given parent at the end of the
     * statement..
     * 
     * @param namedValue
     *            The list of named values.
     */
    public ArgumentElement(P parent, List<NamedValue> namedValue) {
        this.parent = parent;
        this.namedValue = namedValue;
    }

    /**
     * Add an argument to the list of arguments.
     * 
     * @param name
     *            The argument name.
     * @param value
     *            The argument value.
     * @return This argument language element in order to continue specifying
     *         arguments.
     */
    public ArgumentElement<P> argument(String name, String value) {
        namedValue.add(new NamedValue(name, value));
        return this;
    }

    /**
     * Terminate the argument specification statement.
     * 
     * @return The parent langauge element..
     */
    public P end() {
        return parent;
    }
}
