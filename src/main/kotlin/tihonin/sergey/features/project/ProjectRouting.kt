package tihonin.sergey.features.project

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import tihonin.sergey.features.project.ProjectController

fun Application.configureProjectRouting() {

    routing {
        authenticate("auth-jwt") {
            post("/project/create") {
                val projectController = ProjectController(call)
                projectController.createProject()
            }
            post("/project/delete") {
                val projectController = ProjectController(call)
                projectController.deleteProject()
            }
            get("/project/fetch/id") {
                val projectController = ProjectController(call)
                projectController.fetchProjectByID()
            }
            get("/project/fetch/owner") {
                val projectController = ProjectController(call)
                projectController.fetchProjectByOwner()
            }

            get("/project/fetchall") {
                val projectController = ProjectController(call)
                projectController.fetchAllProjects()
            }
        }
    }
}