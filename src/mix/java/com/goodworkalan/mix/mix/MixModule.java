package com.goodworkalan.mix.mix;

import com.goodworkalan.mix.Builder;
import com.goodworkalan.mix.ProjectModule;

public class MixModule extends ProjectModule {
    @Override
    public void build(Builder builder) {
        // FIXME Does it make sense at all to have ...
        // FIXME How to reuse? Can you swap in compilers? Are projects
        // ordered by different source directories?
        // FIXME How do I test this in Eclipse?
        // FIXME How do I insert a transform?
        // FIXME Maybe I build class paths separately, and mix output
        // has a class path server?
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
                    .argument("include", "**/*.java")
                    .end()
                .command("copy")
                    .argument("source-directory", "src/resources/java")
                    .argument("output-directory", "smotchkiss/classes")
                    .end()
                .produces()
                    .classes("smotchkiss/classes")
                    .end()
                .end()
            .recipe("javac-test")
                .depends()
                    .source("javac")
                    .artifact("org.testng", "testng", "5.8")
                    .end()
                .command("javac")
                    .argument("source-directory", "src/test/java")
                    .argument("output-directory", "smotchkiss/test-classes")
                    .argument("include", "**/*.java")
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
                    .end()
                .end()
            .recipe("clean")
                .command("delete")
                    .argument("directory", "smotchkiss")
                    .end()
                .end();
    }
}
