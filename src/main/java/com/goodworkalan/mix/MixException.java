package com.goodworkalan.mix;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * An exception raised by the Mix application.
 * 
 * @author Alan Gutierrez
 */
public class MixException extends RuntimeException {
    /** Serial version id. */
    private static final long serialVersionUID = 1L;
   
    /** The error context. */
    private final Class<?> context;
    
    /** The error code. */
    private final String code;
    
    /** The detail message format arguments. */
    private final Object[] arguments;

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
        super(null, cause);
        this.context = context;
        this.code = code;
        this.arguments = arguments;
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
