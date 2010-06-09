package com.goodworkalan.mix;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.Commandable;
import com.goodworkalan.go.go.Environment;

/**
 * The make command take a single recipe name as an argument and executes the
 * recipe. If you want to execute multiple recipes, execute mix multiple times.
 * 
 * @author Alan Gutierrez
 */
@Command(parent = MixCommand.class)
public class MakeCommand implements Commandable {
    /**
     * Execute the make, building the single recipe given in the command line
     * arguments.
     * 
     * @param env
     *            The execution environment.
     */
    public void execute(Environment env) {
        if (env.remaining.isEmpty()) {
            throw new MixError(MakeCommand.class, "no.targets");
        }
        Project project = env.get(Project.class, 0);
        Mix mix = env.get(Mix.class, 0);
        LinkedList<String> buildQueue = new LinkedList<String>();
        for (String superRecipeName : env.remaining) {
            env.verbose("start", superRecipeName);
            LinkedList<String> recipeQueue = new LinkedList<String>();
            recipeQueue.add(superRecipeName);
            while (!recipeQueue.isEmpty()) {
                String name = recipeQueue.removeFirst();
                Recipe recipe = project.getRecipe(name);
                System.out.println(name);
                for (Dependency dependency : recipe.getDependencies()) {
                    for (String recipeName : dependency.getRecipeNames()) {
                        recipeQueue.addLast(recipeName);
                    }
                }
                buildQueue.addFirst(name);
            }
        }
        for (String recipeName : new LinkedHashSet<String>(buildQueue)) {
            Make make = new Make(recipeName);
            env.output(Make.class, make);
            Recipe recipe = project.getRecipe(recipeName);
            boolean build = recipe.getRebuilds().isEmpty();
            if (!build) {
                for (Iterator<Rebuild> rebuilds = recipe.getRebuilds().iterator(); !build && rebuilds.hasNext();) {
                    build = rebuilds.next().isDirty(mix);
                }
            }
            if (build) {
                env.verbose("dirty", recipeName);
                for (Commandable commandable : project.getRecipe(recipeName).getProgram()) {
                    commandable.execute(env);
                }
            } else {
                env.verbose("clean", recipeName);
            }
        }
    }
}
