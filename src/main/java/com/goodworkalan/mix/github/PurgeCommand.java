package com.goodworkalan.mix.github;

import static com.goodworkalan.mix.Production.productionsByName;
import static com.goodworkalan.mix.Production.productionsByQualifiedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.goodworkalan.github4j.downloads.Download;
import com.goodworkalan.github4j.downloads.GitHubDownloadException;
import com.goodworkalan.github4j.downloads.GitHubDownloads;
import com.goodworkalan.go.go.Argument;
import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.Commandable;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.library.Artifact;
import com.goodworkalan.go.go.version.VersionSelector;
import com.goodworkalan.mix.Production;
import com.goodworkalan.mix.Project;

@Command(parent = GitHubCommand.class)
public class PurgeCommand implements Commandable {
    private final static Pattern GITHUB_GROUP = Pattern.compile("com\\.github\\.\\w[-\\w\\d]*\\.\\w[-\\w\\d]*");
    
    private final List<String> suffixes = new ArrayList<String>();
    
    @Argument
    public void addSuffix(String suffix) {
        suffixes.add(suffix);
    }

    private boolean isGitHubProject(Artifact artifact) {
        return GITHUB_GROUP.matcher(artifact.getGroup()).matches();
    }
    
    private String[] getRepository(Artifact artifact) {
        String[] split = artifact.getGroup().split("\\.");
        return new String[] { split[2], split[3] };
    }
    private final Pattern EXTRACT_VERSION = Pattern.compile("^(\\w[-_\\w\\d]*\\-)+((?:\\.?\\d+)+)(.*)$");
    
    public boolean matches(String artifactName, String stem, String suffix) {
        for (String match : suffixes) {
            String[] split = match.split("/");
            String testName = artifactName + "-";
            String testSuffix = "." + split[0];
            if (split.length == 2) {
                testSuffix = "-" + split[0] + "." + split[1];
            }
            if (stem.equals(testName)) {
                if (suffix.equals(testSuffix)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void purge(Environment env, Collection<Production> productions, String version) {
        GitHubConfig github = env.get(GitHubConfig.class, 1);
        for (Production production : productions) {
            Artifact artifact = production.getArtifact();
            env.debug("inspecting", artifact);
            if (!isGitHubProject(artifact)) {
                env.debug("notGitHub", artifact);
                continue;
            }
            String[] repository = getRepository(artifact);
            if (!github.login.equals(repository[0])) {
                env.debug("wrongLogin", artifact, repository[0]);
                continue;
            }
            Map<String, List<Download>> byVersion = new HashMap<String, List<Download>>();
            try {
                for (Download download : GitHubDownloads.getDownloads(github.login, repository[1])) {
                    String fileName = download.getFileName();
                    Matcher matcher = EXTRACT_VERSION.matcher(fileName);
                    if (matcher.matches() && matches(artifact.getName(), matcher.group(1), matcher.group(3))) {
                        String number = matcher.group(2);
                        List<Download> candidates = byVersion.get(number);
                        if (candidates == null) {
                            candidates = new ArrayList<Download>();
                            byVersion.put(number, candidates);
                        }
                        candidates.add(download);
                    }
                }
            } catch (GitHubDownloadException e) {
                throw new GitHubError(UploadCommand.class, "delete", e);
            }
            env.debug("candidates", byVersion.keySet());
            VersionSelector versionSelector = new VersionSelector(version);
            try {
                String selected;
                while ((selected = versionSelector.select(byVersion.keySet())) != null) {
                    for (Download download : byVersion.remove(selected)) {
                        env.debug("purging", download.getFileName());
                        env.io.out.println(download.getFileName());
                        download.delete(github.token);
                    }
                }
            } catch (GitHubDownloadException e) {
                throw new GitHubError(UploadCommand.class, "delete", e);
            }
        }
    }

    public void execute(Environment env) {
        if (suffixes.isEmpty()) {
            suffixes.addAll(Arrays.asList("jar", "sources/jar", "javadoc/jar", "dep"));
        }
        env.debug("purge", env.remaining);
        Project project = env.get(Project.class, 0);
        List<Production> productions = project.getProductions();
        for (String version : env.remaining) {
            String[] split = version.split("/");
            switch (split.length) {
            case 1:
                purge(env, productions, split[0]);
                break;
            case 2:
                purge(env, productionsByName(productions, split[0]), split[1]);
                break;
            case 3:
                purge(env, productionsByQualifiedName(productions, split[0], split[1]), split[2]);
                break;
            }
        }
    }
}
