package com.goodworkalan.cafe.eclipse;

import com.goodworkalan.cafe.ProjectModule;
import com.goodworkalan.cafe.builder.Builder;
import com.goodworkalan.cafe.outline.JavaProject;

/**
 * Builds the project definition for Cafe Eclipse.
 *
 * @author Alan Gutierrez
 */
public class CafeEclipseProject implements ProjectModule {
    /**
     * Build the project definition Cafe Eclipse.
     *
     * @param builder
     *          The project builder.
     */
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces("com.github.bigeasy.cafe/cafe-eclipse/0.1.0.3")
                .depends()
                    .production("com.github.bigeasy.cafe/cafe/0.+1.4")
                    .production("com.github.bigeasy.comfort-xml/comfort-xml/0.+1")
                    .production("com.github.bigeasy.comfort-io/comfort-io/0.+1")
                    .end()    
                .end()
            .end();
    }
}
