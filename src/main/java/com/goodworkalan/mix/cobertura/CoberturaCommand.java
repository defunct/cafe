package com.goodworkalan.mix.cobertura;

import com.goodworkalan.go.go.Argument;
import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.Commandable;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.library.Artifact;
import com.goodworkalan.mix.MixCommand;

@Command(parent = MixCommand.class)
public class CoberturaCommand implements Commandable {
    @Argument
    public Artifact cobertura = new Artifact("net.sourceforge.cobertura/cobertura/1.9.4.1");
    
    public void execute(Environment env) {
        env.output(cobertura);
    }
}
