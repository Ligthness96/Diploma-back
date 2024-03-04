package tihonin.sergey.features.participant

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import tihonin.sergey.features.project.ProjectController

fun Application.configureParticipantsRouting() {

    routing {
        authenticate("auth-jwt") {
            get("/participant/fetch") {
                val participantsController = ParticipantsController(call)
                participantsController.fetchParticipants()
            }
        }
    }
}