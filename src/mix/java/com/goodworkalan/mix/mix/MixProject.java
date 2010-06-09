package com.goodworkalan.mix.mix;

import com.goodworkalan.mix.ProjectModule;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.cookbook.JavaProject;

public class MixProject implements ProjectModule {
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces("com.github.bigeasy.mix/mix/0.1.3.16")
                .main()
                    .depends()
                        .artifact("com.github.bigeasy.spawn/spawn/0.+1.1")
                        .artifact("com.github.bigeasy.go-go/go-go/0.+1.4")
                        .artifact("com.github.bigeasy.comfort-io/comfort-io/0.+1.1")
                        .end()
                    .end()
                .test()
                    .depends()
                        .artifact("org.testng/testng-jdk15/5.10")
                        .end()
                    .end()
                .end()
            .end();
    }
}
