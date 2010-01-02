package com.goodworkalan.mix.github;

import com.goodworkalan.github.downloads.Download;
import com.goodworkalan.github.downloads.GitHubDownloadException;
import com.goodworkalan.github.downloads.GitHubDownloads;
import com.goodworkalan.go.go.Argument;
import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.Commandable;
import com.goodworkalan.go.go.Environment;

/** FIXME Create a list of only artifacts, eh, why? */
@Command(parent=GithubCommand.class)
public class DownloadsCommand implements Commandable {
    private GithubCommand.Config github;
    
    private String project;
    
    private boolean nameOnly;

    @Argument
    public void addProject(String project) {
        this.project = project;
    }
    
    @Argument
    public void addNameOnly(boolean nameOnly) {
        this.nameOnly = nameOnly;
    }

    public void setGithub(GithubCommand.Config github) {
        this.github = github;
    }

    public void execute(Environment env) {
        try {
            GitHubDownloads downloads = new GitHubDownloads(github.getLogin(), github.getToken());
            for (Download download : downloads.getDownloads(project)) {
                env.io.out.println(nameOnly ? download.getFileName() : download.getUrl());
            }
        } catch (GitHubDownloadException e) {
            throw new GitHubError(DownloadsCommand.class, "io", e);
        }
    }
}
