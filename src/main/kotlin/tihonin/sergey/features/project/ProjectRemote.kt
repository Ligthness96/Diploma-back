package tihonin.sergey.features.project

import kotlinx.serialization.Serializable
import tihonin.sergey.databasemodels.projecttree.Edge
import tihonin.sergey.databasemodels.projecttree.Node

@Serializable
data class CreateProjectReceiveRemote(
    val projectname: String,
    val owner: String
)
@Serializable
data class FetchByIDProjectReceiveRemote(
    val projectid: String
)
@Serializable
data class FetchByOwnerProjectReceiveRemote(
    val owner: String
)
@Serializable
data class FetchByParticipantProjectReceiveRemote(
    val userid: String
)
@Serializable
data class ProjectResponseRemote(
    val projectid: String,
    val projectname: String,
    val owner: String,
    val nodes: List<Node>,
    val edges: List<Edge>
)

@Serializable
data class ListProjectsResponseRemote(
    val projects: List<ProjectResponseRemote>
)

@Serializable
data class DeleteProjectReceiveRemote(
    val projectid: String,
)
