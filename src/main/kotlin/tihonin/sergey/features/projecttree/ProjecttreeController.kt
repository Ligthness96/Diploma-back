package tihonin.sergey.features.projecttree

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.*
import tihonin.sergey.databasemodels.projecttree.ProjecttreeDTO
import tihonin.sergey.databasemodels.projecttree.Projecttrees
import tihonin.sergey.utils.TokenCheck
import java.util.*

class ProjecttreeController(private val call: ApplicationCall) {
    suspend fun editProjecttree() {
        val receive = call.receive<ProjecttreeRequest>()
        Projecttrees.editProjecttree(
            ProjecttreeDTO(
                projectid = UUID.fromString(receive.projectid),
                edges = receive.edges,
                nodes = receive.nodes
            ), UUID.fromString(receive.projectid)
        )
        val tree = Projecttrees.fetchProjecttree(UUID.fromString(receive.projectid))
        if (tree != null) {
            call.respond(ProjecttreeRequest(
                tree.projectid.toString(),
                tree.nodes,
                tree.edges
            ))
        }
    }
}