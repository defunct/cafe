package com.goodworkalan.mix.builder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.goodworkalan.go.go.library.Include;
import com.goodworkalan.mix.Dependency;
import com.goodworkalan.mix.task.JavaSourceElement;
import com.goodworkalan.mix.task.JavacConfiguration;

public class JavaSpecificsElement {
    protected final Builder builder;

    protected final Map<List<String>, Dependency> mainDependencies = new LinkedHashMap<List<String>, Dependency>();
    
    protected final Map<List<String>, Dependency> testDependencies = new LinkedHashMap<List<String>, Dependency>();

    protected final List<JavacConfiguration> javacConfigurations = new ArrayList<JavacConfiguration>();

    protected final List<JavacConfiguration> mainJavacConfigurations = new ArrayList<JavacConfiguration>();

    protected final List<JavacConfiguration> testJavacConfigurations = new ArrayList<JavacConfiguration>();

    protected JavaSpecificsElement(Builder builder) {
        this.builder = builder;
    }

    private void moreSpecific() {
        if (mainJavacConfigurations.isEmpty()) {
            mainJavacConfigurations.addAll(javacConfigurations);
        }
        if (testJavacConfigurations.isEmpty()) {
            testJavacConfigurations.addAll(javacConfigurations);
        }
    }

    /**
     * Add a single artifact dependency that will be used to build all of the
     * primary source code, and will be included in the list of production
     * dependencies. The given list of excludes specify which of the artifacts
     * in the given artifacts dependencies to exclude by artifact group and
     * name.
     * 
     * @param artifact
     *            The artifact to add to the project.
     * @param excludes
     *            The artifacts to exclude from the project.
     * @return This project builder to continue building the project.
     */
    public JavaSpecificsElement production(String artifact, String...excludes) {
        Include include = new Include(artifact, excludes);
        List<String> key = include.getArtifact().getUnversionedKey();
        if (!mainDependencies.containsKey(key)) {
            mainDependencies.put(key, new ArtifactDependency(include));
        }
        return this;
    }

    /**
     * Add a single artifact dependency that will be used to build all of the
     * supporting source code and tests. The dependency will not be included in
     * the list of production dependencies. The given list of excludes specify
     * which of the artifacts in the given artifacts dependencies to exclude by
     * artifact group and name.
     * 
     * @param artifact
     *            The artifact to add to the project.
     * @param excludes
     *            The artifacts to exclude from the project.
     * @return This project builder to continue building the project.
     */
    public JavaSpecificsElement development(String artifact, String...excludes) {
        Include include = new Include(artifact, excludes);
        List<String> key = include.getArtifact().getUnversionedKey();
        if (!testDependencies.containsKey(key)) {
            testDependencies.put(key, new ArtifactDependency(include));
        }
        return this;
    }

    public JavaSourceElement<JavaSpecificsElement> main() {
        moreSpecific();
        return new JavaSourceElement<JavaSpecificsElement>(this, mainJavacConfigurations, mainDependencies);
    }
    
    public JavaSourceElement<JavaSpecificsElement> test() {
        moreSpecific();
        return new JavaSourceElement<JavaSpecificsElement>(this, testJavacConfigurations, testDependencies);
    }
    
    public Builder end() {
        return builder;
    }
}
