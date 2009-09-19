package com.goodworkalan.mix.mix;

import com.goodworkalan.mix.Builder;
import com.goodworkalan.mix.ProjectModule;

public class MixModule extends ProjectModule {
    @Override
    public void build(Builder builder) {
        builder
            .recipe("javac")
                .depends()
                    .artifact("com.goodworkalan", "spawn", "0.1")
                    .artifact("com.goodworkalan", "go-go", "0.1")
                    .artifact("com.goodworkalan", "glob", "0.1-SNAPSHOT")
                    .end()
                .command("javac")
                    .argument("source-directory", "src/main/java")
                    .argument("output-directory", "smotchkiss/classes")
                    .argument("debug", "true")
                    .end()
                .command("copy")
                    .argument("source-directory", "src/main/resources")
                    .argument("output-directory", "smotchkiss/classes")
                    .end()
                .produces()
                    .classes("smotchkiss/classes")
                    .end()
                .end()
            .recipe("javac-test")
                .depends()
                    .source("javac")
                    .artifact("org.testng", "testng", "5.10")
                    .end()
                .command("javac")
                    .argument("source-directory", "src/test/java")
                    .argument("output-directory", "smotchkiss/test-classes")
                    .argument("debug", "true")
                    .end()
                .produces()
                    .classes("smotchkiss/test-classes")
                    .end()
                .end()
            .recipe("test")
                .depends()
                    .source("javac-test")
                    .end()
                .command("test-ng")
                    .argument("source-directory", "src/test/java")
                    .end()
                .end()
            .recipe("clean")
                .command("delete")
                    .argument("file", "smotchkiss")
                    .argument("recurse", "true")
                    .end()
                .end()
            .end();
    }
}
