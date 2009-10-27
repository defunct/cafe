package com.goodworkalan.mix.task;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.goodworkalan.go.go.Arguable;
import com.goodworkalan.go.go.Argument;
import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.Commandable;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.mix.MixError;
import com.goodworkalan.mix.MixCommand;

@Command(name = "test-ng", hidden = true, parent = MixCommand.class)
public class TestNGCommand implements Commandable {
    public final static class Arguments implements Arguable {
        public final Map<String, String> defines = new LinkedHashMap<String, String>();
        
        public final List<File> classes = new ArrayList<File>();
        
        public final List<Artifact> artifacts = new ArrayList<Artifact>();
        
        @Argument
        public void addDefine(String define) {
            String[] pair = define.split(":");
            if (pair.length != 2) {
                throw new MixError(0);
            }
            defines.put(pair[0], pair[1]);
        }
        
        @Argument
        public void addClasses(File file) {
            classes.add(file);
        }
        
        @Argument
        public void addArtifact(Artifact artifact) {
            artifacts.add(artifact);
        }
    }

    /**
     * A no-op implementation of execute.
     * 
     * @param env
     *            The execution environment.
     */
    public void execute(Environment env) {
    }
}
