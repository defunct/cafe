package com.goodworkalan.mix.eclipse.mix;

import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.mix.ProjectModule;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.builder.JavaProject;

public class MixEclipseProject extends ProjectModule {
    @Override
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces(new Artifact("com.goodworkalan/mix-eclipse/0.1"))
                .main()
                    .depends()
                        .artifact(new Artifact("com.goodworkalan/mix/0.1.2"))
                        .end()
                    .end()    
                .end()
            .end();
    }
}
