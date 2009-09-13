package com.goodworkalan.mix;

import java.util.List;

import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.Task;

@Command(parent = MixTask.class)
public class MakeTask extends Task {
    private MixTask.Configuration configuration;
    
    public void setConfiguration(MixTask.Configuration configuration) {
        this.configuration = configuration;
    }
    
    @Override
    public void execute(Environment env) {
        List<String> remaining = env.commandPart.getRemaining();
        if (remaining.isEmpty()) {
            throw new MixException(0);
        }
        configuration.getProject().getRecipe(remaining.get(0));
    }
}
