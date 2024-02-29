package tihonin.sergey.features.project

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import tihonin.sergey.databasemodels.participant.ParticipantDTO
import tihonin.sergey.databasemodels.participant.Participants
import tihonin.sergey.databasemodels.project.*
import tihonin.sergey.databasemodels.projecttree.ProjecttreeDTO
import tihonin.sergey.databasemodels.projecttree.Projecttrees
import tihonin.sergey.databasemodels.task.Tasks
import tihonin.sergey.features.projecttree.ProjecttreeRequest
import tihonin.sergey.utils.TokenCheck
import java.util.UUID

class ProjectController(private val call: ApplicationCall) {
    suspend fun createProject() {
        val token = call.request.headers["TOKEN"]
        if (TokenCheck.isTokenValid(token.orEmpty())) {
            val receive = call.receive<CreateProjectReceiveRemote>()
            val id = UUID.randomUUID()
            Projects.insert(
                ProjectDTO(
                    id,
                    receive.projectname,
                    UUID.fromString(receive.owner)
                )
            )
            Projecttrees.insert(
                ProjecttreeDTO(
                    id,
                    emptyList(),
                    emptyList()
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
                receive.owner,
                emptyList(),
                emptyList()
            )
            )
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Вы не авторизованы")
        }
    }

    suspend fun fetchProjectByID() {
        val token = call.request.headers["TOKEN"]
        if (TokenCheck.isTokenValid(token.orEmpty())) {
            val receive = call.receive<FetchByIDProjectReceiveRemote>()
            val project = Projects.fetchProject(UUID.fromString(receive.projectid))
            val tree = Projecttrees.fetchProjecttree(UUID.fromString(receive.projectid))
            if (project != null && tree != null){
                call.respond(ProjectResponseRemote(
                    project.projectid.toString(),
                    project.projectname,
                    project.owner.toString(),
                    tree.nodes,
                    tree.edges
                ))
            }
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Вы не авторизованы")
        }
    }
    suspend fun fetchProjectByOwner() {
        val token = call.request.headers["TOKEN"]
        if (TokenCheck.isTokenValid(token.orEmpty())) {
            val receive = call.receive<FetchByOwnerProjectReceiveRemote>()
            val project = Projects.fetchOwnProject(UUID.fromString(receive.owner))
            if (project != null){
                val tree = project.projectid?.let { Projecttrees.fetchProjecttree(it) }
                if (tree != null) {
                    call.respond(ProjectResponseRemote(
                        project.projectid.toString(),
                        project.projectname,
                        project.owner.toString(),
                        tree.nodes,
                        tree.edges
                    ))
                }
            }
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Вы не авторизованы")
        }
    }

    suspend fun fetchAllProjects() {
        val token = call.request.headers["TOKEN"]
        if (TokenCheck.isTokenValid(token.orEmpty())) {
            val receive = call.receive<FetchByParticipantProjectReceiveRemote>()
            val participants = Participants.fetchAllByUserID(UUID.fromString(receive.userid))
            val projects = mutableListOf<ProjectResponseRemote>()
            for(participant in participants){
                val project = Projects.fetchProject(UUID.fromString(participant.projectid))
                val projecttree = Projecttrees.fetchProjecttree(UUID.fromString(participant.projectid))
                if (project != null && projecttree != null) {
                    projects.add(
                        ProjectResponseRemote(
                            project.projectid.toString(),
                            project.projectname,
                            project.owner.toString(),
                            projecttree.nodes,
                            projecttree.edges

                        )
                    )
                }
            }
            call.respond(projects)
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Вы не авторизованы")
        }
    }


    suspend fun deleteProject() {
        val token = call.request.headers["TOKEN"]
        if (TokenCheck.isTokenValid(token.orEmpty())) {
            val receive = call.receive<DeleteProjectReceiveRemote>()
            Tasks.deleteAllTasks(UUID.fromString(receive.projectid))
            Projecttrees.deleteProjecttree(UUID.fromString(receive.projectid))
            Projects.deleteProject(UUID.fromString(receive.projectid))
            Participants.deleteAll(UUID.fromString(receive.projectid))
            call.respond(HttpStatusCode.OK, "Project was deleted")
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Вы не авторизованы")
        }
    }
}