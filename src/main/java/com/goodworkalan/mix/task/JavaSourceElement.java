package com.goodworkalan.mix.task;

import java.util.List;

public class JavaSourceElement<P> {
    private final List<JavacConfiguration> configurations ;
    
    private final P parent;
    
    public JavaSourceElement(P parent, List<JavacConfiguration> configurations) {
        this.parent = parent;
        this.configurations = configurations;
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
