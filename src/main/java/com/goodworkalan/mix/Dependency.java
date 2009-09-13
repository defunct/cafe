package com.goodworkalan.mix;

import java.io.File;
import java.util.Set;

import com.goodworkalan.go.go.CommandPart;
import com.goodworkalan.go.go.Executor;
import com.goodworkalan.go.go.Library;

// FIXME This belongs in Jav-a-Go-Go. A means to resolve such dependencies,
// and to add file paths and jars. So, dir, jar and artifact, expand.
public interface Dependency {
    public Set<File> getFiles(Project project, Library library);
    
    public void make(Executor executor, CommandPart mix);
}
