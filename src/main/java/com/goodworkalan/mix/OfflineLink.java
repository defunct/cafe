package com.goodworkalan.mix;

import java.io.File;
import java.net.URI;

class OfflineLink {
    private final File packages;
    
    private URI uri;
    
    public OfflineLink(File packages) {
        this.packages = packages;
    }
    
    public File getPackages() {
        return packages;
    }
    
    public void setUri(URI uri) {
        this.uri = uri;
    }
    
    public URI getUri() {
        return uri;
    }
}
