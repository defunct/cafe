package com.goodworkalan.mix.github;

import com.goodworkalan.go.go.Argument;
import com.goodworkalan.go.go.Commandable;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.Output;
import com.goodworkalan.spawn.Exit;
import com.goodworkalan.spawn.Slurp;
import com.goodworkalan.spawn.Spawn;

/**
 * The GitHub command in the command heirarchy.
 * 
 * @author Alan Gutierrez
 */
public class GithubCommand implements Commandable {
    /**
     * The common configuration for all GitHub commands. 
     */
    public static class Config implements Output {
        /** The GitHub login. */
        private final String login;

        /** The GitHub API token. */
        private final String token;

        /**
         * Create a configuration with the given GitHub login and API token.
         * 
         * @param login
         *            The GitHub login.
         * @param token
         *            The GitHub API token.
         */
        public Config(String login, String token) {
            this.login = login;
            this.token = token;
        }

        /**
         * Get the GitHub login.
         * 
         * @return The GitHub login.
         */
        public String getLogin() {
            return login;
        }
        /**
         * Get the GitHub API token.
         * 
         * @return The GitHub API token.
         */
        public String getToken() {
            return token;
        }
    }

    /** The GitHub login. */
    private String login;
    
    /** The GitHub API token. */
    private String token;
    
    /** The configuration. */
    private Config config;
    
    /**
     * Set the GitHub login.
     * 
     * @param login
     *            The GitHub login.
     */
    @Argument
    public void addLogin(String login) {
        this.login = login;
    }

    /**
     * Set the GitHub API token.
     * 
     * @param token The GitHub API token.
     */
    @Argument
    public void addToken(String token) {
        this.token = token;
    }

    /**
     * Get the configuration.
     * 
     * @return The configuration.
     */
    public Config getConfig() {
        return config;
    }

    /**
     * Determine the login and API token if it has not been explicitly set.
     * 
     * @param env
     *            The execution environment.
     */
    public void execute(Environment env) {
        Spawn<Slurp, Slurp> spawn = Spawn.spawn();
        Exit<Slurp, Slurp> exit;
        if (login == null) {
            exit = spawn.execute("git", "config", "github.user");
            if (!exit.isSuccess()) {
                throw new GitHubError(GithubCommand.class, "no.login");
            }
            addLogin(exit.getStdOut().getLines().get(0));
        }
        if (token == null) {
            exit = spawn.execute("git", "config", "github.token");
            if (!exit.isSuccess()) {
                throw new GitHubError(GithubCommand.class, "no.token");
            }
            addToken(exit.getStdOut().getLines().get(0));
        }
        config = new Config(login, token);
    }
}
