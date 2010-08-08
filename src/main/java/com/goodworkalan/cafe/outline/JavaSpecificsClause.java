package com.goodworkalan.cafe.outline;

import java.util.ArrayList;
import java.util.List;

import com.goodworkalan.cafe.builder.Builder;
import com.goodworkalan.cafe.task.JavaSourceElement;
import com.goodworkalan.cafe.task.JavacConfiguration;

/**
 * Used to winnow the options exposed to the user so that general settings 
 * can be set first, then main and test specific settings can be set. This
 * could as easily be done with lists, I think.
 *
 * @author Alan Gutierrez
 */
public class JavaSpecificsClause {
    // TODO Document.
    protected final Builder builder;

    // TODO Document.
    protected final List<JavacConfiguration> javacConfigurations = new ArrayList<JavacConfiguration>();

    // TODO Document.
    protected final List<JavacConfiguration> mainJavacConfigurations = new ArrayList<JavacConfiguration>();

    // TODO Document.
    protected final List<JavacConfiguration> testJavacConfigurations = new ArrayList<JavacConfiguration>();

    // TODO Document.
    protected JavaSpecificsClause(Builder builder) {
        this.builder = builder;
    }

    // TODO Document.
    private void moreSpecific() {
        if (mainJavacConfigurations.isEmpty()) {
            mainJavacConfigurations.addAll(javacConfigurations);
        }
        if (testJavacConfigurations.isEmpty()) {
            testJavacConfigurations.addAll(javacConfigurations);
        }
    }

    /**
     * Add a single artifact dependency that will be used to build all of the
     * primary source code, and will be included in the list of production
     * dependencies. The given list of excludes specify which of the artifacts
     * in the given artifacts dependencies to exclude by artifact group and
     * name.
     * 
     * @param artifact
     *            The artifact to add to the project.
     * @param excludes
     *            The artifacts to exclude from the project.
     * @return This project builder to continue building the project.
     */
    public JavaSpecificsClause production(String artifact, String...excludes) {
        builder
            .recipe("production")
                .depends()
                    .include(artifact, excludes)
                    .end()
                .end()
            .end();
        return this;
    }

    /**
     * Add a single artifact dependency that will be used to build all of the
     * supporting source code and tests. The dependency will not be included in
     * the list of production dependencies. The given list of excludes specify
     * which of the artifacts in the given artifacts dependencies to exclude by
     * artifact group and name.
     * 
     * @param artifact
     *            The artifact to add to the project.
     * @param excludes
     *            The artifacts to exclude from the project.
     * @return This project builder to continue building the project.
     */
    public JavaSpecificsClause development(String artifact, String...excludes) {
        builder
            .recipe("development")
                .depends()
                    .include(artifact, excludes)
                    .end()
                .end()
            .end();
        return this;
    }

    // TODO Document.
    public JavaSourceElement<JavaSpecificsClause> main() {
        moreSpecific();
        return new JavaSourceElement<JavaSpecificsClause>(this, mainJavacConfigurations);
    }
    
    // TODO Document.
    public JavaSourceElement<JavaSpecificsClause> test() {
        moreSpecific();
        return new JavaSourceElement<JavaSpecificsClause>(this, testJavacConfigurations);
    }
    
    // TODO Document.
    public Builder end() {
        return builder;
    }
}
