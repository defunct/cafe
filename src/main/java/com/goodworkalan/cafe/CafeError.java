package com.goodworkalan.cafe;

import com.goodworkalan.danger.Danger;
import com.goodworkalan.go.go.Erroneous;


/**
 * A Mix exception that indicates a configuration or other error that the
 * programmer could remedy.
 * 
 * @author Alan Gutierrez
 */
public class CafeError extends Danger implements Erroneous {
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
    public CafeError(Class<?> context, String code, Object...arguments) {
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
    public CafeError(Class<?> context, String code, Throwable cause, Object...arguments) {
        super(context, code, cause, arguments);
    }
    
    public int getExitCode() {
        return 1;
    }
}
