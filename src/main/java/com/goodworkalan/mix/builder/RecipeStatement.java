package com.goodworkalan.mix.builder;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.goodworkalan.go.go.Commandable;
import com.goodworkalan.mix.Dependency;
import com.goodworkalan.mix.MixException;
import com.goodworkalan.mix.Production;
import com.goodworkalan.mix.Rebuild;
import com.goodworkalan.mix.Recipe;

// TODO Document.
public class RecipeStatement {
    // TODO Document.
    private final Builder builder;
    
    // TODO Document.
    private final String name;
    
    // TODO Document.
    private final Map<String, Recipe> recipes;
    
    // TODO Document.
    private final List<Commandable> program = new ArrayList<Commandable>();
    
    // TODO Document.
    private final Map<List<String>, Dependency> dependencies = new LinkedHashMap<List<String>, Dependency>();
    
    // TODO Document.
    private final Set<File> classes = new LinkedHashSet<File>();
    
    // TODO Document.
    private final Map<List<String>, Production> artifacts; 
    
    // TODO Document.
    private final List<Rebuild> rebuilds = new ArrayList<Rebuild>();
    
    // TODO Document.
    public RecipeStatement(Builder builder, Map<List<String>, Production> artifacts, Map<String, Recipe> recipes, String name) {
        this.builder = builder;
        this.recipes = recipes;
        this.artifacts = artifacts;
        this.name = name;
        
        Recipe existing = recipes.get(name);
        if (existing != null) {
            program.addAll(existing.getProgram());
            classes.addAll(existing.getClasses());
            dependencies.putAll(existing.getDependencyMap());
            rebuilds.addAll(existing.getRebuilds());
        }
    }
    
    // TODO Document.
    public RecipeStatement make(String recipe) {
        return this;
    }

    /**
     * Return element in the domain-specific language that specifies a rebuild
     * if dirty test that compares a set of source files to a set of output
     * files and skips running the recipe if all the source is older than all
     * the output.
     * 
     * @return A rebuild language element to specify rebuild if dirty source and
     *         output files.
     */
    public RebuildClause rebuild() {
        return new RebuildClause(this, rebuilds);
    }
    
    /**
     * Add the given executable to the recipe.
     * 
     * @param executable
     *            The executable to add.
     * @return This recipe builder to continue building the recipe.
     */
    public RecipeStatement executable(Commandable executable) {
        program.add(executable);
        return this;
    }
    
    /** Here's an idea on reuse and extension. */
    public RecipeStatement reset() {
        return this;
    }

	/**
	 * Create an instance of the given task builder class using this recipe
	 * builder as a parent builder and return the newly created task builder
	 * instance in order to build the task.
	 * <p>
	 * A task builder is a duck typed class, a rare thing for Java. It is a
	 * class that defines a constructor with a single recipe builder parameter
	 * and defines a method named <code>end</code> that returns the recipe
	 * builder that was given to the constructor. The end method will add an
	 * executable to the recipe that will perform the task.
	 * <p>
	 * The methods defined by the task configure the executable. They generally
	 * return the task builder to permit method chaining, but they could also
	 * return sub builders. Your code completing IDE should explain the builder
	 * as you go along, since the builder will only expose configuration
	 * methods.
	 * <p>
	 * Normal usage of the task builder is to call the configuration methods in
	 * a method chain. After the end method has been called on the builder, no
	 * more configuration methods should be called. Builders do not have to
	 * enforce this with checking, since it is a simple enough rule to follow,
	 * and in normal use, the reference to the task builder is not available
	 * after the end method is called. The task builder is rarely assigned to a
	 * variable, but is instead called in a method chain.
	 * <p>
	 * You might need to loop through a collection and call a configuration
	 * method repeatedly, in which case you will need to assign the task builder
	 * to a variable, but don't touch it again after you've called the end
	 * method.
	 * <p>
	 * Task builders do not need to enforce this rule, since it is natural to
	 * follow. If someone is bitten by this, you can point them to this patch of
	 * documentation and ask that they not do it again.
	 * 
	 * @param <T>
	 *            The task type.
	 * @param taskClass
	 *            The task class.
	 * @return An instance of the task class.
	 */
    public <T> T task(final Class<T> taskClass) {
        try {
            return taskClass.getConstructor(RecipeStatement.class).newInstance(RecipeStatement.this);
        } catch (Exception e) {
            throw new MixException(Builder.class, "cannot.create.task", taskClass);
        }
    }

    /**
     * Specify the dependencies for this recipe.
     * 
     * @return A depends language element to specify project dependencies.
     */
    public DependsClause<RecipeStatement> depends() {
        return new DependsClause<RecipeStatement>(this, dependencies);
    }
    
    // TODO Document.
    public ProducesClause produces() {
        return new ProducesClause(this, name, classes, artifacts);
    }

    /**
     * Terminate the recipe statement and record the recipe.
     * 
     * @return The builder element in order to continue specifying recipes.
     */
    public Builder end() {
        recipes.put(name, new Recipe(program, dependencies, classes, rebuilds));
        return builder;
    }
}
