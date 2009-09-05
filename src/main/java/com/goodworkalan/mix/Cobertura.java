package com.goodworkalan.mix;

import java.util.ArrayList;
import java.util.List;

import com.goodworkalan.go.go.Artifact;

public class Cobertura {
    public void execute() {
        Artifact artifact = new Artifact("cobertura", "cobertura", "1.9rc1");
        List<String> arguments = new ArrayList<String>();
        arguments.add("java");
        ProcessBuilder process = new ProcessBuilder();
    }
}
