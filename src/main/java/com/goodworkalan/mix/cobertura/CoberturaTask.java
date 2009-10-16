package com.goodworkalan.mix.cobertura;

import com.goodworkalan.go.go.Arguable;
import com.goodworkalan.go.go.Argument;
import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.Task;
import com.goodworkalan.mix.MixTask;

@Command(parent = MixTask.class)
public class CoberturaTask extends Task {
    public final static class Arguments implements Arguable {
        private Artifact cobertura = new Artifact("cobertura", "cobertura", "1.8");
        
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
}
