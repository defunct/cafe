package com.goodworkalan.cafe.cobertura;

import com.goodworkalan.cafe.ProjectModule;
import com.goodworkalan.cafe.builder.Builder;
import com.goodworkalan.cafe.outline.JavaProject;

/**
 * Builds the project definition for Cafe Cobertura.
 *
 * @author Alan Gutierrez
 */
public class CafeCoberturaProject implements ProjectModule {
    /**
     * Build the project definition for Cafe Cobertura.
     *
     * @param builder
     *          The project builder.
     */
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces("com.github.bigeasy.cafe/cafe-cobertura/0.1.0.5")
                .depends()
                    .production("com.github.bigeasy.cafe/cafe/0.1.+4")
                    .production("com.github.bigeasy.comfort-io/comfort-io/0.1.+1")
                    .production("com.github.bigeasy.spawn/spawn/0.1.+1")
                    .development("org.testng/testng-jdk15/5.10")
                    .end()
                .end()
            .end();
    }
}
