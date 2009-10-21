package com.goodworkalan.mix.task;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.goodworkalan.mix.FindList;
import com.goodworkalan.mix.MixError;

public class JavadocOptionsElement<P, S> {
    protected final P parent;
    
    protected final FindList findList = new FindList();
    
    protected List<URI> links = new ArrayList<URI>();

    protected final Map<URI, File> offlineLinks = new LinkedHashMap<URI, File>();
    
    protected String visibility;
    
    protected final SelfServer<S> self;
    
    protected JavadocEnd ending;
    
    public JavadocOptionsElement(P parent, SelfServer<S> self, JavadocEnd ending) {
        this.parent = parent;
        this.self = self;
        this.ending = ending;
    }
    
    public static <Parent> JavadocOptionsElement<Parent, JavadocOptionsElement<Parent, ?>> newJavadocOptionsElement(Parent parent, JavadocEnd ending) {
        SelfServer<JavadocOptionsElement<Parent, ?>> self = new SelfServer<JavadocOptionsElement<Parent, ?>>();
        JavadocOptionsElement<Parent, JavadocOptionsElement<Parent, ?>> element = new JavadocOptionsElement<Parent, JavadocOptionsElement<Parent,?>>(parent, self, ending);
        self.setSelf(element);
        return element;
    }
    
    public S link(URI uri) {
        links.add(uri);
        return self.getSelf();
    }
    
    public S offlineLink(URI uri, File file) {
        offlineLinks.put(uri, file);
        return self.getSelf();
    }
    
    
    public S visibility(String visibility) {
        if ("|public|protected|package|private|".indexOf("|" + visibility + "|") == -1) {
            throw new MixError(0);
        }
        this.visibility = visibility;
        return self.getSelf();
    }
    
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
