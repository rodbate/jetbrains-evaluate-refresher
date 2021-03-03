package com.github.rodbate.intellijevaluaterefresher.services

import com.github.rodbate.intellijevaluaterefresher.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
