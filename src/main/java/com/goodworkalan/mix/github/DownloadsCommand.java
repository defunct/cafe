package com.goodworkalan.mix.github;

import com.goodworkalan.github.downloads.Download;
import com.goodworkalan.github.downloads.GitHubDownloadException;
import com.goodworkalan.github.downloads.GitHubDownloads;
import com.goodworkalan.go.go.Argument;
import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.Commandable;
import com.goodworkalan.go.go.Environment;

/** FIXME Create a list of only artifacts, eh, why? */
@Command(parent = GitHubCommand.class)
public class DownloadsCommand implements Commandable {
    @Argument
    public String project;
    
    @Argument
    public boolean nameOnly;

    public void execute(Environment env) {
        GitHubConfig github = env.get(GitHubConfig.class, 1);
        try {
            GitHubDownloads downloads = new GitHubDownloads(github.login, github.token);
            for (Download download : downloads.getDownloads(project)) {
                env.io.out.println(nameOnly ? download.getFileName() : download.getUrl());
            }
        } catch (GitHubDownloadException e) {
            throw new GitHubError(DownloadsCommand.class, "io", e);
        }
    }
}
