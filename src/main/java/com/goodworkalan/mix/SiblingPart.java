package com.goodworkalan.mix;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

import com.goodworkalan.comfort.io.Files;
import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.go.go.GoException;
import com.goodworkalan.go.go.Include;
import com.goodworkalan.go.go.Library;
import com.goodworkalan.go.go.PathPart;
import com.goodworkalan.go.go.ResolutionPart;

public class SiblingPart implements PathPart {
    private final MixCommand.Arguments arguments;
    
    private final File dir;
    
    private final Include include;
    
    public SiblingPart(MixCommand.Arguments arguments, File dir, Include include) {
        this.arguments = arguments;
        this.dir = dir;
        this.include = include;
    }

    public Collection<PathPart> expand(Library library, Collection<PathPart> expand) {
        PathPart resolution = new ResolutionPart(include);
        if (arguments.isSiblings()) {
            File sibling = new File(dir.getParentFile(), include.getArtifact().getName());
            if (sibling.isDirectory()) {
                File distribution = new File(sibling, Files.file("target", "distribution"));
                if (distribution.isDirectory()) {
                    Library siblingLibrary = new Library(distribution);
                    try {
                        return resolution.expand(siblingLibrary, expand);      
                    } catch (GoException e) {
                        if (e.getCode() != GoException.UNRESOLVED_ARTIFACT) {
                            throw e;
                        }
                    }
                }
            }
        }
        return resolution.expand(library, expand);
    }
    
    
    public File getFile() {
        throw new UnsupportedOperationException();
    }
    
    public URL getURL() throws MalformedURLException {
        throw new UnsupportedOperationException();
    }
    
    public Artifact getArtifact() {
        return include.getArtifact();
    }

    public Object getUnversionedKey() {
        return include.getArtifact().getKey().subList(0, 2);
    }
}
