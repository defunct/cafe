package com.goodworkalan.mix;

import java.io.File;

import com.goodworkalan.glob.Files;
import com.goodworkalan.glob.Find;
import com.goodworkalan.go.go.Argument;
import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.CommandPart;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.Task;

@Command(parent = MixTask.class)
public class InstallTask extends Task {
    private File libraryDirectory;
    
    private MixTask.Configuration configuration;
    
    public void setConfiguration(MixTask.Configuration configuration) {
        this.configuration = configuration;
    }
    
    @Argument
    public void addLibraryDirectory(File libraryDirectory) {
        this.libraryDirectory = libraryDirectory;
    }

    @Override
    public void execute(Environment env) {
        if (libraryDirectory == null) {
            if (System.getProperty("user.home") == null) {
                throw new MixException(0);
            }
            File home = new File(System.getProperty("user.home"));
            libraryDirectory = new File(home, ".m2/repository");
        }

        Project project = configuration.getProject();
        CommandPart mix = env.commandPart.getParent();
        for (String argument : env.commandPart.getRemaining()) {
            for (ArtifactSource source : project.getArtifactSource(argument)) {
                env.executor.execute(mix.extend("make", source.getRecipe()));
                Artifact artifact = source.getArtifact();
                Find find = new Find();
                find.include(artifact.getName() + "-" + artifact.getVersion() + "*.*");
                File sourceDirectory = new File(source.getDirectory(), source.getArtifact().getDirectoryPath());
                File outputDirectory = new File(libraryDirectory, source.getArtifact().getDirectoryPath());
                for (String fileName : find.find(sourceDirectory)) {
                    File destination = new File(outputDirectory, fileName);
                    File parent = destination.getParentFile();
                    if (!(parent.isDirectory() || parent.mkdirs())) {
                        throw new MixException(0);
                    }
                    Files.copy(new File(sourceDirectory, fileName), destination);
                }
            }
        }
    }
}
