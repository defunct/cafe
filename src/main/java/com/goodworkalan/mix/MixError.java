package com.goodworkalan.mix;

/**
 * A Mix exception that indicates a configuration or other error that the
 * programmer could remedy.
 * 
 * @author Alan Gutierrez
 */
public class MixError extends MixException {
    /** Serial version id. */
    private static final long serialVersionUID = 1L;

    /**
     * Create a mix error with the given error code.
     * 
     * @param code
     *            The error code.
     */
    public MixError(int code) {
        super(code);
    }
}
