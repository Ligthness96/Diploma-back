package tihonin.sergey.features.projecttree

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*


fun Application.configureProjecttreeRouting() {

    routing {
        authenticate("auth-jwt") {
            post("/projecttree/edit") {
                val projecttreeController = ProjecttreeController(call)
                projecttreeController.editProjecttree()
            }
        }
    }
}