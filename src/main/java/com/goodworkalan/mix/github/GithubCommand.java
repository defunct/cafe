package com.goodworkalan.mix.github;

import com.goodworkalan.go.go.Argument;
import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.Commandable;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.mix.MixCommand;
import com.goodworkalan.spawn.Exit;
import com.goodworkalan.spawn.Spawn;

/**
 * The GitHub command in the command hierarchy.
 * 
 * @author Alan Gutierrez
 */
@Command(parent = MixCommand.class, name = "github")
public class GitHubCommand implements Commandable {
    /** The GitHub login. */
    @Argument
    public String login;
    
    /** The GitHub API token. */
    @Argument
    public String token;

    /**
     * Determine the login and API token if it has not been explicitly set.
     * 
     * @param env
     *            The execution environment.
     */
    public void execute(Environment env) {
        Spawn spawn = new Spawn();
        Exit exit;
        if (login == null) {
            exit = spawn.$$("git", "config", "github.user");
            if (!exit.isSuccess()) {
                throw new GitHubError(GitHubCommand.class, "no.login");
            }
            login = exit.out.get(0);
        }
        if (token == null) {
            exit = spawn.$$("git", "config", "github.token");
            if (!exit.isSuccess()) {
                throw new GitHubError(GitHubCommand.class, "no.token");
            }
            token = exit.out.get(0);
        }
        env.output(new GitHubConfig(login, token));
    }
}
