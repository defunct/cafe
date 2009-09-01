package com.goodworkalan.mix;

import java.io.File;

import org.testng.annotations.Test;

/**
 * Test the compiler.
 * 
 * @author Alan Gutierrez
 */
public class CompileTest {
    @Test
    public void hello() {
        Mix.Arguments arguments = new Mix.Arguments();
        arguments.setWorkingDirectory(new File("src/test/project"));
        Compile compile = new Compile();
        compile.setConfiguration(arguments);
        compile.execute();
    }
}
