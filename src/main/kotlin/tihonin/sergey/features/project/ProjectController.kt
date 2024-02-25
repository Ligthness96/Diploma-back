package tihonin.sergey.features.project

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import tihonin.sergey.databasemodels.project.*
import tihonin.sergey.databasemodels.projecttree.ProjecttreeDTO
import tihonin.sergey.databasemodels.projecttree.Projecttrees
import tihonin.sergey.databasemodels.task.Tasks
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
            call.respond(
                CreateProjectResponseRemote(
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

    suspend fun deleteProject() {
        val token = call.request.headers["TOKEN"]
        if (TokenCheck.isTokenValid(token.orEmpty())) {
            val receive = call.receive<DeleteProjectReceiveRemote>()
            Tasks.deleteAllTasks(UUID.fromString(receive.projectid))
            Projecttrees.deleteProjecttree(UUID.fromString(receive.projectid))
            Projects.deleteProject(UUID.fromString(receive.projectid))
            call.respond(HttpStatusCode.OK, "Project was deleted")
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Вы не авторизованы")
        }
    }
}