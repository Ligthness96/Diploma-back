package tihonin.sergey.features.project

import kotlinx.serialization.Serializable

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
    val owner: String
)

@Serializable
data class ListProjectsResponseRemote(
    val projects: List<ProjectResponseRemote>
)

@Serializable
data class DeleteProjectReceiveRemote(
    val projectid: String,
)
