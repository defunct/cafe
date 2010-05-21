package com.goodworkalan.mix.github;

import com.goodworkalan.go.go.library.Artifact;

class Utility {
    /**
     * Get the GitHub project name from the group of the given
     * <code>artifact</code>.
     * 
     * @param artifact
     *            The artifact.
     * @return The GitHub project name.
     */
    public static String getGitHubProjectName(Artifact artifact) {
        String[] split = artifact.getGroup().split("\\.");
        return split[split.length - 1];
    }

    /**
     * Get the GitHub account name from the group of the given
     * <code>artifact</code>.
     * 
     * @param artifact
     *            The artifact.
     * @return The GitHub account name.
     */
    public static String getGitHubAccountName(Artifact artifact) {
        String[] split = artifact.getGroup().split("\\.");
        return split[split.length - 2];
    }
}
