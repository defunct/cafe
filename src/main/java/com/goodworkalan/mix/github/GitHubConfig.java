package com.goodworkalan.mix.github;

/**
 * The common configuration for all GitHub commands.
 * 
 * @author Alan Gutierrez
 */
public class GitHubConfig {
    /** The GitHub login. */
    public final String login;

    /** The GitHub API token. */
    public final String token;

    /**
     * Create a configuration with the given GitHub login and API token.
     * 
     * @param login
     *            The GitHub login.
     * @param token
     *            The GitHub API token.
     */
    public GitHubConfig(String login, String token) {
        this.login = login;
        this.token = token;
    }
}