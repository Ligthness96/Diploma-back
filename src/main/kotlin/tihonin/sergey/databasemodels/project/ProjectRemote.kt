package tihonin.sergey.databasemodels.project

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class CreateProjectReceiveRemote(
    val projectname: String,
    val owner: String
)
@Serializable
data class CreateProjectResponseRemote(
    val projectid: String,
    val projectname: String,
    val owner: String,
    val nodes: List<Node>,
    val edges: List<Edge>
)

@Serializable
data class DeleteProjectReceiveRemote(
    val projectid: String,
)
