package com.goodworkalan.mix.builder;

import com.goodworkalan.mix.Builder;

public class BasicJavaProject extends SourceElement<BasicJavaProject> {
    private final Builder builder;

    public BasicJavaProject(Builder builder) {
        this.builder = builder;
    }
    
    public MainElement main() {
        return new MainElement(this);
    }
    
    public Builder end() {
        return builder;
    }
    
    public BasicJavaProject getSelf() {
        return this;
    }
}
