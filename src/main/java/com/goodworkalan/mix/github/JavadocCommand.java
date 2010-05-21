package com.goodworkalan.mix.github;

import static com.goodworkalan.comfort.io.Files.file;
import static com.goodworkalan.comfort.io.Files.unlink;
import static com.goodworkalan.mix.github.Utility.getGitHubAccountName;
import static com.goodworkalan.mix.github.Utility.getGitHubProjectName;

import java.io.File;

import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.Commandable;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.library.Artifact;
import com.goodworkalan.mix.Mix;
import com.goodworkalan.mix.Production;
import com.goodworkalan.mix.Project;
import com.goodworkalan.mix.Recipe;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.builder.Executable;
import com.goodworkalan.mix.task.Copy;
import com.goodworkalan.mix.task.Delete;
import com.goodworkalan.mix.task.Mkdirs;
import com.goodworkalan.spawn.Spawn;

/**
 * Publish Javadoc to GitHub in the gh-pages 
 *
 * @author Alan Gutierrez
 */
@Command(parent = GitHubCommand.class)
public class JavadocCommand implements Commandable {
    public void execute(Environment env) {
        // Build the documentation, we're expecting an apidocs and a devdocs target.
        env.executor.run(env.io, "mix", env.arguments.get(0), "make", "apidocs");
        env.executor.run(env.io, "mix", env.arguments.get(0), "make", "devdocs");
        Mix mix = env.get(Mix.class, 0);
        Project project = env.get(Project.class, 0);
        for (Production source : project.getProductions()) {
            File ghPages = file(mix.getWorkingDirectory(), "target", "gh-pages");
            if (ghPages.exists()) {
                unlink(ghPages);
            }
            // Create the various name parts so we can build command lines.
            Artifact artifact = source.getArtifact();
            String projectName = getGitHubProjectName(artifact);
            String accountName = getGitHubAccountName(artifact);
            String branch = artifact.getName().equals(projectName) ? "master" : artifact.getName();
            String prefix = projectName + "-";
            if (branch.startsWith(prefix)) {
                branch = branch.substring(prefix.length());
            }
            Spawn spawn = new Spawn();
            spawn.$("git", "clone", "git@github.com:" + accountName + "/" + projectName + ".git", ghPages.getAbsolutePath()).out(env.io.out).err(env.io.err).run();
            spawn.setWorkingDirectory(ghPages);
            spawn.$("git", "checkout", "gh-pages").out(env.io.out).err(env.io.err).run();
            File apidocs = file(ghPages, branch, artifact.getVersion(), "docs", "api");
            File devdocs = file(ghPages, branch, artifact.getVersion(), "docs", "dev");
            // FIXME Must be able to easily reuse.
            Builder builder = new Builder();
            builder
                .recipe("gh-pages")
                    .task(Mkdirs.class)
                        .directory(apidocs.getParentFile())
                        .end()
                    .task(Delete.class)
                        .file(apidocs).recurse(true)
                        .end()
                    .task(Copy.class)
                        .source(new File("target/apidocs")).end()
                        .output(apidocs)
                        .end()
                    .task(Delete.class)
                        .file(devdocs).recurse(true)
                        .end()
                    .task(Copy.class)
                        .source(new File("target/devdocs")).end()
                        .output(devdocs)
                        .end()
                    .end();
            Project subProject = builder.createProject(mix.getWorkingDirectory());
            Recipe recipe = subProject.getRecipe("gh-pages");
            // FIXME I want the project to MAKE.
            for (Executable executable : recipe.getProgram()) {
                executable.execute(env);
            }
            spawn.$("git", "add", ".").out(env.io.out).err(env.io.err).run();
            spawn.$("git", "commit", "-a", "-m", "Automated commit of generated Javadoc.").out(env.io.out).err(env.io.err).run();
            spawn.$("git", "push", "origin", "gh-pages").out(env.io.out).err(env.io.err).run();
            unlink(ghPages);
        }
    }
}