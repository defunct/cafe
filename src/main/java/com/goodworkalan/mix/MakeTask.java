package com.goodworkalan.mix;

import java.util.List;

import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.CommandPart;
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
        CommandPart mix = env.commandPart.getParent();
        String recipe = env.commandPart.getRemaining().get(0);
        for (Dependency dependency : configuration.getProject().getDependencies(recipe)) {
            dependency.make(env.executor, mix);
        }
        for (List<String> step : configuration.getProject().getCommands(recipe)) {
            CommandPart next = mix.extend(step);
            if (String.class.equals(next.getArgumentTypes().get("recipe"))) {
                next = next.argument("recipe", recipe);
            }
            env.executor.execute(next);
        }
    }
}
