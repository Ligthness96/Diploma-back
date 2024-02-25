package tihonin.sergey.features.task

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import tihonin.sergey.databasemodels.task.TaskDTO
import tihonin.sergey.databasemodels.task.Tasks
import tihonin.sergey.databasemodels.task.mapTaskDTO
import tihonin.sergey.utils.TokenCheck
import java.util.*

class TaskController(private val call: ApplicationCall) {
    suspend fun createTask() {
        val token = call.request.headers["TOKEN"]
        if (TokenCheck.isTokenValid(token.orEmpty())) {
            val receive = call.receive<AddTaskRequest>()
            Tasks.insert(receive.mapTaskDTO())
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Вы не авторизованы")
        }
    }

    suspend fun deleteTask() {
        val token = call.request.headers["TOKEN"]
        if (TokenCheck.isTokenValid(token.orEmpty())) {
            val receive = call.receive<DeleteTaskRequest>()
            Tasks.deleteTaskByID(UUID.fromString(receive.taskid))
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Вы не авторизованы")
        }
    }

    suspend fun editTask() {
        val token = call.request.headers["TOKEN"]
        if (TokenCheck.isTokenValid(token.orEmpty())) {
            val receive = call.receive<DeleteTaskRequest>()
            Tasks.deleteTaskByID(UUID.fromString(receive.taskid))
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Вы не авторизованы")
        }
    }

}