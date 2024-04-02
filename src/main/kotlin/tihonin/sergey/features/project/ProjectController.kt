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
    }

    suspend fun fetchProjectByID() {
        val projectid = call.request.queryParameters["projectid"]
        val project = Projects.fetchProject(UUID.fromString(projectid))
        val tree = Projecttrees.fetchProjecttree(UUID.fromString(projectid))
        if (project != null && tree != null){
            call.respond(ProjectResponseRemote(
                project.projectid.toString(),
                project.projectname,
                project.owner.toString(),
                tree.nodes,
                tree.edges
            ))
        }
    }
    suspend fun fetchProjectByOwner() {
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
    }

    suspend fun fetchAllProjects() {
        val id = call.request.queryParameters["userid"]
        val participants = Participants.fetchAllByUserID(UUID.fromString(id))
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
    }


    suspend fun deleteProject() {
        val receive = call.receive<DeleteProjectReceiveRemote>()
        Tasks.deleteAllTasks(UUID.fromString(receive.projectid))
        Projecttrees.deleteProjecttree(UUID.fromString(receive.projectid))
        Projects.deleteProject(UUID.fromString(receive.projectid))
        Participants.deleteAll(UUID.fromString(receive.projectid))
        call.respond(HttpStatusCode.OK, "Project was deleted")
    }
}