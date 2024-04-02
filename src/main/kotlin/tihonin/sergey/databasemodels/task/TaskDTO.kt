package tihonin.sergey.databasemodels.task

import tihonin.sergey.features.task.AddTaskRequest
import tihonin.sergey.features.task.FetchTaskResponse
import java.time.LocalDate
import java.util.*

class TaskDTO(
    val taskid: UUID?,
    val projectid: UUID,
    val taskname: String,
    val executor: UUID,
    val datestart: LocalDate,
    val dateend: LocalDate,
    val iscomplete: Boolean
)


fun AddTaskRequest.mapTaskDTO(): TaskDTO =
    TaskDTO(
        taskid = UUID.randomUUID(),
        projectid = UUID.fromString(projectid),
        taskname = taskname,
        executor = UUID.fromString(executor),
        datestart = LocalDate.parse(datestart),
        dateend = LocalDate.parse(dateend),
        iscomplete = false
    )

fun TaskDTO.mapToFetchEventResponse(): FetchTaskResponse =
    FetchTaskResponse(
        taskid = taskid.toString(),
        projectid = projectid.toString(),
        taskname = taskname,
        executor = executor.toString(),
        datestart = datestart.toString(),
        dateend = dateend.toString(),
        iscomplete = iscomplete
    )