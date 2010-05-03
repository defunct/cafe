package com.goodworkalan.mix;

import java.io.File;

import com.goodworkalan.comfort.io.Find;
import com.goodworkalan.go.go.Commandable;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.reflective.ReflectiveException;
import com.goodworkalan.reflective.ReflectiveFactory;

public class ProjectCommand implements Commandable {
    private final File workingDirectory;
    
    private final ReflectiveFactory reflective;
    
    private final File output;

    public ProjectCommand(ReflectiveFactory reflective, File workingDirectory, File output) {
        this.reflective = reflective;
        this.workingDirectory = workingDirectory;
        this.output = output;
    }

    /**
     * Search through the mix build source directory for project modules classes
     * and apply them against a builder to build the project.
     * 
     * @param env
     *            The environment.
     */
    public void execute(Environment env) {
        Builder builder = new Builder();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        for (String className : new Find().include("**/*.class").find(output)) {
            className = className.replace("/", ".");
            className = className.replace("\\", ".");
            className = className.substring(0, className.length() - ".class".length());
            Class<?> foundClass;
            try {
                foundClass = classLoader.loadClass(className);
            } catch (ClassNotFoundException e) {
                // It is to be expected.
                continue;
            }
            if (ProjectModule.class.isAssignableFrom(foundClass)) {
                ProjectModule projectModule;
                try {
                    projectModule = (ProjectModule) reflective.getConstructor(foundClass).newInstance();
                } catch (ReflectiveException e) {
                    throw new MixException(MixCommand.class, "project.module", e);
                }
                projectModule.build(builder);
            }
        }
        env.output(builder.createProject(workingDirectory));
    }
}
