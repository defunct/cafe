package com.goodworkalan.mix;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.goodworkalan.comfort.io.Files;
import com.goodworkalan.go.go.Argument;
import com.goodworkalan.go.go.ArgumentList;
import com.goodworkalan.go.go.Commandable;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.library.Artifacts;
import com.goodworkalan.go.go.library.DirectoryPart;
import com.goodworkalan.go.go.library.Include;
import com.goodworkalan.go.go.library.ResolutionPart;
import com.goodworkalan.ilk.Ilk;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.task.Copy;
import com.goodworkalan.mix.task.FindList;
import com.goodworkalan.mix.task.Javac;

/**
 * Root command for Mix.
 * 
 * @author Alan Gutierrez
 */
public class MixCommand implements Commandable {
    /**
     * Create a mix task.
     */
    public MixCommand() {
    }

    /**
     * Set whether or not Mix is being run without an Internet connection.
     * 
     * @param offline
     *            If true, Mix is being run without an Internet connection.
     */
    @Argument
    public boolean offline;

    @Argument
    public boolean siblings;
    
    @Argument
    public boolean bootstrap;
    
    @Argument
    public String projectModule;

    /**
     * The project root directory, defaults to the current working
     * directory.
     */
    @Argument
    public File workingDirectory = new File(".");

    /**
     * Rebuild the mix build configuration classes if any of them are dirty,
     * then set the project command to run next as a hidden command.
     * 
     * @param env
     *            The environment.
     */
    public void execute(Environment env) {
        Mix mix;
        try {
            mix = new Mix(workingDirectory.getCanonicalFile(), offline, siblings);
        } catch (IOException e) {
            throw new MixError(MixCommand.class, "working.directory", workingDirectory);
        }
        env.output(new Ilk<Set<List<String>>>(){}, new HashSet<List<String>>());
        env.output(Mix.class, mix);
        env.debug("start");
        // Need to run the compiler out of the context (one more reason
        // why compilers are not pluggable) of the compiler command.
        File output = new File(mix.getWorkingDirectory(), "target/mix-classes");
        if (bootstrap) {
            if (output.exists()) {
                Files.unlink(output);
            }
            if (!output.mkdirs()) {
                throw new MixError(MixCommand.class, "output.mkdirs", output);
            }
            File sourceDirectory = new File(mix.getWorkingDirectory(), "src/mix/java");
            env.debug("javac", sourceDirectory, output);
            if (sourceDirectory.isDirectory()) {
                Builder hidden = new Builder();
                hidden.recipe("production").end();
                File dependencies = new File(mix.getWorkingDirectory(), "src/mix/etc/project.dep");
                if (dependencies.exists()) {
                	for (Include include : Artifacts.read(dependencies)) {
                		hidden
                			.recipe("production")
                				.depends()
                					.include(include)
                					.end()
                				.end()
                			.end();
                	}
                }
                hidden
                    .recipe("javac")
                    	.depends()
                    		.recipe("production")
                    		.end()
                        .task(Javac.class)
                            .artifact("com.github.bigeasy.mix/mix/0.+1.3.5")
                            .source(sourceDirectory.getAbsoluteFile()).end()
                            .output(output.getAbsoluteFile())
                            .end()
                        .task(Copy.class)
                        	.source(new File(mix.getWorkingDirectory(), "src/mix/resources")).exclude("**/.svn/**").end()
                        	.output(output.getAbsoluteFile())
                        	.end()
                        .end();
                env.output(Project.class, hidden.createProject(mix.getWorkingDirectory()));
            }
        } else if (projectModule != null) {
            env.invokeAfter(ProjectCommand.class);
        } else {
            FindList sources = new FindList();
            sources.addDirectory(new File(mix.getWorkingDirectory(), "src/mix/java"));
            sources.filesOnly();
            sources.addDirectory(new File(mix.getWorkingDirectory(), "src/mix/resources"));
            sources.filesOnly();
            FindList outputs = new FindList();
            outputs.addDirectory(output);
            outputs.filesOnly();
            if (new Rebuild(sources, outputs).isDirty(mix)) {
                ArgumentList mixArguments = new ArgumentList(env.arguments.get(0));
                mixArguments.removeArgument("mix:siblings");
                mixArguments.addArgument("mix:bootstrap", "true");
                env.executor.run(env.io, "mix", mixArguments, "make", "javac");
            }
            if (!output.isDirectory()) {
                throw new MixException(MixCommand.class, "output.not.directory", output);
            }
            File dependencies = new File(mix.getWorkingDirectory(), "src/mix/etc/project.dep");
            if (dependencies.exists()) {
            	for (Include include : Artifacts.read(dependencies)) {
                	env.extendClassPath(new ResolutionPart(include));
            	}
            }
            env.extendClassPath(new DirectoryPart(output.getAbsoluteFile()));
            env.invokeAfter(ProjectCommand.class);
        }
    }
}
