package com.goodworkalan.mix;

import org.testng.annotations.Test;

import com.goodworkalan.go.go.CommandInterpreter;

/**
 * Unit tests for mix root command.
 *
 * @author Alan Gutierrez
 */
public class MixTest {
    /** Test the welcome message. */
    @Test
    public void execute() {
        CommandInterpreter ci = new CommandInterpreter();
        ci.execute("mix", "make", "javac");
    }
}
