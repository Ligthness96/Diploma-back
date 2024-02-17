package tihonin.sergey.databasemodels.project

import io.ktor.server.application.*
import io.ktor.server.routing.*
import tihonin.sergey.features.login.LoginController

fun Application.configureProjectRouting() {

    routing {
        post("/project/create") {
            val projectController = ProjectController(call)
            projectController.createProject()
        }
        post("/project/delete") {
            val projectController = ProjectController(call)
            projectController.deleteProject()
        }

    }
}