package com.goodworkalan.mix.builder;

public abstract class SourceElement<T> implements SelfElement<T> {
    public T source(String release) {
        return getSelf();
    }
}
