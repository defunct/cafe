package com.goodworkalan.mix;

import static com.goodworkalan.comfort.io.Files.file;

import java.io.File;
import java.util.Collections;

import org.testng.annotations.Test;

import com.goodworkalan.go.go.Go;

/**
 * Unit tests for mix root command.
 *
 * @author Alan Gutierrez
 */
public class MixTest {
    /** Test the welcome message. */
    @Test
    public void execute() {
        File home = new File(System.getProperty("user.home"));
        File directory = file(home, ".m2", "repository");
        Go.execute(Collections.singletonList(directory), "mix", "--working-directory=src/test/project", "make", "distribution");
    }

    /** Test the welcome message. */
    @Test
    public void dependencies() {
        File home = new File(System.getProperty("user.home"));
        File directory = file(home, ".m2", "repository");
        Go.execute(Collections.singletonList(directory), "mix", "--working-directory=src/test/project", "dependencies");
    }
}
