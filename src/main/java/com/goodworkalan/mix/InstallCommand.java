package com.goodworkalan.mix;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.goodworkalan.comfort.io.Files;
import com.goodworkalan.comfort.io.Find;
import com.goodworkalan.go.go.Argument;
import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.Commandable;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.library.Artifact;

@Command(parent = MixCommand.class)
public class InstallCommand implements Commandable {
    /** The directory of the library in which to install. */
    private File libraryDirectory;

    /**
     * Add the directory of the library in which to install.
     * 
     * @param libraryDirectory
     *            The library directory.
     */
    @Argument
    public void addLibraryDirectory(File libraryDirectory) {
        this.libraryDirectory = libraryDirectory;
    }

    public void execute(Environment env) {
        if (libraryDirectory == null) {
            if (System.getProperty("user.home") == null) {
                throw new MixException(InstallCommand.class, "no.user.home");
            }
            File home = new File(System.getProperty("user.home"));
            libraryDirectory = new File(home, ".m2/repository");
        }

        Project project = env.get(Project.class, 0);
        List<ArtifactSource> artifactSources = new ArrayList<ArtifactSource>();
        if (env.remaining.isEmpty()) {
            artifactSources = project.getArtifactSources();
        } else {
            for (String argument : env.remaining) {
                artifactSources.addAll(project.getArtifactSources(argument));
            }
        }
        for (ArtifactSource source : artifactSources) {
            env.executor.run(env.io, "mix", env.arguments.get(0), "make", source.getRecipe()); 
            Artifact artifact = source.getArtifact();
            Find find = new Find();
            find.include(artifact.getName() + "-" + artifact.getVersion() + "*.*");
            File sourceDirectory = new File(source.getDirectory(), source.getArtifact().getDirectoryPath());
            File outputDirectory = new File(libraryDirectory, source.getArtifact().getDirectoryPath());
            for (String fileName : find.find(sourceDirectory)) {
                File destination = new File(outputDirectory, fileName);
                File parent = destination.getParentFile();
                if (!(parent.isDirectory() || parent.mkdirs())) {
                    throw new MixException(InstallCommand.class, "mkdirs", parent, libraryDirectory);
                }
                Files.copy(new File(sourceDirectory, fileName), destination);
            }
        }
    }
}
