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

@Command(cache = false)
public class SiblingsCommand implements Commandable {
    public void execute(Environment env) {
        env.debug("start", env.getCommandLine());
        Mix mix = env.get(Mix.class, 0);
        if (mix.isSiblings()) {
            Set<Object> seen = new HashSet<Object>();
            LinkedList<File> siblings = new LinkedList<File>();
            find(env, mix.getWorkingDirectory(), seen, siblings);
            ArgumentList mixArguments = new ArgumentList(env.arguments.get(0));
            mixArguments.removeArgument("mix:siblings");
            while (!siblings.isEmpty()) {
                File sibling = siblings.removeLast();
                env.debug("mix:sibling", sibling);
                mixArguments.replaceArgument("mix:working-directory", sibling.getAbsolutePath());
                List<String> commandLine = flatten("mix", mixArguments, env.getCommandLine(1), env.remaining);
                env.executor.run(env.io, commandLine);
            }
        }
    }
    
    private boolean isMixProject(File directory) {
        return directory.isDirectory() && file(directory, "src", "mix", "java").isDirectory();
    }

    private void find(Environment env, File workingDirectory, Set<Object> seen, List<File> directories) {
        File parent = workingDirectory.getParentFile();
        ArgumentList mixArguments = new ArgumentList(env.arguments.get(0));
        mixArguments.removeArgument("mix:siblings");
        mixArguments.replaceArgument("mix:working-directory", workingDirectory.getAbsolutePath());
        List<Include> dependencies = env.executor.run(new Ilk<List<Include>>() {}, InputOutput.nulls(), env.commands.get(0), mixArguments, "dependencies");
        for (Include dependency : dependencies) {
            Artifact artifact = dependency.getArtifact();
            Object unversionedKey = artifact.getUnversionedKey();
            if (!seen.contains(unversionedKey)) {
                seen.add(unversionedKey);
                File sibling = new File(parent, artifact.getName()).getAbsoluteFile();
                if (isMixProject(sibling)) {
                    mixArguments.replaceArgument("mix:working-directory", sibling.getAbsolutePath());
                    List<Production> productions = env.executor.run(new Ilk<List<Production>>() {}, InputOutput.nulls(), "mix", mixArguments, "produces");
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
