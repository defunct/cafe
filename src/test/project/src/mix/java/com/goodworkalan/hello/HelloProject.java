package com.goodworkalan.mix.mix;

import com.goodworkalan.mix.ProjectModule;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.cookbook.JavaProject;

public class HelloProject implements ProjectModule {
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces("com.goodworkalan/hello/0.1")
                .main()
                    .depends()
                        .artifact("com.github.bigeasy.reflective/reflective/0.+1")
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
