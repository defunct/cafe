package com.goodworkalan.mix;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

import com.goodworkalan.comfort.io.Files;
import com.goodworkalan.comfort.io.Find;
import com.goodworkalan.go.go.Arguable;
import com.goodworkalan.go.go.Argument;
import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.go.go.Commandable;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.Output;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.builder.Executable;
import com.goodworkalan.mix.builder.Rebuild;
import com.goodworkalan.mix.task.Javac;
import com.goodworkalan.reflective.ReflectiveException;
import com.goodworkalan.reflective.ReflectiveFactory;

/**
 * Root command for Mix.
 * 
 * @author Alan Gutierrez
 */
public class MixCommand implements Commandable {
    /** Use to create project modules. */
    private final ReflectiveFactory reflectiveFactory;
    
    /** If true, attempt to load a project. */
    private boolean project = true;
    
    /**
     * Create a mix task.
     */
    public MixCommand() {
        this(new ReflectiveFactory());
    }

    /**
     * Create a mix task with the given reflective factory for testing. The
     * given reflective factory can simulate reflection exceptions.
     * 
     * @param reflectiveFactory
     *            The reflective factory.
     */
    MixCommand(ReflectiveFactory reflectiveFactory) {
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
        // FIXME Outgoing.
        private File workingDirectory = new File(".");
        
        /** Whether or not Mix is being run without an Internet connection. */
        private boolean offline;
        
        /** Whether or not we use siblings when we can instead of artifacts. */
        private boolean siblings;

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

        /**
         * Set whether or not Mix is being run without an Internet connection.
         * 
         * @param offline
         *            If true, Mix is being run without an Internet connection.
         */
        @Argument
        public void addOffline(boolean offline) {
            this.offline = offline;
        }

        /**
         * Get whether or not Mix is being run without an Internet connection.
         * 
         * @return True Mix is being run without an Internet connection.
         */
        public boolean isOffline() {
            return offline;
        }
        
        @Argument
        public void addSiblings(boolean siblings) {
            this.siblings = siblings;
        }
        
        public boolean isSiblings() {
            return siblings;
        }
    }
    
    /**
     * The output from running the mix task.
     */
    public final static class Configuration implements Output {
        /** The project defintion. */
        private final Project project;

        /**
         * Create a new configuration.
         * 
         * @param project
         *            The project definition.
         */
        public Configuration(Project project) {
            this.project = project;
        }

        /**
         * Get the project definition.
         * 
         * @return The project definition.
         */
        public Project getProject() {
            return project;
        }
    }
    
    /**
     * The configuration class produced by executing the mix command.
     */
    private Configuration configuration;
    
    /**
     * The mix comamnd arguments.
     */
    private Arguments arguments = new Arguments();

    /**
     * Set the mix command arguments.
     * 
     * @param arguments
     *            The mix command arguments.
     */
    public void setArguments(Arguments arguments) {
        this.arguments = arguments;
    }
    
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
            String sourceDirectoryName = properties.getProperty("source", "src/mix/java");
            if (new File(sourceDirectoryName).isAbsolute()) {
                throw new MixException(0);
            }
            FindList sources = new FindList();
            sources.addDirectory(new File(sourceDirectoryName));
            sources.isFile();
            String resourceDirectoryName = properties.getProperty("resources", "src/mix/resources");
            if (new File(sourceDirectoryName).isAbsolute()) {
                throw new MixException(0);
            }
            sources.addDirectory(new File(resourceDirectoryName));
            sources.isFile();
            FindList outputs = new FindList();
            outputs.addDirectory(outputDirectory);
            outputs.isFile();
            if (new Rebuild(sources, outputs).isDirty()) {
                System.out.println("Building mix.");
                if (outputDirectory.exists()) {
                    Files.delete(outputDirectory);
                }
                if (!outputDirectory.mkdirs()) {
                    throw new MixException(0);
                }
                // FIXME Do a dependency check instead.
                File sourceDirectory = new File(arguments.getWorkingDirectory(), sourceDirectoryName);
                if (sourceDirectory.isDirectory()) {
                    Builder hiddenBuilder = new Builder();
                    hiddenBuilder
                        .recipe("javac")
                            .task(Javac.class)
                                .artifact(new Artifact("com.goodworkalan/mix/0.1"))
                                .source(sourceDirectory).end()
                                .output(outputDirectory)
                                .end()
                             .end();
                    Project project = hiddenBuilder.createProject(arguments.getWorkingDirectory(), env.executor, env.part);
                    for (Executable executable : project.getRecipe("javac").getProgram()) {
                        executable.execute(env, project, "javac");
                    }
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
                        classLoader = new URLClassLoader(new URL[] { outputDirectory.getAbsoluteFile().toURI().toURL() }, currentClassLoader);
                    } catch (MalformedURLException e) {
                        throw new MixException(0, e);
                    }
                    Thread.currentThread().setContextClassLoader(classLoader);
                    for (String className : new Find().include("**/*.class").find(outputDirectory)) {
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
