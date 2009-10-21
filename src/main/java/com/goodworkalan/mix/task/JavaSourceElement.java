package com.goodworkalan.mix.task;

import java.util.List;
import java.util.Map;

import com.goodworkalan.mix.Dependency;
import com.goodworkalan.mix.builder.DependsElement;

public class JavaSourceElement<P> {
    private final List<JavacConfiguration> configurations ;
    
    private final Map<List<String>, Dependency> dependencies;
    
    private final P parent;
    
    public JavaSourceElement(P parent, List<JavacConfiguration> configurations,  Map<List<String>, Dependency> dependencies) {
        this.parent = parent;
        this.configurations = configurations;
        this.dependencies = dependencies;
    }
    
    public DependsElement<JavaSourceElement<P>> depends() {
        return new DependsElement<JavaSourceElement<P>>(this, dependencies);
    }
    
    public JavacOptionsElement<JavaSourceElement<P>, JavacOptionsElement<JavaSourceElement<P>, ?>> javac() {
        return JavacOptionsElement.<JavaSourceElement<P>>newJavacOptionsElement(this, new JavacEnd() {
            public void end(JavacConfiguration configuration) {
                configurations.add(configuration);
            }
        });
    }

    public P end() {
        return parent;
    }
}
