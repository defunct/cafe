package com.goodworkalan.mix;

import static com.goodworkalan.comfort.io.Files.file;
import static com.goodworkalan.go.go.Environment.flatten;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.goodworkalan.go.go.ArgumentList;
import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.Commandable;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.InputOutput;
import com.goodworkalan.go.go.library.Artifact;
import com.goodworkalan.go.go.library.Include;
import com.goodworkalan.ilk.Ilk;

// TODO Document.
@Command(cache = false)
public class SiblingsCommand implements Commandable {
    // TODO Document.
    public void execute(Environment env) {
        env.debug("start", env.getCommandLine());
        Mix mix = env.get(Mix.class, 0);
        if (mix.isSiblings()) {
            Set<Object> seen = new HashSet<Object>();
            LinkedList<File> siblings = new LinkedList<File>();
            find(env, mix.getWorkingDirectory(), seen, siblings);
            ArgumentList mixArguments = new ArgumentList(env.arguments.get(0));
            mixArguments.removeArgument("cafe:siblings");
            while (!siblings.isEmpty()) {
                File sibling = siblings.removeLast();
                env.debug("sibling", sibling);
                mixArguments.replaceArgument("cafe:working-directory", sibling.getAbsolutePath());
                List<String> commandLine = flatten("cafe", mixArguments, env.getCommandLine(1), env.remaining);
                StringBuilder display = new StringBuilder();
                String separator = "";
                for (String argument : commandLine) {
                    display.append(separator).append(argument);
                    separator = " ";
                }
                env.io.out.println(display);
                env.executor.run(env.io, commandLine);
            }
            mixArguments = new ArgumentList(env.arguments.get(0));
            mixArguments.removeArgument("cafe:siblings");
            StringBuilder display = new StringBuilder();
            String separator = "";
            for (String argument : flatten("cafe", mixArguments, env.getCommandLine(1), env.remaining)) {
                display.append(separator).append(argument);
                separator = " ";
            }
            env.io.out.println(display);
        }
    }
    
    // TODO Document.
    private boolean isMixProject(File directory) {
        return directory.isDirectory() && file(directory, "src", "cafe", "java").isDirectory();
    }

    // TODO Document.
    private void find(Environment env, File workingDirectory, Set<Object> seen, List<File> directories) {
        File parent = workingDirectory.getParentFile();
        ArgumentList mixArguments = new ArgumentList(env.arguments.get(0));
        mixArguments.removeArgument("cafe:siblings");
        mixArguments.replaceArgument("cafe:working-directory", workingDirectory.getAbsolutePath());
        List<Include> dependencies = env.executor.run(new Ilk<List<Include>>() {}, InputOutput.nulls(), env.commands.get(0), mixArguments, "dependencies");
        for (Include dependency : dependencies) {
            Artifact artifact = dependency.getArtifact();
            Object unversionedKey = artifact.getUnversionedKey();
            if (!seen.contains(unversionedKey)) {
                seen.add(unversionedKey);
                File sibling = new File(parent, artifact.getName()).getAbsoluteFile();
                if (isMixProject(sibling)) {
                    mixArguments.replaceArgument("cafe:working-directory", sibling.getAbsolutePath());
                    List<Production> productions = env.executor.run(new Ilk<List<Production>>() {}, InputOutput.nulls(), "cafe", mixArguments, "produces");
                    for (Production production : productions) {
                        if (production.getArtifact().getUnversionedKey().equals(artifact.getUnversionedKey())) {
                            directories.add(sibling);
                            find(env, sibling, seen, directories);
                        }
                    }
                }
            }
        }
    }
}
