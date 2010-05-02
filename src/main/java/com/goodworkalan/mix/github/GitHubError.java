package com.goodworkalan.mix.github;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.goodworkalan.go.go.Erroneous;

public class GitHubError extends RuntimeException implements Erroneous {
    /** Serial version id. */
    private static final long serialVersionUID = 1L;

    /** The error context. */
    private final Class<?> context;
    
    /** The error code. */
    private final String code;
    
    /** The detail message format arguments. */
    private final Object[] arguments;

    /**
     * Create a mix error with the given error code.
     * 
     * @param context
     *            The error context.
     * @param code
     *            The error code.
     */
    public GitHubError(Class<?> context, String code, Object...arguments) {
        this.context = context;
        this.code = code;
        this.arguments = arguments;
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
    public GitHubError(Class<?> context, String code, Throwable cause, Object...arguments) {
        super(null, cause);
        this.context = context;
        this.code = code;
        this.arguments = arguments;
    }
    
    public int getExitCode() {
        return 1;
    }

    /**
     * Returns the detail message string of this error.
     * 
     * @return The detail message string of this error.
     */
    @Override
    public String getMessage() {
        ResourceBundle bundle = ResourceBundle.getBundle(context.getPackage().getName() + ".exceptions");
        String className = context.getCanonicalName();
        int index = className.lastIndexOf('.');
        if (index > -1) {
            className = className.substring(index + 1);
        }
        String key = className + "/" + code;
        try {
            return String.format(bundle.getString(key), arguments);
        } catch (MissingResourceException e) {
            return key;
        }
    }
}
