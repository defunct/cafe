package com.goodworkalan.mix.cobertura.mix;

import com.goodworkalan.mix.ProjectModule;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.cookbook.JavaProject;

/**
 * Builds the project definition for Mix Antlr.
 *
 * @author Alan Gutierrez
 */
public class MixAntlrProject implements ProjectModule {
    /**
     * Build the project definition for Mix Antlr.
     *
     * @param builder
     *          The project builder.
     */
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces("com.github.bigeasy.mix/mix-antlr/0.1")
                .depends()
                    .production("com.github.bigeasy.mix/mix/0.1.+3.1")
                    .production("antlr/antlr/2.7.+6")
                    .development("org.testng/testng-jdk15/5.10")
                    .end()
                .end()
            .end();
    }
}
