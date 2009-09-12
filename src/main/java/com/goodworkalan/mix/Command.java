package com.goodworkalan.mix;

import java.util.ArrayList;
import java.util.List;

class Command {
    public final String name;
    
    public final List<String> arguments = new ArrayList<String>();
    
    public Command(String name) {
        this.name = name;
    }
}
