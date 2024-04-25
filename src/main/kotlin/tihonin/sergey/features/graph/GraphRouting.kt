package tihonin.sergey.features.graph

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*


fun Application.configureGraphRouting() {

    routing {
        authenticate("auth-jwt") {
            get("/graph/edges") {
                val graphController = GraphController(call)
                graphController.fetchEdges()
            }
            post("/graph/edge/delete") {
                val graphController = GraphController(call)
                graphController.deleteEdge()
            }
            post("/graph/edge/create") {
                val graphController = GraphController(call)
                graphController.createEdge()
            }
        }
    }
}