package tihonin.sergey.features.task

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import tihonin.sergey.features.project.ProjectController

fun Application.configureTaskRouting() {

    routing {
        authenticate("auth-jwt") {
            post("/task/create") {
                val taskController = TaskController(call)
                taskController.createTask()
            }
            post("/task/delete") {
                val taskController = TaskController(call)
                taskController.deleteTask()
            }
            post("/task/edit") {
                val taskController = TaskController(call)
                taskController.editTask()
            }
            post("/task/edit/iscomplete") {
                val taskController = TaskController(call)
                taskController.editIsComplete()
            }
            get("/task/fetch") {
                val taskController = TaskController(call)
                taskController.fetchTask()
            }
            get("/task/fetch/all") {
                val taskController = TaskController(call)
                taskController.fetchAlTasks()
            }
        }
    }
}