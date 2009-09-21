package com.goodworkalan.mix.cobertura;

import com.goodworkalan.go.go.Arguable;
import com.goodworkalan.go.go.Argument;
import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.go.go.Task;

public class CoberturaTask extends Task {
    public final static class Arguments implements Arguable {
        private Artifact cobertura = new Artifact("cobertura", "cobertura", "1.9rc1");
        
        public Arguments(Artifact cobertura) {
            this.cobertura = cobertura;
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
