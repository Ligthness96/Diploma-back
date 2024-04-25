package tihonin.sergey.features.project

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import tihonin.sergey.databasemodels.edges.Edges
import tihonin.sergey.databasemodels.participant.ParticipantDTO
import tihonin.sergey.databasemodels.participant.Participants
import tihonin.sergey.databasemodels.project.*
import tihonin.sergey.databasemodels.task.Tasks
import java.util.UUID


class ProjectController(private val call: ApplicationCall) {
    suspend fun createProject() {
        val receive = call.receive<CreateProjectReceiveRemote>()
        val id = UUID.randomUUID()
        Projects.insert(
            ProjectDTO(
                id,
                receive.projectname,
                UUID.fromString(receive.owner)
            )
        )
        Participants.insert(
            ParticipantDTO(
                id,
                UUID.fromString(receive.owner),
                admin = true
            )
        )
        call.respond(
            ProjectResponseRemote(
                id.toString(),
                receive.projectname,
                receive.owner
            )
        )
    }

    suspend fun fetchProjectByID() {
        val projectid = call.request.queryParameters["projectid"]
        val project = Projects.fetchProject(UUID.fromString(projectid))
        if (project != null){

            call.respond(ProjectResponseRemote(
                project.projectid.toString(),
                project.projectname,
                project.owner.toString()
            ))
        }
    }
    suspend fun fetchProjectByOwner() {
        val receive = call.receive<FetchByOwnerProjectReceiveRemote>()
        val project = Projects.fetchOwnProject(UUID.fromString(receive.owner))
        if (project != null){
            call.respond(ProjectResponseRemote(
                project.projectid.toString(),
                project.projectname,
                project.owner.toString()
            ))
        }
    }

    suspend fun fetchAllProjects() {
        val id = call.request.queryParameters["userid"]
        val participants = Participants.fetchAllByUserID(UUID.fromString(id))
        val projects = mutableListOf<ProjectResponseRemote>()
        for(participant in participants){
            val project = Projects.fetchProject(UUID.fromString(participant.projectid))
            if (project != null) {
                projects.add(
                    ProjectResponseRemote(
                        project.projectid.toString(),
                        project.projectname,
                        project.owner.toString()
                    )
                )
            }
        }
        call.respond(projects)
    }


    suspend fun deleteProject() {
        val receive = call.receive<DeleteProjectReceiveRemote>()
        Tasks.deleteAllTasks(UUID.fromString(receive.projectid))
        Edges.deleteEdgesWithProject(UUID.fromString(receive.projectid))
        Projects.deleteProject(UUID.fromString(receive.projectid))
        Participants.deleteAll(UUID.fromString(receive.projectid))
        call.respond(HttpStatusCode.OK, "Project was deleted")
    }
}