package com.goodworkalan.mix.builder;

import org.testng.annotations.Test;

import com.goodworkalan.mix.Builder;

public class BasicJavaProjectTest {
    @Test
    public void build() {
        Builder builder = new Builder();
        builder
            .cookbook(BasicJavaProject.class)
                .source("1.5")
                .main()
                    .javac().debug(true).end()
                    .end()
                .end()
            .end();
    }
}
