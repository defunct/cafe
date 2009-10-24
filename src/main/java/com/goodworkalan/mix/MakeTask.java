package com.goodworkalan.mix;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.Task;
import com.goodworkalan.mix.builder.Executable;

@Command(parent = MixTask.class)
public class MakeTask extends Task {
    private MixTask.Configuration configuration;
    
    public void setConfiguration(MixTask.Configuration configuration) {
        this.configuration = configuration;
    }
    
    @Override
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
            for (Executable executable : project.getRecipe(recipeName).getProgram()) {
                executable.execute(env, project, recipeName);
            }
        }
    }
}
