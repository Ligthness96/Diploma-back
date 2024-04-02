package tihonin.sergey.features.participant

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.configureParticipantsRouting() {

    routing {
        authenticate("auth-jwt") {
            get("/participants/fetch") {
                val participantsController = ParticipantsController(call)
                participantsController.fetchParticipants()
            }
        }
    }
}