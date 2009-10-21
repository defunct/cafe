package com.goodworkalan.mix.task;

public class SelfServer<T> {
    private T self;
    
    public SelfServer() {
    }
    
    public SelfServer(T self) {
        this.self = self;
    }
    
    public void setSelf(T object) {
        this.self = object;
    }
    
    public T getSelf() {
        return self;
    }
}
