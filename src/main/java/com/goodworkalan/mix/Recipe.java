package com.goodworkalan.mix;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.goodworkalan.go.go.PathPart;

public class Recipe {
    private final List<Command> commands;
    
    private final Map<List<String>, Dependency> dependencies;
    
    private final Collection<PathPart> produce;
    
    Recipe(List<Command> commands, Map<List<String>, Dependency> dependencies, Collection<PathPart> produce) {
        this.commands = commands;
        this.dependencies = dependencies;
        this.produce = produce;
    }
    
    public List<List<String>> getCommands() {
        List<List<String>> commands = new ArrayList<List<String>>();
        for(Command command : this.commands) {
            List<String> line = new ArrayList<String>();
            line.add(command.name);
            line.addAll(command.arguments);
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
