package tihonin.sergey

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import org.jetbrains.exposed.sql.Database
import tihonin.sergey.features.invite.configureInviteRouting
import tihonin.sergey.features.project.configureProjectRouting
import tihonin.sergey.features.login.configureLoginRouting
import tihonin.sergey.features.participant.configureParticipantsRouting
import tihonin.sergey.features.graph.configureGraphRouting
import tihonin.sergey.features.register.configureRegisterRouting
import tihonin.sergey.features.task.configureTaskRouting
import tihonin.sergey.plugins.*

fun main() {
    Database.connect(
        url = "jdbc:postgresql://147.45.247.19:5432/default_db",
        driver = "org.postgresql.Driver",
        user = "gen_user",
        password = "i0iTB@_|(cYrF;"
    )

    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSecurity()
    configureGraphRouting()
    configureParticipantsRouting()
    configureTaskRouting()
    configureProjectRouting()
    configureLoginRouting()
    configureRegisterRouting()
    configureInviteRouting()
    configureCORS()
    configureSerialization()
    configureRouting()
}
