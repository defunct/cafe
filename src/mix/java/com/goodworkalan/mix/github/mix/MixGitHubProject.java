package com.goodworkalan.mix.eclipse.mix;

import com.goodworkalan.mix.ProjectModule;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.builder.JavaProject;

/**
 * Builds the project definition for Mix GitHub.
 *
 * @author Alan Gutierrez
 */
public class MixGitHubProject implements ProjectModule {
    /**
     * Build the project definition for Mix GitHub.
     *
     * @param builder
     *          The project builder.
     */
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces("com.github.bigeasy.mix/mix-github/0.1")
                .main()
                    .depends()
                        .include("com.github.bigeasy.mix/mix/0.+1")
                        .include("com.github.bigeasy.spawn/spawn/0.+1")
                        .include("com.github.bigeasy.github4j/github4j-downloads/0.+1")
                        .include("com.github.bigeasy.github4j/github4j-uploader/0.+1")
                        .end()
                    .end()    
                .test()
                    .depends()
                        .include("org.testng/testng-jdk15/5.10")
                        .end()
                    .end()
                .end()
            .end();
    }
}
