package com.goodworkalan.mix;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.Commandable;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.mix.builder.Executable;
import com.goodworkalan.mix.builder.Rebuild;

/**
 * The make command take a single recipe name as an argument and executes the
 * recipe. If you want to execute multiple recipes, execute mix multiple times.
 * 
 * @author Alan Gutierrez
 */
@Command(parent = MixCommand.class)
public class MakeCommand implements Commandable {
    /** The mix configuration. */
    private MixCommand.Configuration configuration;
    
    public void setConfiguration(MixCommand.Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Execute the make, building the single recipe given in the command line
     * arguments.
     * 
     * @param env
     *            The execution environment.
     */
    public void execute(Environment env) {
        List<String> remaining = env.part.getRemaining();
        if (remaining.isEmpty()) {
            throw new MixException(0);
        }
        Project project = configuration.getProject();
        LinkedList<String> buildQueue = new LinkedList<String>();
        LinkedList<String> recipeQueue = new LinkedList<String>();
        recipeQueue.add(remaining.get(0));
        while (!recipeQueue.isEmpty()) {
            String name = recipeQueue.removeFirst();
            Recipe recipe = project.getRecipe(name);
            for (Dependency dependency : recipe.getDependencies()) {
                for (String recipeName : dependency.getRecipes(project)) {
                    recipeQueue.addLast(recipeName);
                }
            }
            buildQueue.addFirst(name);
        }
        for (String recipeName : new LinkedHashSet<String>(buildQueue)) {
            Recipe recipe = project.getRecipe(recipeName);
            boolean build = recipe.getRebuilds().isEmpty();
            if (!build) {
                for (Iterator<Rebuild> rebuilds = recipe.getRebuilds().iterator(); !build && rebuilds.hasNext();) {
                    build = rebuilds.next().isDirty();
                }
            }
            if (build) {
                for (Executable executable : project.getRecipe(recipeName).getProgram()) {
                    executable.execute(env, project, recipeName);
                }
            }
        }
    }
}
