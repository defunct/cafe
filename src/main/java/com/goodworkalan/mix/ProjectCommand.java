package com.goodworkalan.mix;

import java.io.File;

import com.goodworkalan.comfort.io.Files;
import com.goodworkalan.comfort.io.Find;
import com.goodworkalan.go.go.Argument;
import com.goodworkalan.go.go.Commandable;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.mix.builder.Builder;

// TODO Document.
public class ProjectCommand implements Commandable {
    // TODO Document.
    public ProjectCommand() {
    }

    // TODO Document.
    @Argument
    public String projectModule;

    // TODO Document.
    private void execute(ClassLoader classLoader, Builder builder, String projectModuleClassName, boolean exceptional) {
        Class<?> loadedClass;
        try {
            loadedClass = classLoader.loadClass(projectModuleClassName);
        } catch (ClassNotFoundException e) {
            if (exceptional) {
                throw new MixError(MixCommand.class, "project.module.missing", projectModuleClassName);
            }
            return;
        }
        if (ProjectModule.class.isAssignableFrom(loadedClass)) {
            final Class<? extends ProjectModule> projectModuleClass;
            try {
                projectModuleClass = loadedClass.asSubclass(ProjectModule.class);
            } catch (ClassCastException e) {
                if (exceptional) {
                }
                return;
            }
            ProjectModule projectModule;
            try {
                projectModule = projectModuleClass.newInstance();
            } catch (Exception e) {
                throw new MixException(MixCommand.class, "project.module", projectModuleClass, MixCommand.class, e, "project.module", projectModuleClass);
            }
            projectModule.build(builder);
        } else if (exceptional) {
            throw new MixError(MixCommand.class, "not.a.project.module", projectModuleClassName);
        }
    }

    /**
     * Search through the mix build source directory for project modules classes
     * and apply them against a builder to build the project.
     * 
     * @param env
     *            The environment.
     */
    public void execute(Environment env) {
        Mix mix = env.get(Mix.class, 0);
        File output = Files.file(mix.getWorkingDirectory(), "target", "mix-classes");
        Builder builder = new Builder();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (projectModule == null) {
            for (String className : new Find().include("**/*.class").find(output)) {
                className = className.replace("/", ".");
                className = className.replace("\\", ".");
                className = className.substring(0, className.length() - ".class".length());
                execute(classLoader, builder, className, false);
            }
        } else {
            execute(classLoader, builder, projectModule, true);
        }
        env.output(Project.class, builder.createProject(mix.getWorkingDirectory()));
        env.invokeAfter(SiblingsCommand.class);
    }
}
