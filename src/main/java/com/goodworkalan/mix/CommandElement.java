package com.goodworkalan.mix;


public class CommandElement<T> {
    private final T parent;

    private final Command command;

    public CommandElement(T parent, Command command) {
        this.parent = parent;
        this.command = command;
    }
    
    public CommandElement<T> argument(String name, String value) {
        command.arguments.add("--" + name + "=" + value);
        return this;
    }
    
    public T end() {
        return parent;
    }
}
