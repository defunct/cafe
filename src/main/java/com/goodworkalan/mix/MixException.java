package com.goodworkalan.mix;

import com.goodworkalan.danger.ContextualDanger;

/**
 * An exception raised by the Mix application.
 * 
 * @author Alan Gutierrez
 */
public class MixException extends ContextualDanger {
    /** Serial version id. */
    private static final long serialVersionUID = 1L;

    /**
     * Create a mix exception with the given error code and message format
     * arguments.
     * 
     * @param context
     *            The error context.
     * @param code
     *            The error code.
     * @param arguments
     *            The format arguments.
     */
    public MixException(Class<?> context, String code, Object...arguments) {
        this(context, code, null, arguments);
    }

    /**
     * Create a mix exception with the given error code, cause and message
     * format arguments.
     * 
     * @param context
     *            The error context.
     * @param code
     *            The error code.
     * @param cause
     *            The cause.
     * @param arguments
     *            The format arguments.
     */
    public MixException(Class<?> context, String code, Throwable cause, Object...arguments) {
        super(context, code, cause, arguments);
    }
}
