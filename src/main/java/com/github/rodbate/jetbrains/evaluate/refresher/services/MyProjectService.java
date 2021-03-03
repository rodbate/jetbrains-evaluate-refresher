package com.github.rodbate.jetbrains.evaluate.refresher.services;

import com.github.rodbate.jetbrains.evaluate.refresher.MyBundle;
import com.intellij.openapi.project.Project;

public class MyProjectService {

    private final Project project;


    public MyProjectService(Project project) {
        this.project = project;
        System.out.println(MyBundle.message("projectService", project.getName()));
    }
}
