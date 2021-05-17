package com.github.noblemathews.protobuggy.services

import com.github.noblemathews.protobuggy.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
