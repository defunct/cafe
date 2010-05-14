package com.goodworkalan.mix.task;

// FIXME Do you want to make your code this much bigger?
public class End {
    private boolean ended;
    
    public void amend() {
        if (ended) {
            throw new IllegalStateException();
        }
    }
    
    public void end() {
        ended = true;
    }
}
