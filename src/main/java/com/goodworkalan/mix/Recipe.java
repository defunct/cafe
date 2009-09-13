package com.goodworkalan.mix;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.goodworkalan.go.go.PathPart;

public class Recipe {
    private final Map<String, Command> commands;
    
    private final Map<String, Dependency> dependencies;
    
    private final Collection<PathPart> produce;
    
    Recipe(Map<String, Command> commands, Map<String, Dependency> dependencies, Collection<PathPart> produce) {
        this.commands = commands;
        this.dependencies = dependencies;
        this.produce = produce;
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
    
    public Collection<PathPart> getProduce() {
        return produce;
    }
}
