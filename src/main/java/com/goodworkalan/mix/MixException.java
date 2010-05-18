package com.goodworkalan.mix;

import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.goodworkalan.danger.ContextualDanger;
import com.goodworkalan.reflective.Reflection;
import com.goodworkalan.reflective.Reflective;
import com.goodworkalan.reflective.ReflectiveException;

/**
 * An exception raised by the Mix application.
 * 
 * @author Alan Gutierrez
 */
public class MixException extends ContextualDanger {
    /** Serial version id. */
    private static final long serialVersionUID = 1L;
   
    /** The cache of resource bundles for the parent exception class. */
    private final static ConcurrentMap<String, ResourceBundle> BUNDLE = new ConcurrentHashMap<String, ResourceBundle>();

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
        super(BUNDLE, context, code, cause, arguments);
    }
    
    public static <T> T reflect(Reflection<T> reflection, Class<?> contextClass, String messageKey, Object...arguments) {
        try {
            return new Reflective().reflect(reflection);
        } catch (ReflectiveException e) {
            throw new MixException(contextClass, messageKey, e, arguments);
        }
    }
}
