package com.goodworkalan.mix;

import java.io.File;

import org.testng.annotations.Test;

public class JavadocTest {
    @Test
    public void javadoc() {
        MixTask.Arguments arguments = new MixTask.Arguments();
        arguments.addWorkingDirectory(new File("src/test/project"));
//        Javadoc javadoc = new Javadoc();
//        javadoc.setConfiguration(new MixTask.Configuration(new Project(new File("src/test/project"), Collections.<String, Recipe>emptyMap())));
//        javadoc.execute();
    }
}
