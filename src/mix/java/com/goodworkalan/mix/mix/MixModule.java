package com.goodworkalan.mix.mix;

import java.net.URI;

import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.mix.BasicJavaModule;

public class MixModule extends BasicJavaModule {
    public MixModule() {
        super(new Artifact("com.goodworkalan", "mix", "0.1"));
        addDependency(new Artifact("com.goodworkalan", "spawn", "0.1"));
        addDependency(new Artifact("com.goodworkalan", "go-go", "0.1"));
        addDependency(new Artifact("com.goodworkalan", "glob", "0.1-SNAPSHOT"));
        addTestDependency(new Artifact("org.testng", "testng", "5.10"));
        addLink(URI.create("http://java.sun.com/j2se/1.5.0/docs/api"));
    }
}
