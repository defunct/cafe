package com.goodworkalan.mix.github;

import com.goodworkalan.github4j.downloads.Download;
import com.goodworkalan.github4j.downloads.GitHubDownloadException;
import com.goodworkalan.github4j.downloads.GitHubDownloads;
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
            for (Download download : GitHubDownloads.getDownloads(github.login, project)) {
                env.io.out.println(nameOnly ? download.getFileName() : download.getUrl());
            }
        } catch (GitHubDownloadException e) {
            throw new GitHubError(DownloadsCommand.class, "io", e);
        }
    }
}
