package com.goodworkalan.mix;

import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.Commandable;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.spawn.Spawn;

/**
 * Executes a command in the project working directory.
 *
 * @author Alan Gutierrez
 */
@Command(parent = CafeCommand.class)
public class ExecuteCommand implements Commandable {
    /**
     * Execute the command in the remaining arguments in the working directory
     * of the project.
     * 
     * @param env
     *            The environment.
     */
    public void execute(Environment env) {
        long start = System.currentTimeMillis();
        try {
            env.debug("start", env.remaining);
            if (!env.remaining.isEmpty()) {
                Project project = env.get(Project.class, 0);
                Spawn spawn = new Spawn();
                spawn.setWorkingDirectory(project.getWorkingDirectory());
                env.exit(spawn.$(env.remaining).err(env.io.err).out(env.io.out).run().code);
            }
        } finally {
            env.debug("end", (System.currentTimeMillis() - start) / 1000.0);
        }
    }
}
