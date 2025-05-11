package tihonin.sergey.utils

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import tihonin.sergey.databasemodels.edges.Edges
import tihonin.sergey.databasemodels.invite.Invites
import tihonin.sergey.databasemodels.participant.Participants
import tihonin.sergey.databasemodels.project.Projects
import tihonin.sergey.databasemodels.task.Tasks
import tihonin.sergey.databasemodels.user.Users

object TestDatabase {
  fun connect() {
    Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")
    transaction { SchemaUtils.create(Users, Projects, Tasks, Participants, Edges, Invites) }
  }
}
