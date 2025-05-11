package tihonin.sergey.utils

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import tihonin.sergey.features.graph.configureGraphRouting
import tihonin.sergey.features.invite.configureInviteRouting
import tihonin.sergey.features.login.configureLoginRouting
import tihonin.sergey.features.participant.configureParticipantsRouting
import tihonin.sergey.features.project.configureProjectRouting
import tihonin.sergey.features.register.configureRegisterRouting
import tihonin.sergey.features.task.configureTaskRouting
import tihonin.sergey.plugins.configureCORS
import tihonin.sergey.plugins.configureRouting
import tihonin.sergey.plugins.configureSecurity
import tihonin.sergey.plugins.configureSerialization

fun Application.testModule() {
  Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")

  configureSecurity()
  configureSerialization()
  configureCORS()
  configureRouting()

  configureGraphRouting()
  configureInviteRouting()
  configureLoginRouting()
  configureParticipantsRouting()
  configureProjectRouting()
  configureRegisterRouting()
  configureTaskRouting()
}
