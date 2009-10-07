package com.goodworkalan.mix;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.goodworkalan.go.go.Argument;
import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.Task;

@Command(parent = MixTask.class)
public class ZipTask extends Task {
    private final byte[] buffer = new byte[4098];

    private ZipOutputStream out;
    
    /** Set of files added to the zip file. */
    private final Set<String> seen = new HashSet<String>();

    /** The files to add to the zip file. */
    private final FindList findList = new FindList();
    
    /** The compression level. */
    private int level = 0;

    /** The output file name. */
    private File outputFile;

    /**
     * Set the output file name.
     * 
     * @param outputFile
     *            The output file name.
     */
    @Argument
    public void addOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    /**
     * Set the compression level.
     * 
     * @param level
     *            The compression level.
     */
    @Argument
    public void addLevel(int level) {
        this.level = level;
    }

    /**
     * Add a source directory.
     * 
     * @param sourceDirectory
     */
    @Argument
    public void addSourceDirectory(File sourceDirectory) {
        findList.addDirectory(sourceDirectory);
    }

    /**
     * Apply the include criteria to the last directory added to this task.
     * 
     * @param include
     *            The include pattern.
     */
    @Argument
    public void addInclude(String include) {
        findList.addInclude(include);
    }

    /**
     * Apply the exclude criteria to the last directory added to this task.
     * 
     * @param exclude
     *            The exclude pattern.
     */
    @Argument
    public void addExclude(String exclude) {
        findList.addExclude(exclude);
    }
    
    protected void addDirectory(String entryName) throws IOException {
        if (!entryName.endsWith("/")) {
            entryName = entryName + "/";
        }
        if (seen.contains(entryName)) {
            throw new MixException(0);
        }
        ZipEntry zipEntry = new ZipEntry(entryName);
        out.putNextEntry(zipEntry);
        out.closeEntry();
    }
    
    protected void addFile(File source, String entryName) throws IOException {
        if (seen.contains(entryName)) {
            throw new MixException(0);
        }
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
    
    protected void addAdditionalEntries(Environment env) throws IOException {
    }

    @Override
    public void execute(Environment env) {
        try {
            out = new ZipOutputStream(new FileOutputStream(outputFile));
            out.setLevel(level);
            for (FindList.Entry entry : findList) {
                for (String fileName : entry.getFind().find(entry.getDirectory())) {
                    File source = new File(entry.getDirectory(), fileName);
                    if (source.isDirectory()) {
                        addDirectory(fileName);
                    } else {
                        addFile(source, fileName);
                    }
                }
            }
            addAdditionalEntries(env);
            out.close();
        } catch (IOException e) {
            throw new MixException(0, e);
        }
    }
}
