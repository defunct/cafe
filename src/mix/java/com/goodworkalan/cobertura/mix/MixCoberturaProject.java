package com.goodworkalan.mix.cobertura.mix;

import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.mix.ProjectModule;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.builder.JavaProject;

public class MixCoberturaProject extends ProjectModule {
    @Override
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces(new Artifact("com.goodworkalan/mix-cobertura/0.1"))
                .main()
                    .depends()
                        .artifact(new Artifact("com.goodworkalan/mix/0.1.2"))
                        .artifact(new Artifact("com.goodworkalan/comfort-io/0.1.1"))
                        .artifact(new Artifact("com.goodworkalan/spawn/0.1.1"))
                        .end()
                    .end()
                .test()
                    .depends()
                        .artifact(new Artifact("org.testng/testng/5.10"))
                        .end()
                    .end()
                .end()
            .end();
    }
}