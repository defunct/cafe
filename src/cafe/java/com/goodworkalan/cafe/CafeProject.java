package com.goodworkalan.cafe;

import com.goodworkalan.cafe.ProjectModule;
import com.goodworkalan.cafe.builder.Builder;
import com.goodworkalan.cafe.outline.JavaProject;

public class CafeProject implements ProjectModule {
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces("com.github.bigeasy.cafe/cafe/0.1.4.1")
                .depends()
                    .production("com.github.bigeasy.spawn/spawn/0.+1.1")
                    .production("com.github.bigeasy.danger/danger/0.+3")
                    .production("com.github.bigeasy.go-go/go-go/0.+1.4")
                    .production("com.github.bigeasy.comfort-io/comfort-io/0.+1.1")
                    .development("org.testng/testng-jdk15/5.10")
                    .end()
                .end()
            .end();
    }
}
