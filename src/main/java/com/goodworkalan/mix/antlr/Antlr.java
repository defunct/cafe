package com.goodworkalan.mix.antlr;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
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
import com.goodworkalan.mix.Mix;
import com.goodworkalan.mix.MixException;
import com.goodworkalan.mix.builder.RecipeStatement;
import com.goodworkalan.mix.task.FindElement;
import com.goodworkalan.mix.task.FindList;
import com.goodworkalan.mix.task.Javac;
import com.goodworkalan.spawn.Exit;
import com.goodworkalan.spawn.Spawn;

/**
 * Invoke antler on a grammar.
 * 
 * @author Alan Gutierrez
 */
public class Antlr {
    /** The parent recipe builder. */
	private final RecipeStatement recipeStatement;
	
	/** Whether all rules will generate tracing information. */
	private boolean diagnostic;
	
	/** Whether all rules will generate tracing information. */
	private boolean trace;
	
	/** Whether parser rules will generate tracing information. */
	private boolean traceParser;
	
	/** Whether lexer rules will generate tracing information. */
	private boolean traceLexer;
	
	/** Whether tree parser rules will generate tracing information. */
	private boolean traceTreeParser;
	
	/** The output directory where all output is generated. */
	private File output;

	/** The list of source files. */
	private FindList grammars = new FindList();

	/**
	 * Create an Antlr statement that adds an executable to the given recipe
	 * statement.
	 * 
	 * @param recipeStatement
	 */
	public Antlr(RecipeStatement recipeStatement) {
	    this.recipeStatement = recipeStatement;
	}
	
	/**
	 * Set the output directory where all output is generated.
	 * 
	 * @param output The output directory.
	 * 
	 * @return  This Antlr clause to continue specifying Antler properties.
	 */
	public Antlr output(File output) {
		this.output = output;
		return this;
	}

	/**
	 * Indicate that a text file will be generated containing debugging information
	 * for the grammars.
	 * 
	 * @return  This Antlr clause to continue specifying Antler properties.
	 */
	public Antlr diagnostic() {
		this.diagnostic = true;
		return this;
	}

	/**
	 * Indicate that all rules will generate tracing information.
	 * 
	 * @return This Antlr clause to continue specifying Antler properties.
	 */
    public Antlr trace() {
    	this.trace = true;
    	return this;
    }

	/**
	 * Indicate that lexer rules will generate tracing information.
	 * 
	 * @return This Antlr clause to continue specifying Antler properties.
	 */
    public Antlr traceLexer() {
    	this.traceLexer = true;
    	return this;
    }

	/**
	 * Indicate that parser rules will generate tracing information.
	 * 
	 * @return This Antlr clause to continue specifying Antler properties.
	 */
    public Antlr traceParser() {
    	this.traceParser = true;
    	return this;
    }
    /**
	 * Indicate that tree parser rules will generate tracing information.
	 * 
	 * @return This Antlr clause to continue specifying Antler properties.
	 */
    public Antlr traceTreeParser() {
    	this.traceTreeParser = true;
    	return this;
    }

	/**
	 * Start a source clause that will add the source files in the given
	 * directory to the Antlr input.
	 * 
	 * @param directory
	 *            The directory in which to look for source files.
	 * @return A find clause to specify include and exclude patterns for the
	 *         source directory.
	 */
    public FindElement<Antlr> source(File directory) {
        return new FindElement<Antlr>(this, grammars, directory);
    }

	/**
	 * Terminate the Antlr task builder, adding an Antlr command to the recipe
	 * and returning the parent recipe statement to continue building the
	 * recipe.
	 * 
	 * @return The parent recipe statement in the builder language to continue
	 *         building the recipe.
	 */
    public RecipeStatement end() {
    	return recipeStatement.executable(new Commandable() {
			public void execute(Environment env) {
				Artifact artifact = new Artifact("antlr/antlr/2.7.6");
				
				Set<File> classpath = PathParts.fileSet(env.library.resolve(Collections.<PathPart>singleton(new ResolutionPart(artifact))));

				List<String> arguments = new ArrayList<String>();

                arguments.add("java");
                arguments.add("-classpath");
                arguments.add(Files.path(classpath));
                arguments.add("antlr.Tool");
             
                if (output != null) {
                	arguments.add("-o");
                	arguments.add(output.getAbsolutePath());
                }
                if (diagnostic) {
                	arguments.add("-diagnostic");
                }
                if (trace) {
                	arguments.add("-trace");
                }
                if (traceLexer) {
                	arguments.add("-traceLexer");
                }
                if (traceParser) {
                	arguments.add("-traceParser");
                }
                if (traceTreeParser) {
                	arguments.add("-traceTreeParser");
                }
                
                Spawn spawn = new Spawn();

                Mix mix = env.get(Mix.class, 0);
                for (FindList.Entry entry : grammars) {
                    Find find = entry.getFind();
                    if (!find.hasFilters()) {
                        find.include("**/*.java");
                    }
                    File directory = mix.relativize(entry.getDirectory());
                    env.debug(Javac.class, "sources", directory);
                    for (String fileName : find.find(directory)) {
                    	List<String> args = new ArrayList<String>(arguments);
                    	args.add(new File(directory, fileName).toString());
                    	env.debug(Antlr.class, "arguments", args);
                    	Exit exit = spawn.$(args).out(env.io.out).err(env.io.err).run();
                    	if (!exit.isSuccess()) {
                    		throw new MixException(Antlr.class, "invoke", mix.getWorkingDirectory(), arguments);
                    	}
                    }
                }
			}
		});
    }
}
