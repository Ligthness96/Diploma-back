package tihonin.sergey.features.invite

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.configureInviteRouting() {
    routing {
        authenticate("auth-jwt") {
            post("/invite/create") {
                val inviteController = InviteController(call)
                inviteController.createInvite()
            }
            post("/invite/join") {
                val inviteController = InviteController(call)
                inviteController.joinInvite()
            }
        }
    }
}