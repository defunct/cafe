package com.goodworkalan.mix.task;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.goodworkalan.comfort.io.ComfortIOException;
import com.goodworkalan.comfort.io.Files;
import com.goodworkalan.comfort.io.Find;
import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.Library;
import com.goodworkalan.go.go.PathPart;
import com.goodworkalan.go.go.ResolutionPart;
import com.goodworkalan.mix.Dependency;
import com.goodworkalan.mix.FindList;
import com.goodworkalan.mix.MixCommand;
import com.goodworkalan.mix.Project;
import com.goodworkalan.mix.builder.Executable;
import com.goodworkalan.mix.builder.RecipeElement;
import com.goodworkalan.reflective.Method;
import com.goodworkalan.reflective.ReflectiveFactory;
import com.goodworkalan.spawn.Redirect;
import com.goodworkalan.spawn.Spawn;

public class Javadoc extends JavadocOptionsElement<RecipeElement, Javadoc> {
    /** The reflective factory used to create the Javadoc task. */
    private final ReflectiveFactory reflectiveFactory = new ReflectiveFactory();
    
    private boolean fork;
    
    private String visibility;
    
    private File output;
    
    /** The directory where package lists for offline linking are kept. */
    private File packageLists;
    
    private FindList findList = new FindList();
    
    /** Artifacts. */
    private final List<Artifact> artifacts = new ArrayList<Artifact>();
    
    public Javadoc(RecipeElement recipeElement) {
        super(recipeElement, new SelfServer<Javadoc>(), null);
        this.ending = new JavadocEnd() {
            public void end(JavadocConfiguration configuration) {
                configure(configuration);
                parent.addExecutable(new Executable() {
                    public void execute(Environment env, MixCommand.Arguments mix, Project project, String recipeName) {
                        List<String> arguments = new ArrayList<String>();
                        
                        arguments.add("-d");
                        arguments.add(mix.relativize(output).getPath());
                        
                        if (visibility != null) {
                            arguments.add("-" + visibility);
                        }
                        
                        Collection<PathPart> parts = new ArrayList<PathPart>();
                        for (Artifact artifact : artifacts) {
                            parts.add(new ResolutionPart(artifact));
                        }
                        for (Dependency dependency : project.getRecipe(recipeName).getDependencies()) {
                            parts.addAll(dependency.getPathParts(project));
                        }
                        for (FindList.Entry entry : findList) {
                            Find find = entry.getFind();
                            if (!find.hasFilters()) {
                                find.include("**/*.java");
                            }
                            File directory = mix.relativize(entry.getDirectory());
                            for (String fileName : find.find(directory)) {
                                arguments.add(new File(directory, fileName).toString());
                            }
                        }
                        arguments.add("-quiet");
                        for (Map.Entry<URI, File> entry : offlineLinks.entrySet()) {
                            arguments.add("-linkoffline");
                            arguments.add(entry.getKey().toASCIIString());
                            File packages = entry.getValue();
                            if (!packages.isAbsolute()) {
                                packages = new File(mix.getWorkingDirectory(), packages.getPath());
                            }
                            arguments.add(packages.getAbsoluteFile().toURI().toString());
                        }
                        if (packageLists != null) {
                            File workingPackageLists = mix.relativize(packageLists);
                            if (workingPackageLists.isDirectory()) {
                                for (File file : workingPackageLists.listFiles()) {
                                    if (file.isDirectory() && file.canRead()) {
                                        try {
                                            for (String line : Files.slurp(new File(file, "url"))) {
                                                line = line.trim();
                                                if (line.length() != 0) {
                                                    arguments.add("-linkoffline");
                                                    arguments.add(line);
                                                    arguments.add(file.getAbsoluteFile().toURI().toString());
                                                    break;
                                                }
                                            }
                                        } catch (ComfortIOException e) {
                                            // If we can't read the URL file, then it doesn't get linked.
                                        }
                                    }
                                }
                            }
                        }
//                        if (!mixArguments.isOffline()) {
//                            for (URI link : links) {
//                                arguments.add("-link");
//                                arguments.add(link.toASCIIString());
//                            }
//                        }
                        Set<File> classpath = new LinkedHashSet<File>();
                        Library library = env.part.getCommandInterpreter().getLibrary();
                        classpath.addAll(library.resolve(parts).getFiles());
                        if (!classpath.isEmpty()) {
                            arguments.add("-classpath");
                            arguments.add(Files.path(classpath));
                        }
                        Class<?> compilerClass = null;
                        try {
                            compilerClass = Class.forName("com.sun.tools.javadoc.Main");
                        } catch (ClassNotFoundException e) {
                        }
                        if (fork || compilerClass == null) {
                            arguments.add(0, "javadoc");
                            
                            ProcessBuilder newProcess = new ProcessBuilder();
                            newProcess.command().addAll(arguments);
                            
                            Spawn<Redirect, Redirect> spawn;
                            spawn = Spawn.spawn(new Redirect(env.io.out), new Redirect(env.io.err));
                            
                            spawn.getProcessBuilder().command().addAll(arguments);
                            spawn.execute();
                        } else {
                            try {
                                Method method = reflectiveFactory.getMethod(compilerClass, "execute", new String [0].getClass());
                                method.invoke(null, new Object[] { arguments.toArray(new String[arguments.size()]) });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                
            }
        };
    }

    public Javadoc configure(JavadocConfiguration...configurations) {
        return configure(Arrays.asList(configurations));
    }
    
    public Javadoc configure(List<JavadocConfiguration> configurations){
        for (JavadocConfiguration configuration : configurations) {
            configuration.configure(this);
        }
        return this;
    }

    public Javadoc artifact(Artifact artifact) {
        artifacts.add(artifact);
        return this;
    }

    public Javadoc link(URI uri) {
        links.add(uri);
        return this;
    }

    public Javadoc offlineLink(URI uri, File file) {
        offlineLinks.put(uri, file);
        return this;
    }
    
    public FindElement<Javadoc> source(File directory) {
        return new FindElement<Javadoc>(this, findList, directory);
    }

    public Javadoc output(File directory) {
        this.output = directory;
        return this;
    }

    /**
     * Set the directory where package lists for offline linking are stored. The
     * directory will contain a directory for each library that will be linked
     * offline. In each library directory will be the package list and a file
     * named <code>url</code> that contains the URL where the documentation is
     * hosted.
     * 
     * @param directory
     *            The package lists.
     * @return This Javadoc object.
     */
    public Javadoc packageLists(File directory) {
        this.packageLists = directory;
        return this;
    }
}
