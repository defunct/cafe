package com.goodworkalan.cafe.task;

// TODO Document.
public class SelfServer<T> {
    // TODO Document.
    private T self;
    
    // TODO Document.
    public SelfServer() {
    }
    
    // TODO Document.
    public SelfServer(T self) {
        this.self = self;
    }
    
    // TODO Document.
    public void setSelf(T object) {
        this.self = object;
    }
    
    // TODO Document.
    public T getSelf() {
        return self;
    }
}
