package com.goodworkalan.mix.builder;

import com.goodworkalan.go.go.Environment;

public interface Executable {
    // FIXME No longer necessary to pass Mix and Project since they are available
    // through the get method.
    public void execute(Environment env);
}
