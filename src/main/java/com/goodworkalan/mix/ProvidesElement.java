package com.goodworkalan.mix;

public class ProvidesElement {
    private final Builder builder;

    public ProvidesElement(Builder builder) {
        this.builder = builder;
    }
    public ProvidesElement artifact(String suffix, String extension) {
        return this;
    }
    
    public Builder end() {
        return builder;
    }
}
