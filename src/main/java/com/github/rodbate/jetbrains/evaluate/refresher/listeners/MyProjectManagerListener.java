package com.github.rodbate.jetbrains.evaluate.refresher.listeners;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import org.jetbrains.annotations.NotNull;

public class MyProjectManagerListener implements ProjectManagerListener {

    @Override
    public void projectOpened(@NotNull Project project) {
        System.out.println("projectOpened -> " + project.getName());
    }
}
