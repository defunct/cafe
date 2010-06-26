package com.goodworkalan.mix.eclipse.mix;

import com.goodworkalan.mix.ProjectModule;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.cookbook.JavaProject;

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
                .produces("com.github.bigeasy.mix/mix-github/0.1.0.1")
                .depends()
                    .production("com.github.bigeasy.mix/mix/0.+1")
                    .production("com.github.bigeasy.spawn/spawn/0.+1")
                    .production("com.github.bigeasy.github4j/github4j-downloads/0.+1")
                    .production("com.github.bigeasy.github4j/github4j-uploader/0.+1")
                    .development("org.testng/testng-jdk15/5.10")
                    .end()
                .end()
            .end();
    }
}
