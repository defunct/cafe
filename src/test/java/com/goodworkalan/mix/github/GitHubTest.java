package com.goodworkalan.mix.github;

import java.io.File;
import java.util.Collections;

import org.testng.annotations.Test;

import com.goodworkalan.comfort.io.Files;
import com.goodworkalan.go.go.Go;

/**
 * Functional tests.
 *
 * @author Alan Gutierrez
 */
public class GitHubTest {
    /** Upload. */
    @Test
    public void upload() {
        File home = new File(System.getProperty("user.home"));
        File directory = Files.file(home, ".m2", "repository");
        Go.execute(Collections.singletonList(directory), "mix", "github", "upload", "--replace");
    }
}
