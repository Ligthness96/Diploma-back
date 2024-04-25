package tihonin.sergey.features.task

import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.util.*

@Serializable
data class AddTaskRequest(
    val projectid: String,
    val taskname: String,
    val executor: String,
    val datestart: String,
    val dateend: String
)

@Serializable
data class FetchTaskRequest(
    val taskid: String
)

@Serializable
data class EditIsCompleteRequest(
    val taskid: String,
    val iscomplete: Boolean
)


@Serializable
data class FetchTaskResponse(
    val taskid: String,
    val projectid: String,
    val taskname: String,
    val executor: String,
    val datestart: String,
    val dateend: String,
    val iscomplete: Boolean,
)

@Serializable
data class FetchTasksByProjectidResponse(
    val tasks: List<FetchTaskResponse>
)