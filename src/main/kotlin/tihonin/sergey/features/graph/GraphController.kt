package tihonin.sergey.features.graph

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import tihonin.sergey.databasemodels.edges.EdgeDTO
import tihonin.sergey.databasemodels.edges.Edges
import java.util.*

class GraphController(private val call: ApplicationCall) {
    suspend fun fetchEdges() {
        val projectid = call.request.queryParameters["projectid"]
        val edges = Edges.fetchEdges(UUID.fromString(projectid))
        if (edges != null) {
            call.respond(edges)
        } else {
            call.respond(HttpStatusCode.NoContent, "Рёбер нет")
        }
    }
    suspend fun deleteEdge() {
        val receive = call.receive<EdgeDeleteRemote>()
        Edges.deleteEdge(receive.edgefrom, receive.edgeto)
        call.respond(HttpStatusCode.OK, "Ребро успешно удалено")
    }
    suspend fun createEdge() {
        val receive = call.receive<EdgesRemote>()
        val edge = Edges.fetchEdge(receive.edgefrom, receive.edgeto)
        if (edge==null){
            Edges.createEdge(EdgeDTO(
                UUID.fromString(receive.projectid),
                UUID.fromString(receive.taskid),
                receive.edgefrom,
                receive.edgeto
            ))
        }
        call.respond(HttpStatusCode.OK)
    }
}