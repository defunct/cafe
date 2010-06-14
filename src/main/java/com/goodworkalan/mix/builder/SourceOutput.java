package com.goodworkalan.mix.builder;

import java.io.File;


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
