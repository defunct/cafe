package com.goodworkalan.mix.task;

// FIXME Do you want to make your code this much bigger?
public class End {
    // TODO Document.
    private boolean ended;
    
    // TODO Document.
    public void amend() {
        if (ended) {
            throw new IllegalStateException();
        }
    }
    
    // TODO Document.
    public void end() {
        ended = true;
    }
}
