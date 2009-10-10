package com.goodworkalan.mix.eclipse.mix;

import java.net.URI;

import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.mix.BasicJavaModule;

public class MixEclipseModule extends BasicJavaModule {
    public MixEclipseModule() {
        super(new Artifact("com.goodworkalan", "mix-eclipse", "0.1"));
        addDependency(new Artifact("org.freemarker", "freemarker", "2.3.13"));
        addDependency(new Artifact("com.goodworkalan", "mix", "0.1"));
//        addTestDependency(new Artifact("org.testng", "testng", "5.10"));
        addLink(URI.create("http://java.sun.com/j2se/1.5.0/docs/api"));
    }
}
