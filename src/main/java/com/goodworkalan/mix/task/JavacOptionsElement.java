package com.goodworkalan.mix.task;


public class JavacOptionsElement<P, S> {
    protected final P parent;
    
    protected Boolean verbose;

    protected Boolean debug;
    
    /** Fork if true. */
    protected Boolean fork;
    
    protected String source;
    
    protected String target;
    
    protected Boolean deprecation;
    
    protected Boolean unchecked;
    
    protected final SelfServer<S> self;

    protected JavacEnd ending;
    
    protected JavacOptionsElement(P parent, SelfServer<S> self, JavacEnd ending) {
        this.parent = parent;
        this.self = self;
        this.ending = ending;
    }
    
    public static <Parent> JavacOptionsElement<Parent, JavacOptionsElement<Parent, ?>> newJavacOptionsElement(Parent parent, JavacEnd ending) {
        SelfServer<JavacOptionsElement<Parent, ?>> self = new SelfServer<JavacOptionsElement<Parent, ?>>();
        JavacOptionsElement<Parent, JavacOptionsElement<Parent, ?>> element = new JavacOptionsElement<Parent, JavacOptionsElement<Parent,?>>(parent, self, ending);
        self.setSelf(element);
        return element;
    }
    
    public S verbose(boolean verbose) {
        this.verbose = verbose;
        return self.getSelf();
    }
    
    public S debug(boolean debug) {
        this.debug = debug;
        return self.getSelf();
    }
    
    public S fork(boolean fork) {
        this.fork = fork;
        return self.getSelf();
    }
    
    public S source(String source) {
        this.source = source;
        return self.getSelf();
    }
    
    public S target(String target) {
        this.target = target;
        return self.getSelf();
    }
    
    public S deprecation(boolean deprecation) {
        this.deprecation = deprecation;
        return self.getSelf();
    }
    
    public S unchecked(boolean unchecked) {
        this.unchecked = unchecked;
        return self.getSelf();
    }
    
    public P end() {
        ending.end(new JavacConfiguration() {
            public void configure(Javac javac) {
                if (debug != null) {
                    javac.debug(debug);
                }
                if (fork != null) {
                    javac.fork(fork);
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
