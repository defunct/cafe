package com.goodworkalan.mix;

import java.io.File;

import org.testng.annotations.Test;

public class JavadocTest {
    @Test
    public void javadoc() {
        Mix.Arguments arguments = new Mix.Arguments();
        arguments.setWorkingDirectory(new File("src/test/project"));
        Javadoc javadoc = new Javadoc();
        javadoc.setConfiguration(arguments);
        javadoc.execute();
    }
}
