package com.goodworkalan.cafe.task;


// TODO Document.
public class JavacOptionsElement<P, S> {
    // TODO Document.
    protected final P parent;
    
    // TODO Document.
    protected Boolean verbose;

    // TODO Document.
    protected Boolean debug;
    
    // TODO Document.
    protected String source;
    
    // TODO Document.
    protected String target;
    
    // TODO Document.
    protected Boolean deprecation;
    
    // TODO Document.
    protected Boolean unchecked;
    
    // TODO Document.
    protected final SelfServer<S> self;

    // TODO Document.
    protected JavacEnd ending;
    
    // TODO Document.
    protected JavacOptionsElement(P parent, SelfServer<S> self, JavacEnd ending) {
        this.parent = parent;
        this.self = self;
        this.ending = ending;
    }
    
    // TODO Document.
    public static <Parent> JavacOptionsElement<Parent, JavacOptionsElement<Parent, ?>> newJavacOptionsElement(Parent parent, JavacEnd ending) {
        SelfServer<JavacOptionsElement<Parent, ?>> self = new SelfServer<JavacOptionsElement<Parent, ?>>();
        JavacOptionsElement<Parent, JavacOptionsElement<Parent, ?>> element = new JavacOptionsElement<Parent, JavacOptionsElement<Parent,?>>(parent, self, ending);
        self.setSelf(element);
        return element;
    }
    
    // TODO Document.
    public S verbose(boolean verbose) {
        this.verbose = verbose;
        return self.getSelf();
    }
    
    // TODO Document.
    public S debug(boolean debug) {
        this.debug = debug;
        return self.getSelf();
    }
    
    // TODO Document.
    public S source(String source) {
        this.source = source;
        return self.getSelf();
    }
    
    // TODO Document.
    public S target(String target) {
        this.target = target;
        return self.getSelf();
    }
    
    // TODO Document.
    public S deprecation(boolean deprecation) {
        this.deprecation = deprecation;
        return self.getSelf();
    }
    
    // TODO Document.
    public S unchecked(boolean unchecked) {
        this.unchecked = unchecked;
        return self.getSelf();
    }
    
    // TODO Document.
    public P end() {
        ending.end(new JavacConfiguration() {
            public void configure(Javac javac) {
                if (debug != null) {
                    javac.debug(debug);
                }
                if (source != null) {
                    javac.source(source);
                }
                if (deprecation != null) {
                    javac.deprecation(deprecation);
                }
                if (unchecked != null) {
                    javac.unchecked(unchecked);
                }
            }
        });
        return parent;
    }
}
