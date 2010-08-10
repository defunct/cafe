package com.goodworkalan.mix.cobertura;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.goodworkalan.cafe.CafeError;
import com.goodworkalan.cafe.builder.FindList;
import com.goodworkalan.comfort.io.Files;
import com.goodworkalan.go.go.Argument;
import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.Commandable;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.library.Artifact;
import com.goodworkalan.go.go.library.PathPart;
import com.goodworkalan.go.go.library.PathParts;
import com.goodworkalan.go.go.library.ResolutionPart;
import com.goodworkalan.spawn.Exit;
import com.goodworkalan.spawn.Spawn;

// TODO Document.
@Command(parent = CoberturaCommand.class)
public class ReportCommand implements Commandable {
    /** A file query to match files to copy. */
    private final FindList findList = new FindList();

    /**
     * The name of the file to use for storing the class metadata. The default
     * file is "cobertura.ser" in the current working directory.
     */
    private File dataFile;

    /** The output directory for instrumented classes. */
    private File outputDirectory;
    
    /** The report format, either xml or html. */
    private String format = "html";
    
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
     * Set the report format, either xml or html.
     * 
     * @param format
     *            The report format.
     */
    @Argument
    public void addFormat(String format) {
        this.format = format;
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

    // TODO Document.
    public void execute(Environment env) {
        Artifact cobertura = env.get(Artifact.class, 1);
        List<String> arguments = new ArrayList<String>();
        
        arguments.add("java");
        
        Collection<PathPart> parts = new ArrayList<PathPart>();
        parts.add(new ResolutionPart(cobertura));
        Set<File> classpath = PathParts.fileSet(env.library.resolve(parts));
        
        arguments.add("-classpath");
        arguments.add(Files.path(classpath));

        arguments.add("net.sourceforge.cobertura.reporting.Main");
        
        arguments.add("--destination");
        arguments.add(outputDirectory.toString());
        
        arguments.add("--format");
        arguments.add(format);
        
        if (dataFile != null) {
            arguments.add("--datafile");
            arguments.add(dataFile.toString());
        }
        
        List<String> files = new ArrayList<String>();
        for (FindList.Entry entry : findList) {
            arguments.add("--basedir");
            arguments.add(entry.getDirectory().toString());
            for (String fileName : entry.getFind().find(entry.getDirectory())) {
                files.add(fileName);
            }
        }
        
        arguments.addAll(files);

        System.out.println(arguments);
        ProcessBuilder newProcess = new ProcessBuilder();
        newProcess.command().addAll(arguments);
        
        Exit exit = new Spawn().$(arguments).out(env.io.out).err(env.io.err).run();

        if (exit.code != 0) {
            throw new CafeError(ReportCommand.class, "fork");
        }
    }
}
