package com.goodworkalan.mix.junit;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.goodworkalan.comfort.io.Files;
import com.goodworkalan.comfort.io.Find;
import com.goodworkalan.go.go.Commandable;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.library.Artifact;
import com.goodworkalan.go.go.library.PathPart;
import com.goodworkalan.go.go.library.PathParts;
import com.goodworkalan.go.go.library.ResolutionPart;
import com.goodworkalan.mix.Dependency;
import com.goodworkalan.mix.Make;
import com.goodworkalan.mix.Mix;
import com.goodworkalan.mix.MixException;
import com.goodworkalan.mix.Project;
import com.goodworkalan.mix.builder.FindList;
import com.goodworkalan.mix.builder.FindStatement;
import com.goodworkalan.mix.builder.RecipeStatement;
import com.goodworkalan.spawn.Exit;
import com.goodworkalan.spawn.Spawn;

/**
 * Run JUnit tests using JUnit 4. The task will spawn a new java process using
 * classpath inherited from the recipe, but with JUnit 4 prepended to the
 * classpath.
 * 
 * @author Alan Gutierrez
 */
public class JUnit {
	/** The parent recipe statement in the builder language. */
	private final RecipeStatement recipeStatement;

	/** The filesystem queries that locate test to run. */
	private final FindList tests = new FindList();

	/** Whether to enable assertions. */
	private boolean enableAssertions;

	/**
	 * Create an JUnit task builder that adds an executable to the given recipe
	 * statement.
	 * 
	 * @param recipeStatement
	 *            The parent recipe statement in the builder language.
	 */
    public JUnit(RecipeStatement recipeStatement) {
        this.recipeStatement = recipeStatement;
    }

	/**
	 * Return a file find clause to specify the source files of the test classes
	 * to run. The source files are translated into their class names, by
	 * converting the file path relative to the source directory relative to the
	 * search directory.
	 * 
	 * @param directory
	 *            The source directory.
	 * @return A file find clause to specify the source files of the test class.
	 */
    public FindStatement<JUnit> source(File directory) {
        return new FindStatement<JUnit>(this, tests, directory);
    }
    
    /**
     * Whether to enable assertions when running the JUnit tests.
     * 
     * @return The JUnit task to continue specifying JUnit properties.
     */
    public JUnit enableAssertions() {
    	this.enableAssertions = true;
    	return this;
    }

	/**
	 * Terminate the JUnit task builder, adding an JUnit command to the recipe
	 * and returning the parent recipe statement to continue building the
	 * recipe.
	 * 
	 * @return The parent recipe statement in the builder language to continue
	 *         building the recipe.
	 */
    public RecipeStatement end() {
    	return recipeStatement.executable(new Commandable() {
            public void execute(Environment env) {
            	List<String> commandLine = new ArrayList<String>();
            
            	// Java executable.
            	commandLine.add("java");

            	// Classpath.
            	Collection<PathPart> parts = new ArrayList<PathPart>();
            	parts.add(new ResolutionPart(new Artifact("junit/junit/4.+8")));
                Project project = env.get(Project.class, 0);
                Make make = env.get(Make.class, 1);
                for (Dependency dependency : project.getRecipe(make.recipeName).getDependencies()) {
                    parts.addAll(dependency.getPathParts(project));
                }
                Set<File> classpath = PathParts.fileSet(env.library.resolve(parts));
                if (!classpath.isEmpty()) {
                	commandLine.add("-cp");
                	commandLine.add(Files.path(classpath));
                }
                
                // Enable assertions.
                if (enableAssertions) {
                	commandLine.add("-ea");
                }
                
                // JUnit test runner.
                commandLine.add("org.junit.runner.JUnitCore");
                
                // Test classes.
                List<String> testClasses = new ArrayList<String>();
                for (FindList.Entry entry : tests) {
                    Find find = entry.getFind();
                    if (!find.hasFilters()) {
                        find.include("**/*Test.java");
                        if (!find.hasFilters()) { 
                        throw new RuntimeException(entry.getDirectory().toString());
                        }
                    }
                    for (String className : find.find(entry.getDirectory())) {
                        className = className.replace("/", ".");
                        className = className.substring(0, className.length() - ".java".length());
                        testClasses.add(className);
                    }
                }

                commandLine.addAll(testClasses);
                
                
                env.debug(JUnit.class, "arguments", commandLine);

                Exit exit = new Spawn().$(commandLine).out(env.io.out).err(env.io.err).run();
                
                Mix mix = env.get(Mix.class, 0);
                if (!exit.isSuccess()) {
                    throw new MixException(JUnit.class, "failure", exit.code, mix.getWorkingDirectory(), commandLine);
                }
            }
        });
    }
}
