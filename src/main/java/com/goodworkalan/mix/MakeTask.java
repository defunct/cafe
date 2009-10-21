package com.goodworkalan.mix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
        Map<String, Recipe> buildQueue = new LinkedHashMap<String, Recipe>();
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
            buildQueue.put(name, recipe);
        }
        List<Recipe> build = new ArrayList<Recipe>(buildQueue.values());
        Collections.reverse(build);
        for (Recipe recipe : build) {
            for (Executable executable : recipe.getProgram()) {
                executable.execute(project, env);
            }
        }
    }
}
