package com.goodworkalan.mix;

import java.io.File;
import java.util.Collections;

import org.testng.annotations.Test;

import com.goodworkalan.go.go.CommandInterpreter;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.EnvironmentBuilder;

/**
 * Test the compiler.
 * 
 * @author Alan Gutierrez
 */
public class CompileTest {
    @Test
    public void hello() {
        MixTask.Configuration configuration = new MixTask.Configuration(new Project(new File("src/test/project"), Collections.<String, Recipe>emptyMap()));
        JavacTask compile = new JavacTask();
        compile.setConfiguration(configuration);
        Environment environment = new EnvironmentBuilder().getInstance();
        compile.execute(environment);
    }
    
    @Test
    public void foo() {
        new CommandInterpreter(null).main("mix", "--working-directory=src/test/project", "javac");
    }
}
