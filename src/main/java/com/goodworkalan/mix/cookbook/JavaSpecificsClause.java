package com.goodworkalan.mix.cookbook;

import java.util.ArrayList;
import java.util.List;

import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.task.JavaSourceElement;
import com.goodworkalan.mix.task.JavacConfiguration;

/**
 * Used to winnow the options exposed to the user so that general settings 
 * can be set first, then main and test specific settings can be set. This
 * could as easily be done with lists, I think.
 *
 * @author Alan Gutierrez
 */
public class JavaSpecificsClause {
    protected final Builder builder;

    protected final List<JavacConfiguration> javacConfigurations = new ArrayList<JavacConfiguration>();

    protected final List<JavacConfiguration> mainJavacConfigurations = new ArrayList<JavacConfiguration>();

    protected final List<JavacConfiguration> testJavacConfigurations = new ArrayList<JavacConfiguration>();

    protected JavaSpecificsClause(Builder builder) {
        this.builder = builder;
    }

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
                    .artifact(artifact, excludes)
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
                    .artifact(artifact, excludes)
                    .end()
                .end()
            .end();
        return this;
    }

    public JavaSourceElement<JavaSpecificsClause> main() {
        moreSpecific();
        return new JavaSourceElement<JavaSpecificsClause>(this, mainJavacConfigurations);
    }
    
    public JavaSourceElement<JavaSpecificsClause> test() {
        moreSpecific();
        return new JavaSourceElement<JavaSpecificsClause>(this, testJavacConfigurations);
    }
    
    public Builder end() {
        return builder;
    }
}
