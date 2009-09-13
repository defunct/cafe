package com.goodworkalan.mix;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Recipe {
    private final Map<String, Command> commands;
    
    private final Map<String, Dependency> dependencies;
    
    Recipe(Map<String, Command> commands, Map<String, Dependency> dependencies) {
        this.commands = commands;
        this.dependencies = dependencies;
    }
    
    public List<List<String>> getCommands() {
        List<List<String>> commands = new ArrayList<List<String>>();
        for(Map.Entry<String, Command> entry : this.commands.entrySet()) {
            List<String> line = new ArrayList<String>();
            line.add(entry.getValue().name);
            line.addAll(entry.getValue().arguments);
            commands.add(line);
        }
        return commands;
    }
    
    public List<Dependency> getDependencies() {
        return new ArrayList<Dependency>(dependencies.values());
    }
}
