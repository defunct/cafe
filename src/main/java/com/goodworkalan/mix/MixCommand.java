package com.goodworkalan.mix;

import java.io.File;
import java.io.IOException;

import com.goodworkalan.comfort.io.Files;
import com.goodworkalan.go.go.Argument;
import com.goodworkalan.go.go.Commandable;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.library.DirectoryPart;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.builder.Executable;
import com.goodworkalan.mix.builder.Rebuild;
import com.goodworkalan.mix.task.Javac;
import com.goodworkalan.reflective.ReflectiveFactory;

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
        env.output(mix);
        env.debug("start");
        // Need to run the compiler out of the context (one more reason
        // why compilers are not pluggable) of the compiler command.
        File output = new File(mix.getWorkingDirectory(), "target/mix-classes");
        FindList sources = new FindList();
        sources.addDirectory(new File(mix.getWorkingDirectory(), "src/mix/java"));
        sources.isFile();
        sources.addDirectory(new File(mix.getWorkingDirectory(), "src/mix/resources"));
        sources.isFile();
        FindList outputs = new FindList();
        outputs.addDirectory(output);
        outputs.isFile();
        if (new Rebuild(sources, outputs).isDirty(mix)) {
            if (output.exists()) {
                Files.delete(output);
            }
            if (!output.mkdirs()) {
                throw new MixError(MixCommand.class, "output.mkdirs", output);
            }
            File sourceDirectory = new File(mix.getWorkingDirectory(), "src/mix/java");
            env.debug("javac", sourceDirectory, output);
            if (sourceDirectory.isDirectory()) {
                Builder hiddenBuilder = new Builder();
                hiddenBuilder
                    .recipe("javac")
                        .task(Javac.class)
                            .artifact("com.github.bigeasy.mix/mix/0.1.3")
                            .source(sourceDirectory.getAbsoluteFile()).end()
                            .output(output.getAbsoluteFile())
                            .end()
                         .end();
                Project project = hiddenBuilder.createProject(mix.getWorkingDirectory());
                for (Executable executable : project.getRecipe("javac").getProgram()) {
                    executable.execute(env, mix, project, "javac");
                }
            }
            // FIXME Do resources too.
        } else if (!output.isDirectory()) {
            throw new MixException(MixCommand.class, "output.not.directory", output);
        }
        env.invokeAfter(new ProjectCommand(new ReflectiveFactory(), mix.getWorkingDirectory(), output));
        env.extendClassPath(new DirectoryPart(output.getAbsoluteFile()));
    }
}
