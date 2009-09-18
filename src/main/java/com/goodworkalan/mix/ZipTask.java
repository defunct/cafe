package com.goodworkalan.mix;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.goodworkalan.go.go.Argument;
import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.Task;

@Command(parent = MixTask.class)
public class ZipTask extends Task {
    /** The files to add to the zip file. */
    private final FindList findList = new FindList();
    
    /** The compression level. */
    private int level = 0;

    /** The output file name. */
    private File outputFile;
    
    /**
     * Set the output file name.
     * @param outputFile The output file name.
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
     * Apply the exclude criteria to the last directory added to this task.
     * 
     * @param exclude
     *            The exclude pattern.
     */
    @Argument
    public void addInclude(String include) {
        findList.addInclude(include);
    }

    /**
     * Apply the include criteria to the last directory added to this task.
     * 
     * @param include
     *            The include pattern.
     */
    @Argument
    public void addExclude(String exclude) {
        findList.addExclude(exclude);
    }
    
    @Override
    public void execute(Environment env) {
        byte[] buffer = new byte[4098];
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outputFile));
            out.setLevel(level);
            for (FindList.Entry entry : findList) {
                for (String fileName : entry.getFileNames()) {
                    File source = new File(entry.getDirectory(), fileName);
                    if (source.isDirectory()) {
                        ZipEntry zipEntry = new ZipEntry(fileName + "/");
                        out.putNextEntry(zipEntry);
                        out.closeEntry();
                    } else {
                        FileInputStream in = new FileInputStream(source);
                        ZipEntry zipEntry = new ZipEntry(fileName);
                        out.putNextEntry(zipEntry);
                        int read;
                        while ((read = in.read(buffer)) != -1) {
                            out.write(buffer, 0, read);
                        }
                        out.closeEntry();
                        in.close();
                    }
                }
            }
            out.close();
        } catch (IOException e) {
            throw new MixException(0, e);
        }
    }
}
