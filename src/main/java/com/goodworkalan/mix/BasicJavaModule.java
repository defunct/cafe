package com.goodworkalan.mix;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.goodworkalan.go.go.Artifact;

public class BasicJavaModule extends ProjectModule {
    private final List<Artifact> dependencies = new ArrayList<Artifact>();
    
    private final List<Artifact> testDepenencies = new ArrayList<Artifact>();
    
    private final Artifact produces;
    
    private final List<URI> links = new ArrayList<URI>();
    
    public BasicJavaModule(Artifact produces) {
        this.produces = produces;
    }
    
    public void addDependency(Artifact artifact) {
        dependencies.add(artifact);
    }
    
    public void addTestDependency(Artifact artifact) {
        testDepenencies.add(artifact);
    }
    
    public void addLink(URI uri) {
        links.add(uri);
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
                .argument("source", "1.5")
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
                .argument("source", "1.5")
                .argument("debug", "true")
                .end()
            .produces()
                .classes("target/test-classes")
                .end()
            .end();

        recipe = builder.recipe("javadoc");
        recipe
            .depends()
                .source("javac")
                .end();
        recipe
            .command("delete")
                .argument("file", "target/apidocs")
                .argument("recurse", "true")
                .end()
            .command("mkdirs")
                .argument("directory", "target/apidocs")
                .end();
        CommandElement<RecipeElement> command = recipe.command("javadoc");
        command
            .argument("source-directory", "src/main/java")
            .argument("output-directory", "target/apidocs");
        for (URI link : links) {
            command.argument("link", link.toASCIIString());
        }
        command.end();
        recipe
            .command("delete")
                .argument("file", "target/devdocs")
                .argument("recurse", "true")
                .end()
            .command("mkdirs")
                .argument("directory", "target/devdocs")
                .end();
        command = recipe.command("javadoc");
        command
            .argument("source-directory", "src/main/java")
            .argument("output-directory", "target/apidocs")
            .argument("visibility", "private");
        for (URI link : links) {
            command.argument("link", link.toASCIIString());
        }
        command.end();
        recipe.end();
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
                    .source("javadoc")
                    .end()
                .command("delete")
                    .argument("file", "target/distribution")
                    .argument("recurse", "true")
                    .end()
                .command("mkdirs")
                    .argument("directory", "target/distribution/" + produces.getDirectoryPath())
                    .end()
                .command("dependencies")
                    .argument("output-file", "target/distribution/" + produces.getPath("dep"))
                    .end()
                .command("zip")
                    .argument("source-directory", "target/classes")
                    .argument("level", "0")
                    .argument("output-file", "target/distribution/" + produces.getPath("jar"))
                    .end()
                .command("zip")
                    .argument("source-directory", "src/main/java")
                    .argument("source-directory", "src/main/resources")
                    .argument("level", "0")
                    .argument("output-file", "target/distribution/" + produces.getPath("sources/jar"))
                    .end()
                .command("zip")
                    .argument("source-directory", "target/apidocs")
                    .argument("level", "0")
                    .argument("output-file", "target/distribution/" + produces.getPath("javadoc/jar"))
                    .end()
                .produces()
                    .artifact(produces.getGroup(), produces.getName(), produces.getVersion()).in("target/distribution")
                    .end()
                .end()
            .end();
    }
}
