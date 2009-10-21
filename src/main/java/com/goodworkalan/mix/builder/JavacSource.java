package com.goodworkalan.mix.builder;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.goodworkalan.mix.Dependency;
import com.goodworkalan.mix.FindList;
import com.goodworkalan.mix.task.JavacConfiguration;


public class JavacSource {
    private File output;
    
    private FindList source = new FindList();
    
    private FindList resources = new FindList();
    
    private final Map<List<String>, Dependency> dependencies = new LinkedHashMap<List<String>, Dependency>();
    
    private final List<JavacConfiguration> javacConfigurations = new ArrayList<JavacConfiguration>();
    
    public JavacSource() {
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
    
    public FindList getResources() {
        return resources;
    }
    
    public Map<List<String>, Dependency> getDependencies() {
        return dependencies;
    }
    
    public List<JavacConfiguration> getJavacConfigurations() {
        return javacConfigurations;
    }
}
