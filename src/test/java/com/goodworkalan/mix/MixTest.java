package com.goodworkalan.mix;

import java.io.File;
import java.util.Collections;

import org.testng.annotations.Test;

import com.goodworkalan.go.go.CommandInterpreter;
import com.goodworkalan.go.go.ErrorCatcher;

/**
 * Unit tests for mix root command.
 *
 * @author Alan Gutierrez
 */
public class MixTest {
    /** Test the welcome message. */
    @Test
    public void execute() {
        CommandInterpreter ci = new CommandInterpreter(new ErrorCatcher(), Collections.<File>emptyList());
        ci.execute(new File("."), "mix", "--working-directory=src/test/project", "make", "distribution");
    }
}
