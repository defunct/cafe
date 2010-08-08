package com.goodworkalan.cafe.task;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.goodworkalan.cafe.CafeCommand;
import com.goodworkalan.cafe.CafeError;
import com.goodworkalan.go.go.Argument;
import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.Commandable;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.library.Artifact;

// TODO Document.
@Command(name = "test-ng", parent = CafeCommand.class)
public class TestNGCommand implements Commandable {
    // TODO Document.
    public final Map<String, String> defines = new LinkedHashMap<String, String>();
    
    // TODO Document.
    public final List<File> classes = new ArrayList<File>();
    
    // TODO Document.
    public final List<Artifact> artifacts = new ArrayList<Artifact>();
    
    // TODO Document.
    @Argument
    public void addDefine(String define) {
        String[] pair = define.split(":");
        if (pair.length != 2) {
            throw new CafeError(TestNGCommand.class, "invalid.define", define);
        }
        defines.put(pair[0], pair[1]);
    }
    
    // TODO Document.
    @Argument
    public void addClasses(File file) {
        classes.add(file);
    }
    
    // TODO Document.
    @Argument
    public void addArtifact(Artifact artifact) {
        artifacts.add(artifact);
    }

    /**
     * A no-op implementation of execute.
     * 
     * @param env
     *            The execution environment.
     */
    public void execute(Environment env) {
        env.output(TestNGCommand.class, this);
    }
}
