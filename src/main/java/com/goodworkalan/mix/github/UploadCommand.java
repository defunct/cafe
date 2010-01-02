package com.goodworkalan.mix.github;

import java.io.File;

import com.goodworkalan.github.downloads.Download;
import com.goodworkalan.github.downloads.GitHubDownloadException;
import com.goodworkalan.github.downloads.GitHubDownloads;
import com.goodworkalan.go.go.Argument;
import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.Commandable;
import com.goodworkalan.go.go.Environment;

@Command(parent=GithubCommand.class)
public class UploadCommand implements Commandable {
    private File file;

    private GithubCommand.Config github;
    
    private String project;
    
    private String description = "";

    private String contentType = "application/octet-stream";
    
    @Argument
    // FIXME Maybe set enforces once and add allows many?
    public void addContentType(String contentType) {
        this.contentType = contentType;
    }
    
    private String fileName;
    
    @Argument
    public void addFileName(String fileName) {
        this.fileName = fileName;
    }

    @Argument
    public void addDescription(String description) {
        this.description = description;
    }

    @Argument
    public void addProject(String project) {
        this.project = project;
    }

    public void setGithub(GithubCommand.Config github) {
        this.github = github;
    }

    @Argument
    public void addFile(File file) {
        this.file = file;
    }

    /** Whether or not to replace an existing file with the same name. */
    private boolean replace;

    @Argument
    public void addReplace(boolean replace) {
        this.replace = replace;
    }
    
    public void execute(Environment env) {
        try {
            if (fileName == null) {
                fileName = file.getName();
            }
            GitHubDownloads downloads = new GitHubDownloads(github.getLogin(), github.getToken());
            for (Download download : downloads.getDownloads(project)) {
                if (download.getFileName().equals(fileName)) {
                    if (!replace) {
                        throw new GitHubError(UploadCommand.class, "exists", fileName);
                    }
                    download.delete();
                }
            }
            if (file != null) {
                env.debug("upload", project, file, description, contentType, fileName);
                downloads.upload(project, file, description, contentType, fileName);
            }
        } catch (GitHubDownloadException e) {
            throw new GitHubError(UploadCommand.class, "io", e);
        }
    }
}
