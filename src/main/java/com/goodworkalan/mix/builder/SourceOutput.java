package com.goodworkalan.mix.builder;

import java.io.File;

// TODO Document.
public class SourceOutput {
    // TODO Document.
    private File output;
    
    // TODO Document.
    private FindList source = new FindList();
    
    // TODO Document.
    public SourceOutput() {
    }
    
    // TODO Document.
    public void setOutput(File output) {
        this.output = output;
    }
    
    // TODO Document.
    public File getOutput() {
        return output;
    }
    
    // TODO Document.
    public FindList getSource() {
        return source;
    }
}
