package com.goodworkalan.mix.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.goodworkalan.comfort.io.Find;
import com.goodworkalan.go.go.Commandable;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.mix.Make;
import com.goodworkalan.mix.Mix;
import com.goodworkalan.mix.MixError;
import com.goodworkalan.mix.Project;
import com.goodworkalan.mix.builder.FindStatement;
import com.goodworkalan.mix.builder.FindList;
import com.goodworkalan.mix.builder.RecipeStatement;

public class Zip {
    private final RecipeStatement recipeElement;

    private final byte[] buffer = new byte[4098];

    private ZipOutputStream out;
    
    /** Set of files added to the zip file. */
    private final Set<String> seen = new HashSet<String>();

    /** The files to add to the zip file. */
    private final FindList findList = new FindList();
    
    /** The compression level. */
    private int level = 0;

    /** The output file name. */
    private File output;

    public Zip(RecipeStatement recipeElement) {
        this.recipeElement = recipeElement;
    }

    /**
     * Set the output file name.
     * 
     * @param output
     *            The output file name.
     */
    public Zip output(File output) {
        this.output = output;
        return this;
    }

    /**
     * Set the compression level.
     * 
     * @param level
     *            The compression level.
     */
    public Zip level(int level) {
        this.level = level;
        return this;
    }

    /**
     * Add a source directory.
     * 
     * @param directory The source directory.
     */
    public FindStatement<Zip> source(File directory) {
        return new FindStatement<Zip>(this, findList, directory);
    }

    protected void addFile(File source, String entryName) throws IOException {
        entryName = entryName.replace(File.separator, "/");
        if (seen.contains(entryName)) {
            throw new MixError(Zip.class, "seen", entryName, output);
        }
        seen.add(entryName);
        byte[] buffer = this.buffer;
        FileInputStream in = new FileInputStream(source);
        ZipEntry zipEntry = new ZipEntry(entryName);
        out.putNextEntry(zipEntry);
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        out.closeEntry();
        in.close();
    }
    
    protected void addAdditionalEntries(Environment env, Project project, String recipeName) throws IOException {
    }

    protected void addFind(Find find, File directory) throws IOException {
        addFind(find, directory, "");
    }

    protected void addFind(Find find, File directory, String prefix) throws IOException {
        assert prefix != null;
        if (prefix.length() > 0 && !prefix.endsWith("/")) {
            prefix = prefix + "/";
        }
        for (String fileName : find.find(directory)) {
            File source = new File(directory, fileName);
            if (!source.isDirectory()) {
                addFile(source, prefix + fileName);
            }
        }
    }
    
    public RecipeStatement end() {
        return recipeElement.executable(new Commandable() {
            public void execute(Environment env) {
                Mix mix = env.get(Mix.class, 0);
                env.verbose(Zip.class, "start", output);
                File absoluteOutput = mix.relativize(output);
                try {
                    out = new ZipOutputStream(new FileOutputStream(absoluteOutput));
                    out.setLevel(level);
                    for (FindList.Entry entry : findList) {
                        addFind(entry.getFind(), mix.relativize(entry.getDirectory()));
                    }
                    Project project = env.get(Project.class, 0);
                    Make make = env.get(Make.class, 1);
                    addAdditionalEntries(env, project, make.recipeName);
                    out.close();
                } catch (IOException e) {
                    throw new MixError(Zip.class, "failure", e, absoluteOutput);
                }
            }
        });
    }
}
