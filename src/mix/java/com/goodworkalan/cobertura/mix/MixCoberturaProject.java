package com.goodworkalan.mix.cobertura.mix;

import com.goodworkalan.mix.ProjectModule;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.builder.JavaProject;

/**
 * Builds the project definition for Mix Cobertura.
 *
 * @author Alan Gutierrez
 */
public class MixCoberturaProject implements ProjectModule {
    /**
     * Build the project definition for Mix Cobertura.
     *
     * @param builder
     *          The project builder.
     */
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces("com.github.bigeasy.mix/mix-cobertura/0.1.0.2")
                .depends()
                    .production("com.github.bigeasy.mix/mix/0.1.+3.1")
                    .production("com.github.bigeasy.comfort-io/comfort-io/0.1.+1")
                    .production("com.github.bigeasy.spawn/spawn/0.1.+1")
                    .development("org.testng/testng-jdk15/5.10")
                    .end()
                .end()
            .end();
    }
}
