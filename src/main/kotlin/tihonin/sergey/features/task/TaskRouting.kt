package tihonin.sergey.features.task

import io.ktor.server.application.*
import io.ktor.server.routing.*
import tihonin.sergey.features.project.ProjectController

fun Application.configureTaskRouting() {

    routing {
        post("/task/create") {
            val taskController = TaskController(call)
            taskController.createTask()
        }
        post("/task/edit") {
            val taskController = TaskController(call)
            taskController.deleteTask()
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