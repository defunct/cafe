package com.goodworkalan.mix.cobertura;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.goodworkalan.comfort.io.Files;
import com.goodworkalan.go.go.Argument;
import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.Commandable;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.library.Artifact;
import com.goodworkalan.go.go.library.PathPart;
import com.goodworkalan.go.go.library.PathParts;
import com.goodworkalan.go.go.library.ResolutionPart;
import com.goodworkalan.mix.FindList;
import com.goodworkalan.mix.MixError;
import com.goodworkalan.spawn.Exit;
import com.goodworkalan.spawn.Spawn;

/**
 * Instrument classes for Cobertura coverage testing. 
 *  
 * @author Alan Gutierrez
 */
@Command(parent = CoberturaCommand.class)
public class InstrumentCommand implements Commandable {
    /** A file query to match files to copy. */
    private final FindList findList = new FindList();

    /**
     * The name of the file to use for storing the class metadata. The default
     * file is "cobertura.ser" in the current working directory.
     */
    private File dataFile;

    /** The output directory for instrumented classes. */
    private File outputDirectory;
    
    /** The list regular expression to filter out certain lines. */
    private final List<String> ignores = new ArrayList<String>();

    /**
     * Add a source directory that contains class files.
     * 
     * @param sourceDirectory
     */
    @Argument
    public void addSourceDirectory(File sourceDirectory) {
        findList.addDirectory(sourceDirectory);
    }

    /**
     * Include class files that match the given glob include pattern.
     * 
     * @param include
     *            Class files must match this pattern to be included.
     */
    public void addInclude(String include) {
        findList.addInclude(include);
    }

    /**
     * Exclude class files that match the given glob include pattern.
     * 
     * @param exclude
     *            Class files must match this pattern to be included.
     */
    public void addExclude(String exclude) {
        findList.addExclude(exclude);
    }

    /**
     * Set the output directory for instrumented classes.
     * 
     * @param outputDirectory
     *            The output directory for instrumented classes.
     */
    @Argument
    public void addOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    /**
     * Specify a regular expression to filter out certain lines of your source
     * code. This is useful for ignoring logging statements, for example. You
     * can have as many <ignore/> statements as you want.
     * 
     * @param ignore
     *            A regular expression to filter out certain lines.
     */
    @Argument
    public void addIgnore(String ignore) {
        ignores.add(ignore);
    }

    /**
     * Set the name and location of the cobertura data file.
     * 
     * @param dataFile
     *            The data file.
     */
    @Argument
    public void addDataFile(File dataFile) {
        this.dataFile = dataFile;
    }

    public void execute(Environment env) {
        Artifact cobertura = env.get(Artifact.class, 1);
        List<String> arguments = new ArrayList<String>();
        
        arguments.add("java");
        
        Collection<PathPart> parts = new ArrayList<PathPart>();
        parts.add(new ResolutionPart(cobertura));
        Set<File> classpath = PathParts.fileSet(env.library.resolve(parts));
        
        arguments.add("-classpath");
        arguments.add(Files.path(classpath));

        arguments.add("net.sourceforge.cobertura.instrument.Main");
        
        arguments.add("--destination");
        arguments.add(outputDirectory.toString());
        
        if (dataFile != null) {
            arguments.add("--datafile");
            arguments.add(dataFile.toString());
        }
        
        for (String ignore : ignores) {
            arguments.add("--ignore");
            arguments.add(ignore);
        }
        
        for (FindList.Entry entry : findList) {
            for (String fileName : entry.getFind().find(entry.getDirectory())) {
                arguments.add(new File(entry.getDirectory(), fileName).toString());
            }
        }

        System.out.println(arguments);
        ProcessBuilder newProcess = new ProcessBuilder();
        newProcess.command().addAll(arguments);
        
        Exit exit = new Spawn().$(arguments).out(env.io.out).err(env.io.err).run();

        if (exit.code != 0) {
            throw new MixError(InstrumentCommand.class, "fork");
        }
    }
}

