package tihonin.sergey.features.task

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import tihonin.sergey.databasemodels.task.TaskDTO
import tihonin.sergey.databasemodels.task.Tasks
import tihonin.sergey.databasemodels.task.mapTaskDTO
import tihonin.sergey.databasemodels.task.mapToFetchEventResponse
import tihonin.sergey.utils.TokenCheck
import java.time.LocalDate
import java.util.*

class TaskController(private val call: ApplicationCall) {
    suspend fun createTask() {
        val receive = call.receive<AddTaskRequest>()
        Tasks.insert(receive.mapTaskDTO())
        call.respond(HttpStatusCode.Created, message = "Задача успешно создана")
    }

    suspend fun deleteTask() {
        val receive = call.receive<FetchTaskRequest>()
        Tasks.deleteTaskByID(UUID.fromString(receive.taskid))
    }

    suspend fun editTask() {
        val receive = call.receive<FetchTaskResponse>()
        val task = Tasks.editTask(TaskDTO(
            taskid = UUID.fromString(receive.taskid),
            projectid = UUID.fromString(receive.projectid),
            taskname = receive.taskname,
            executor = UUID.fromString(receive.executor),
            datestart = LocalDate.parse(receive.datestart),
            dateend = LocalDate.parse(receive.dateend),
            iscomplete = receive.iscomplete
        ), UUID.fromString(receive.taskid))?.mapToFetchEventResponse()
        if (task!=null){
            call.respond(task)
        } else {
            call.respond(HttpStatusCode.NotFound, "Задача не найдена")
        }

    }

    suspend fun editIsComplete() {
        val receive = call.receive<EditIsCompleteRequest>()
        val task = Tasks.editIsComplete(receive.iscomplete, UUID.fromString(receive.taskid))?.mapToFetchEventResponse()
        if (task!=null){
            call.respond(task)
        } else {
            call.respond(HttpStatusCode.NotFound, "Задача не найдена")
        }
    }

    suspend fun fetchTask() {
        val receive = call.receive<FetchTaskRequest>()
        val task = Tasks.fetchTaskByID(UUID.fromString(receive.taskid))
        if (task != null) {
            call.respond(task.mapToFetchEventResponse())
        } else {
            call.respond(HttpStatusCode.NotFound, "Задача не найдена")
        }
    }
    suspend fun fetchAlTasks() {
        val projectid = call.request.queryParameters["projectid"]
        val tasks = Tasks.fetchAllTask(UUID.fromString(projectid))
        call.respond(tasks)
    }
}