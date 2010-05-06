package com.goodworkalan.mix;

import java.util.List;

import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.Commandable;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.ilk.Ilk;

/**
 * Lists the artifacts produced by a project.
 *
 * @author Alan Gutierrez
 */
@Command(parent = MixCommand.class)
public class ProducesCommand implements Commandable {
    /**
     * Print the artifacts produced by the project.
     * 
     * @param env
     *            The environment.
     */
    public void execute(Environment env) {
        Project project = env.get(Project.class, 0);
        for (Production production : project.getProductions()) {
            env.io.out.println(production.getArtifact());
        }
        env.output(new Ilk<List<Production>>() {}, project.getProductions());
    }
}
