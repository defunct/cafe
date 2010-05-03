package com.goodworkalan.mix;

import java.io.File;
import java.util.Collections;

import org.testng.annotations.Test;

import com.goodworkalan.comfort.io.Files;
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
        File directory = Files.file(home, ".m2", "repository");
        Go.main(Collections.singletonList(directory), "mix", "--working-directory=src/test/project", "make", "distribution");
    }
}
