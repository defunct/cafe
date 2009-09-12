package com.goodworkalan.mix;

import java.util.LinkedHashMap;
import java.util.Map;

public class RecipeElement {
    private final Builder builder;
    
    private final String name;
    
    private final Map<String, Recipe> recipes;
    
    private final Map<String, Command> commands = new LinkedHashMap<String, Command>();
    
    private final Map<String, Dependency> dependencies = new LinkedHashMap<String, Dependency>(); 

    public RecipeElement(Builder builder, Map<String, Recipe> recipes, String name) {
        this.builder = builder;
        this.recipes = recipes;
        this.name = name;
    }

    /**
     * Call the configure method of the given recipe module passing this recipe
     * element.
     * 
     * @param recipeModule
     *            A pre-defined recipe.
     * @return This recipe language element in order to continue to specify
     *         recipe properties.
     */
    public RecipeElement apply(RecipeModule recipeModule) {
        recipeModule.configure(this);
        return this;
    }

    public CommandElement<RecipeElement> command(String name) {
        Command command = new Command(name);
        commands.put(name, command);
        return new CommandElement<RecipeElement>(this, command);
    }

    /**
     * Specify the dependencies for this recipe.
     * 
     * @return A depends language element to specify project dependencies.
     */
    public DependsElement depends() {
        return new DependsElement(this, dependencies);
    }
    
    public Builder end() {
        recipes.put(name, new Recipe(commands, dependencies));
        return builder;
    }
}
