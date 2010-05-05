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
     * @param context
     *            The error context.
     * @param code
     *            The error code.
     */
    public MixError(Class<?> context, String code, Object...arguments) {
        super(context, code, arguments);
    }

    /**
     * Create a mix error with the given error code.
     * 
     * @param context
     *            The error context.
     * @param code
     *            The error code.
     * @param cause
     *            The cause.
     */
    public MixError(Class<?> context, String code, Throwable cause, Object...arguments) {
        super(context, code, cause, arguments);
    }
}
