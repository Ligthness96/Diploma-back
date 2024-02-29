package tihonin.sergey.features.projecttree

import io.ktor.server.application.*
import io.ktor.server.routing.*
import tihonin.sergey.features.project.ProjectController

fun Application.configureProjecttreeRouting() {

    routing {
        post("/projecttree/edit") {
            val projecttreeController = ProjecttreeController(call)
            projecttreeController.editProjecttree()
        }
    }
}