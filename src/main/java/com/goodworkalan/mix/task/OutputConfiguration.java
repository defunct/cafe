package com.goodworkalan.mix.task;

import java.io.File;

public interface OutputConfiguration {
    public File getOuptut();
    
    public void configure(Outputable<?> outputable);
}
