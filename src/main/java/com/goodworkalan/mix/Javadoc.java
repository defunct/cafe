package com.goodworkalan.mix;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Javadoc {
    /** The mix wide configuration. */
    private MixTask.Configuration configuration;
    
    /**
     * The the arguments common to all Mix commands which are specified after
     * the mix command on the command line.
     * 
     * @param configuration
     *            The arguments common to all Mix commands.
     */
    public void setConfiguration(MixTask.Configuration configuration) {
        this.configuration = configuration;
    }

    public void execute() {
        Project project = configuration.getProject();
        List<String> arguments = new ArrayList<String>();
        arguments.add("-d");
        arguments.add("src/test/project/target/apidocs");
        for (File directory : project.getSourceDirectories()) {
            for (String source : project.getSources(directory)) {
                arguments.add(new File(directory, source).toString());
            }
        }
        try {
            com.sun.tools.javadoc.Main.execute(arguments.toArray(new String[arguments.size()]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
