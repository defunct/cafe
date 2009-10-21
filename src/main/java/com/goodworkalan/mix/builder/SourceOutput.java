package com.goodworkalan.mix.builder;

import java.io.File;

import com.goodworkalan.mix.FindList;

public class SourceOutput {
    private File output;
    
    private FindList source = new FindList();
    
    public SourceOutput() {
    }
    
    public void setOutput(File output) {
        this.output = output;
    }
    
    public File getOutput() {
        return output;
    }
    
    public FindList getSource() {
        return source;
    }
}
