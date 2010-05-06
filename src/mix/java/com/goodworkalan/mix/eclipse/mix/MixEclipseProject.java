package com.goodworkalan.mix.eclipse.mix;

import com.goodworkalan.mix.ProjectModule;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.builder.JavaProject;

public class MixEclipseProject extends ProjectModule {
    @Override
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces("com.github.bigeasy.mix/mix-eclipse/0.1")
                .main()
                    .depends()
                        .include("com.github.bigeasy.mix/mix/0.+1.4")
                        .end()
                    .end()    
                .end()
            .end();
    }
}
