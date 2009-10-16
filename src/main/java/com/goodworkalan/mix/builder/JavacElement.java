package com.goodworkalan.mix.builder;

public class JavacElement<P> extends SourceElement<JavacElement<P>> {
    private final P parent;

    public JavacElement(P parent) {
        this.parent = parent;
    }

    public JavacElement<P> debug(boolean debug) {
        return getSelf();
    }
    
    public P end() {
        return parent;
    }
    
    public JavacElement<P> getSelf() {
        return this;
    }
}
