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
                .argument("output-directory", "target/classes")
                .argument("debug", "true")
                .end()
            .command("copy")
                .argument("source-directory", "src/main/resources")
                .argument("output-directory", "target/classes")
                .end()
            .produces()
                .classes("target/classes")
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
                .argument("output-directory", "target/test-classes")
                .argument("debug", "true")
                .end()
            .produces()
                .classes("target/test-classes")
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
                    .argument("file", "target")
                    .argument("recurse", "true")
                    .end()
                .end()
            .recipe("distribution")
                .depends()
                    .source("javac")
                    .end()
                .command("delete")
                    .argument("file", "target/distribution")
                    .argument("recurse", "true")
                    .end()
                .command("mkdirs")
                    .argument("directory", "target/distribution/" + produces.getDirectoryPath())
                    .end()
                .command("dependencies")
                    .argument("output-file", "target/distribution/" + produces.getPath("", "dep"))
                    .end()
                .command("zip")
                    .argument("source-directory", "target/classes")
                    .argument("level", "0")
                    .argument("output-file", "target/distribution/" + produces.getPath("", "jar"))
                    .end()
                .produces()
                    .artifact(produces.getGroup(), produces.getName(), produces.getVersion()).in("target/distribution")
                    .end()
                .end()
            .recipe("javadoc")
                .depends()
                    .source("javac")
                    .end()
                .command("delete")
                    .argument("file", "target/apidocs")
                    .argument("recurse", "true")
                    .end()
                .command("mkdirs")
                    .argument("directory", "target/apidocs")
                    .end()
                .command("javadoc")
                    .argument("source-directory", "src/main/java")
                    .argument("output-directory", "target/apidocs")
                    .argument("offline-link", "src/mix/package-lists/java")
                    .argument("offline-uri", "http://java.sun.com/j2se/1.5.0/docs/api")
                    .end()
                .command("delete")
                    .argument("file", "target/devdocs")
                    .argument("recurse", "true")
                    .end()
                .command("mkdirs")
                    .argument("directory", "target/devdocs")
                    .end()
                .command("javadoc")
                    .argument("source-directory", "src/main/java")
                    .argument("output-directory", "target/devdocs")
                    .argument("visibility", "private")
                    .end()
                .end()
            .end();
    }
}
