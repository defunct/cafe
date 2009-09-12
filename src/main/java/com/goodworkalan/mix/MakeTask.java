package com.goodworkalan.mix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.Task;
import com.goodworkalan.go.go.TaskInfo;

@Command(parent = MixTask.class)
public class MakeTask extends Task {
    private MixTask.Configuration configuration;
    
    public void setConfiguration(MixTask.Configuration configuration) {
        this.configuration = configuration;
    }
    
    @Override
    public void execute(Environment environment) {
        List<String> mixCommand = new ArrayList<String>();
        mixCommand.add("mix");
        mixCommand.addAll(Arrays.asList(environment.arguments[0]));
        String recipe = environment.remaining[0];
        for (List<String> step : configuration.getProject().getCommands(recipe)) {
            List<String> command = new ArrayList<String>();
            command.addAll(mixCommand);
            command.addAll(step);
            TaskInfo taskInfo = environment.commandInterpreter.getTaskInfo(command);
            if (String.class.equals(taskInfo.getArguments().get("recipe"))) {
                command.add("--recipe=" + recipe);
            }
            environment.commandInterpreter.execute(command);
        }
    }
}
