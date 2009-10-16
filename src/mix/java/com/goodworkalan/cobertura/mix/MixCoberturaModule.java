package com.goodworkalan.mix.cobertura.mix;

import java.net.URI;

import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.mix.BasicJavaModule;

public class MixCoberturaModule extends BasicJavaModule {
    public MixCoberturaModule() {
        super(new Artifact("com.goodworkalan", "mix-cobertura", "0.1"));
        addDependency(new Artifact("com.goodworkalan", "mix", "0.1"));
        addDependency(new Artifact("com.goodworkalan", "glob", "0.1"));
        addDependency(new Artifact("com.goodworkalan", "spawn", "0.1"));
        addTestDependency(new Artifact("org.testng", "testng", "5.10"));
//        addLink(URI.create("http://java.sun.com/j2se/1.5.0/docs/api"));
    }
}
