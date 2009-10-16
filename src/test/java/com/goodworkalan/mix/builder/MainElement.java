package com.goodworkalan.mix.builder;

public class MainElement {
    private BasicJavaProject project;
    
    public MainElement(BasicJavaProject project) {
        this.project = project;
    }

    public JavacElement<MainElement> javac() {
        return new JavacElement<MainElement>(this);
    }
    
    public BasicJavaProject end() {
        return project;
    }
}
