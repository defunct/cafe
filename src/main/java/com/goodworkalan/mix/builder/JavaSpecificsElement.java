package com.goodworkalan.mix.builder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
