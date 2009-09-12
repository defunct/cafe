package com.goodworkalan.mix;

import com.goodworkalan.glob.Files;
import com.goodworkalan.go.go.Argument;
import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.Task;

@Command(parent = MixTask.class)
public class Clean extends Task {
    private MixTask.Configuration configuration;
    
    @Argument
    public void setConfiguration(MixTask.Configuration configuration) {
        this.configuration = configuration;
    }
    
    @Override
    public void execute(Environment environment) {
        Files.delete(configuration.getProject().getOutputDirectory());
    }
}
