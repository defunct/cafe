package com.goodworkalan.mix.cobertura;

import com.goodworkalan.go.go.Arguable;
import com.goodworkalan.go.go.Argument;
import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.Commandable;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.mix.MixCommand;

@Command(parent = MixCommand.class)
public class CoberturaCommand implements Commandable {
    public final static class Arguments implements Arguable {
        private Artifact cobertura = new Artifact("net.sourceforge.cobertura/cobertura/1.9.2");
        
        public Arguments() {
        }
        
        @Argument
        public void addCobertura(Artifact cobertura) {
            this.cobertura = cobertura;
        }
        
        public Artifact getCobertura() {
            return cobertura;
        }
    }
    
    public void execute(Environment env) {
    }
}
