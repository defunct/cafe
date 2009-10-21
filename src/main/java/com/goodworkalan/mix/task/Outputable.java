package com.goodworkalan.mix.task;

import java.io.File;

public interface Outputable<S> {
    public S output(File output);
}
