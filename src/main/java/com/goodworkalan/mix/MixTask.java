package com.goodworkalan.mix;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

import com.goodworkalan.glob.Find;
import com.goodworkalan.go.go.Arguable;
import com.goodworkalan.go.go.Argument;
import com.goodworkalan.go.go.CommandInterpreter;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.Output;
import com.goodworkalan.go.go.Task;
import com.goodworkalan.reflective.ReflectiveException;
import com.goodworkalan.reflective.ReflectiveFactory;

/**
 * Root command for Mix.
 * 
 * @author Alan Gutierrez
 */
public class MixTask extends Task {
    /** Use to create project modules. */
    private final ReflectiveFactory reflectiveFactory;
    
    /** If true, attempt to load a project. */
    private boolean project = true;
    
    public MixTask() {
        this(new ReflectiveFactory());
    }
    
    MixTask(ReflectiveFactory reflectiveFactory) {
        this.reflectiveFactory = reflectiveFactory;
    }
    
    /**
     * Set whether not a project should be loaded.
     * 
     * @param project If true a project should be loaded.
     */
    @Argument
    public void addProject(boolean project) {
        this.project = project;
    }
    
    /**
     * Arguments common to all Mix tasks.
     * 
     * @author Alan Gutierrez
     */
    public static class Arguments implements Arguable {
        /**
         * The project root directory, defaults to the current working
         * directory.
         */
        private File workingDirectory = new File(".");
        
        private boolean offline;

        /**
         * Set the project root directory.
         * 
         * @param workingDirectory
         *            The project root directory.
         */
        @Argument
        public void addWorkingDirectory(File workingDirectory) {
            this.workingDirectory = workingDirectory;
        }

        /**
         * Get the project root directory.
         * 
         * @return The project root directory.
         */
        public File getWorkingDirectory() {
            return workingDirectory;
        }
        
        @Argument
        public void addOffline(boolean offline) {
            this.offline = offline;
        }
        
        public boolean isOffline() {
            return offline;
        }
    }
    
    /**
     * The output from running the mix task.
     */
    public final static class Configuration implements Output {
        private final Project project;
        
        public Configuration(Project project) {
            this.project = project;
        }
        
        public Project getProject() {
            return project;
        }
    }
    
    private Configuration configuration;
    
    private Arguments arguments = new Arguments();
    
    public void setArguments(Arguments arguments) {
        this.arguments = arguments;
    }
    
    @Override
    public void execute(Environment env) {
        // Need to run the compiler out of the context (one more reason
        // why compilers are not pluggable) of the compiler command.
        Builder builder = new Builder();
        if (project) {
            Properties properties = new Properties();
            File propertiesFile = new File(arguments.getWorkingDirectory(), "mix.properties");
            if (propertiesFile.exists()) {
                try {
                    properties.load(new FileInputStream(propertiesFile));
                } catch (IOException e) {
                    throw new MixException(0, e);
                }
            }
            String outputDirectoryName = properties.getProperty("output", "target/mix-classes");
            File outputDirectory = new File(outputDirectoryName);
            if (!outputDirectory.isAbsolute()) {
                outputDirectory = new File(arguments.getWorkingDirectory(), outputDirectoryName);
            }
            if (!outputDirectory.exists()) {
                if (!outputDirectory.mkdirs()) {
                    throw new MixException(0);
                }
            } else if (!outputDirectory.isDirectory()) {
                throw new MixException(0);
            }
            if (new Find().include("**/*.class").find(outputDirectory).isEmpty()) {
                // FIXME Do a dependency check instead.
                String sourceDirectoryName = properties.getProperty("source", "src/mix/java");
                if (new File(sourceDirectoryName).isAbsolute()) {
                    throw new MixException(0);
                }
                File sourceDirectory = new File(arguments.getWorkingDirectory(), sourceDirectoryName);
                if (sourceDirectory.isDirectory()) {
                    CommandInterpreter interpreter = env.part.getCommandInterpreter();
                    interpreter
                        .command("mix", "--no-project")
                        .command("javac")
                            .argument("artifact", "com.goodworkalan/mix/0.1")
                            .argument("source-directory", sourceDirectory.toString())
                            .argument("output-directory", outputDirectory.toString())
                        .execute();
                }
                // FIXME Do resources too.
            } else if (!outputDirectory.isDirectory()) {
                throw new MixException(0);
            }
            if (outputDirectory.exists()) {
                ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
                try {
                    ClassLoader classLoader;
                    try {
                        classLoader = new URLClassLoader(new URL[] { outputDirectory.getAbsoluteFile().toURL() }, currentClassLoader);
                    } catch (MalformedURLException e) {
                        throw new MixException(0, e);
                    }
                    Thread.currentThread().setContextClassLoader(classLoader);
                    for (String className : new Find().include("**/*.class").find(outputDirectory)) {
                        className = className.replace("/", ".");
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
                                projectModule = (ProjectModule) reflectiveFactory.getConstructor(foundClass).newInstance();
                            } catch (ReflectiveException e) {
                                throw new MixException(0, e);
                            }
                            projectModule.build(builder);
                        }
                    }
                } finally {
                    Thread.currentThread().setContextClassLoader(currentClassLoader);
                }
            }
        }
        configuration = new Configuration(builder.createProject(arguments.getWorkingDirectory(), env.executor, env.part));
    }
    
    public Configuration getConfiguration() {
        return configuration;
    }
}
