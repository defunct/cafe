package com.goodworkalan.mix.outline;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.goodworkalan.cafe.builder.Builder;
import com.goodworkalan.cafe.task.Copy;
import com.goodworkalan.cafe.task.Dependencies;
import com.goodworkalan.cafe.task.Javac;
import com.goodworkalan.cafe.task.JavacConfiguration;
import com.goodworkalan.cafe.task.JavacEnd;
import com.goodworkalan.cafe.task.JavacOptionsElement;
import com.goodworkalan.cafe.task.Javadoc;
import com.goodworkalan.cafe.task.JavadocConfiguration;
import com.goodworkalan.cafe.task.JavadocEnd;
import com.goodworkalan.cafe.task.JavadocOptionsElement;
import com.goodworkalan.cafe.task.Mkdirs;
import com.goodworkalan.cafe.task.TestNG;
import com.goodworkalan.cafe.task.Unlink;
import com.goodworkalan.cafe.task.War;
import com.goodworkalan.cafe.task.Zip;
import com.goodworkalan.go.go.library.Artifact;

// TODO Document.
public class JavaProject extends JavaSpecificsClause {
    // TODO Document.
    private Artifact produces;
    
    // TODO Document.
    private final List<JavadocConfiguration> apidocConfigurations = new ArrayList<JavadocConfiguration>();

    // TODO Document.
    private final List<JavadocConfiguration> devdocConfigurations = new ArrayList<JavadocConfiguration>();
    
    // TODO Document.
    public JavaProject(Builder builder) {
        super(builder);
        builder.recipe("production").end();
        builder.recipe("development").end();
    }
    
    // TODO Document.
    public JavaProject produces(String artifact) {
        this.produces = new Artifact(artifact);
        return this;
    }

    /**
     * Create a project dependency builder to specify the projects production
     * and development dependencies. This is a shorthand for projects that
     * customize nothing about the default project and only specify their
     * dependencies.
     * 
     * @return A project dependency builder.
     */
    public DependencyStatement depends() {
        return new DependencyStatement(this, builder);
    }
    
    // TODO Document.
    public JavacOptionsElement<JavaProject, JavacOptionsElement<JavaProject, ?>> javac() {
        return JavacOptionsElement.<JavaProject>newJavacOptionsElement(this, new JavacEnd() {
            public void end(JavacConfiguration configuration) {
                javacConfigurations.add(configuration);
            }
        });
    }
    
    // TODO Document.
    public JavadocOptionsElement<JavaProject, JavadocOptionsElement<JavaProject, ?>> apidocs() {
        return JavadocOptionsElement.newJavadocOptionsElement(this, new JavadocEnd() {
            public void end(JavadocConfiguration configuration) {
                apidocConfigurations.add(configuration);
            }
        });
    }
    
    // TODO Document.
    public JavadocOptionsElement<JavaProject, JavadocOptionsElement<JavaProject, ?>> devdocs() {
        return JavadocOptionsElement.newJavadocOptionsElement(this, new JavadocEnd() {
            public void end(JavadocConfiguration configuration) {
                devdocConfigurations.add(configuration);
            }
        });
    }
    
