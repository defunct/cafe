package com.goodworkalan.mix.eclipse.mix;

import com.goodworkalan.mix.ProjectModule;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.builder.JavaProject;

/**
 * Builds the project definition for Mix Eclipse.
 *
 * @author Alan Gutierrez
 */
public class MixEclipseProject implements ProjectModule {
    /**
     * Build the project definition Mix Eclipse.
     *
     * @param builder
     *          The project builder.
     */
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces("com.github.bigeasy.mix/mix-eclipse/0.1.0.2")
                .main()
                    .depends()
                        .include("com.github.bigeasy.mix/mix/0.+1.3.12")
                        .end()
                    .end()    
                .end()
            .end();
    }
}
