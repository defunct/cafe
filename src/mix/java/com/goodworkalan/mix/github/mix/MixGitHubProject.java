package com.goodworkalan.mix.eclipse.mix;

import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.mix.ProjectModule;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.builder.JavaProject;

public class MixGitHubProject extends ProjectModule {
    @Override
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces(new Artifact("com.github.bigeasy.mix/mix-github/0.1"))
                .main()
                    .depends()
                        .artifact(new Artifact("com.goodworkalan/mix/0.1.2"))
                        .artifact(new Artifact("com.goodworkalan/spawn/0.1.1"))
                        .artifact(new Artifact("com.github.bigeasy.github-downloads/github-downloads/0.1"))
                        .end()
                    .end()    
                .test()
                    .depends()
                        .artifact(new Artifact("org.testng/testng/5.10/jdk15"))
                        .end()
                    .end()
                .end()
            .end();
    }
}
