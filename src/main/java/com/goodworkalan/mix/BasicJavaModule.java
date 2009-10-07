package com.goodworkalan.mix;

import java.util.ArrayList;
import java.util.List;

import com.goodworkalan.go.go.Artifact;

public class BasicJavaModule extends ProjectModule {
    private final List<Artifact> dependencies = new ArrayList<Artifact>();
    
    private final List<Artifact> testDepenencies = new ArrayList<Artifact>();
    
    private final Artifact produces;
    
    public BasicJavaModule(Artifact produces) {
        this.produces = produces;
    }
    
    public void addDependency(Artifact artifact) {
        dependencies.add(artifact);
    }
    
    public void addTestDependency(Artifact artifact) {
        testDepenencies.add(artifact);
    }
    
    @Override
    public void build(Builder builder) {
        RecipeElement recipe = builder.recipe("javac");
        DependsElement depends = recipe.depends();
        for (Artifact artifact : dependencies) {
            depends.artifact(artifact.getGroup(), artifact.getName(), artifact.getVersion());
        }
        depends.end();
        recipe
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
            .end();
        recipe = builder.recipe("javac-test");
        depends = recipe.depends();
        depends.source("javac");
        for (Artifact artifact : testDepenencies) {
            depends.artifact(artifact.getGroup(), artifact.getName(), artifact.getVersion());
        }
        depends.end();
        recipe
            .command("javac")
                .argument("source-directory", "src/test/java")
                .argument("output-directory", "smotchkiss/test-classes")
                .argument("debug", "true")
                .end()
            .produces()
                .classes("smotchkiss/test-classes")
                .end()
            .end();
        builder
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
            .recipe("distribution")
                .depends()
                    .source("javac")
                    .end()
                .command("delete")
                    .argument("file", "smotchkiss/distribution")
                    .argument("recurse", "true")
                    .end()
                .command("mkdirs")
                    .argument("directory", "smotchkiss/distribution/com/goodworkalan/mix/0.1")
                    .end()
                .command("dependencies")
                    .argument("output-file", "smotchkiss/distribution/" + produces.getPath("", "dep"))
                    .end()
                .command("zip")
                    .argument("source-directory", "smotchkiss/classes")
                    .argument("level", "0")
                    .argument("output-file", "smotchkiss/distribution/" + produces.getPath("", "jar"))
                    .end()
                .produces()
                    .artifact(produces.getGroup(), produces.getName(), produces.getVersion()).in("smotchkiss/distribution")
                    .end()
                .end()
            .end();
    }
}
