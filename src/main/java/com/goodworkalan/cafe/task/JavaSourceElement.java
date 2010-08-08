package com.goodworkalan.cafe.task;

import java.util.List;

// TODO Document.
public class JavaSourceElement<P> {
    // TODO Document.
    private final List<JavacConfiguration> configurations ;
    
    // TODO Document.
    private final P parent;
    
    // TODO Document.
    public JavaSourceElement(P parent, List<JavacConfiguration> configurations) {
        this.parent = parent;
        this.configurations = configurations;
    }
    
    // TODO Document.
    public JavacOptionsElement<JavaSourceElement<P>, JavacOptionsElement<JavaSourceElement<P>, ?>> javac() {
        return JavacOptionsElement.<JavaSourceElement<P>>newJavacOptionsElement(this, new JavacEnd() {
            public void end(JavacConfiguration configuration) {
                configurations.add(configuration);
            }
        });
    }

    // TODO Document.
    public P end() {
        return parent;
    }
}
