package com.goodworkalan.mix.builder;

import com.goodworkalan.go.go.Environment;
import com.goodworkalan.mix.Mix;
import com.goodworkalan.mix.Project;

public interface Executable {
    public void execute(Environment env, Mix mix, Project project, String recipeName);
}
