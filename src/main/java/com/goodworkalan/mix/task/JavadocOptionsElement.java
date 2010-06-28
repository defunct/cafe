package com.goodworkalan.mix.task;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.goodworkalan.mix.MixError;
import com.goodworkalan.mix.builder.FindList;

// TODO Document.
public class JavadocOptionsElement<P, S> {
    // TODO Document.
    protected final P parent;
    
    // TODO Document.
    protected final FindList findList = new FindList();
    
    // TODO Document.
    protected List<URI> links = new ArrayList<URI>();

    // TODO Document.
    protected final Map<URI, File> offlineLinks = new LinkedHashMap<URI, File>();
    
    // TODO Document.
    protected String visibility;
    
    // TODO Document.
    protected final SelfServer<S> self;
    
    // TODO Document.
    protected JavadocEnd ending;
    
    // TODO Document.
    public JavadocOptionsElement(P parent, SelfServer<S> self, JavadocEnd ending) {
        this.parent = parent;
        this.self = self;
        this.ending = ending;
    }
    
    // TODO Document.
    public static <Parent> JavadocOptionsElement<Parent, JavadocOptionsElement<Parent, ?>> newJavadocOptionsElement(Parent parent, JavadocEnd ending) {
        SelfServer<JavadocOptionsElement<Parent, ?>> self = new SelfServer<JavadocOptionsElement<Parent, ?>>();
        JavadocOptionsElement<Parent, JavadocOptionsElement<Parent, ?>> element = new JavadocOptionsElement<Parent, JavadocOptionsElement<Parent,?>>(parent, self, ending);
        self.setSelf(element);
        return element;
    }
    
    // TODO Document.
    public S link(URI uri) {
        links.add(uri);
        return self.getSelf();
    }
    
    // TODO Document.
    public S offlineLink(URI uri, File file) {
        offlineLinks.put(uri, file);
        return self.getSelf();
    }
    
    // TODO Document.
    public S visibility(String visibility) {
        if ("|public|protected|package|private|".indexOf("|" + visibility + "|") == -1) {
            throw new MixError(JavadocOptionsElement.class, "invalid.visibility", visibility);
        }
        this.visibility = visibility;
        return self.getSelf();
    }
    
    // TODO Document.
    public P end() {
        ending.end(new JavadocConfiguration() {
            public void configure(Javadoc javadoc) {
                for (URI uri : links) {
                    javadoc.link(uri);
                }
                for (Map.Entry<URI, File> offlineLink : offlineLinks.entrySet()) {
                    javadoc.offlineLink(offlineLink.getKey(), offlineLink.getValue());
                }
                if (visibility != null) {
                    javadoc.visibility(visibility);
                }
            }
        });
        return parent;
    }
}