    // TODO Document.
    public Builder end() {
        builder
            .recipe("javac")
                .rebuild()
                    .when()
                        .source(new File("src/main/java")).isFile().end()
                        .source(new File("src/main/resources")).isFile().end()
                        .source(new File("src/mix/java")).isFile().end()
                        .source(new File("src/mix/resources")).isFile().end()
                    .newerThan()
                        .output(new File("target/classes")).isFile().end()
                    .end()
                .depends()
                    .recipe("production")
                    .end()
                .task(Unlink.class)
                    .file(new File("target/classes"))
                    .recurse(true)
                    .end()
                .task(Javac.class)
                    .source(new File("src/main/java")).end()
                    .configure(mainJavacConfigurations)
                    .output(new File("target/classes"))
                    .end()
                .task(Copy.class)
                    .source(new File("src/main/resources")).exclude("**/.svn/**").end()
                    .output(new File("target/classes"))
                    .end()
                 .produces()
                     .classes(new File("target/classes"))
                     .end()
                 .end()
            .recipe("javac-test")
                .rebuild()
                    .when()
                        .source(new File("src/test/java")).isFile().end()
                        .source(new File("src/test/resources")).isFile().end()
                        .source(new File("src/cafe/java")).isFile().end()
                        .source(new File("src/cafe/resources")).isFile().end()
                    .newerThan()
                        .output(new File("target/test-classes")).isFile().end()
                    .end()
                .depends()
                    .recipe("development")
                    .recipe("javac")
                    .end()
                .task(Unlink.class)
                    .file(new File("target/test-classes"))
                    .recurse(true)
                    .end()
                .task(Javac.class)
                    .source(new File("src/test/java")).end()
                    .configure(testJavacConfigurations)
                    .output(new File("target/test-classes"))
                    .end()
                .task(Copy.class)
                    .source(new File("src/test/resources")).exclude("**/.svn/**").end()
                    .output(new File("target/test-classes"))
                    .end()
                .produces()
                    .classes(new File("target/test-classes"))
                    .end()
                .end()
            .recipe("apidocs")
                .rebuild()
                    .when()
                        .source(new File("src/main/java")).isFile().end()
                        .source(new File("src/main/resources")).isFile().end()
                    .newerThan()
                        .output(new File("target/apidocs")).isFile().end()
                    .end()
                .depends()
                    .recipe("javac")
                    .end()
                .task(Unlink.class)
                    .file(new File("target/apidocs")).recurse(true)
                    .end()
                .task(Mkdirs.class)
                    .directory(new File("target/apidocs"))
                    .end()
                .task(Javadoc.class)
                    .configure(apidocConfigurations)
                    .packageLists(new File("src/mix/package-lists"))
                    .source(new File("src/main/java")).end()
                    .output(new File("target/apidocs"))
                    .end()
                .end()
            .recipe("devdocs")
                .rebuild()
                    .when()
                        .source(new File("src/main/java")).isFile().end()
                        .source(new File("src/main/resources")).isFile().end()
                    .newerThan()
                        .output(new File("target/devdocs")).isFile().end()
                    .end()
                .depends()
                    .recipe("javac")
                    .end()
                .task(Unlink.class)
                    .file(new File("target/devdocs")).recurse(true)
                    .end()
                .task(Mkdirs.class)
                    .directory(new File("target/devdocs"))
                    .end()
                .task(Javadoc.class)
                    .visibility("private")
                    .configure(devdocConfigurations)
                    .packageLists(new File("src/cafe/package-lists"))
                    .source(new File("src/main/java")).end()
                    .output(new File("target/devdocs"))
                    .end()
                .end()
            .recipe("test")
                .depends()
                    .recipe("javac-test")
                    .end()
                .task(TestNG.class)
                    .classes(new File("target/classes"))
                    .source(new File("src/test/java")).end()
                    .output(new File("target/test-output"))
                    .end()
                .end()
            .recipe("clean")
                .task(Unlink.class)
                    .file(new File("target"))
                    .recurse(true)
                    .end()
                .end()
            .recipe("war")
                .make("distribution")
                .depends()
                    .recipe("javac")
                    .end()
                .task(Mkdirs.class)
                    .directory(new File("target/distribution"))
                    .end()
                .task(War.class)
                    .source(new File("src/main/webapp")).end()
                    .level(0)
                    .output(new File("target/distribution/" + produces.getFileName("war")))
                    .end()
                .end()
            .recipe("distribution")
                .rebuild()
                    .when()
                        .source(new File("src/main/java")).isFile().end()
                        .source(new File("src/main/resources")).isFile().end()
                        .source(new File("target/classes")).isFile().end()
                        .source(new File("target/apidocs")).isFile().end()
                    .newerThan()
                        .output(new File("target/distribution")).isFile().end()
                    .end()
                .depends()
                    .recipe("javac").recipe("apidocs")
                    .end()
                .task(Unlink.class)
                    .file(new File("target/distribution"))
                    .recurse(true)
                    .end()
                .task(Mkdirs.class)
                    .directory(new File("target/distribution/" + produces.getDirectoryPath()))
                    .end()
                .task(Dependencies.class)
                    .recipe("javac")
                    .output(new File("target/distribution/" + produces.getPath("dep")))
                    .end()
                .task(Zip.class)
                    .source(new File("target/classes")).end()
                    .level(0)
                    .output(new File("target/distribution/" + produces.getPath("jar")))
                    .end()
                .task(Zip.class)
                    .source(new File("src/main/java")).exclude("**/.svn/**").end()
                    .source(new File("src/main/resources")).exclude("**/.svn/**").end()
                    .level(0)
                    .output(new File("target/distribution/" + produces.getPath("sources/jar")))
                    .end()
                .task(Zip.class)
                    .source(new File("target/apidocs")).end()
                    .level(0)
                    .output(new File("target/distribution/" + produces.getPath("javadoc/jar")))
                    .end()
                .produces()
                    .artifact(produces).in(new File("target/distribution"))
                    .end()
                .end();
        return builder;
    }
}
