package com.goodworkalan.mix;

import java.io.File;
import java.util.Set;

import com.goodworkalan.go.go.Library;

public interface Dependency {
    public Set<File> getFiles(Library library);
}
