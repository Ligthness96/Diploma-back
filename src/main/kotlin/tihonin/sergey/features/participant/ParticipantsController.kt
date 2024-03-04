package tihonin.sergey.features.participant

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import tihonin.sergey.databasemodels.participant.Participants
import tihonin.sergey.databasemodels.project.Projects
import tihonin.sergey.databasemodels.projecttree.Projecttrees
import tihonin.sergey.databasemodels.user.Users
import tihonin.sergey.features.login.UserResponseRemote
import tihonin.sergey.features.project.FetchByIDProjectReceiveRemote
import tihonin.sergey.features.project.ProjectResponseRemote
import tihonin.sergey.utils.TokenCheck
import java.util.*

class ParticipantsController(private val call: ApplicationCall) {
    suspend fun fetchParticipants(){
        val receive = call.receive<FetchByIDProjectReceiveRemote>()
        val participants = Participants.fetchAllByProjectID(UUID.fromString(receive.projectid))
        val users = mutableListOf<UserResponseRemote>()
        for(participant in participants){
            val user = Users.fetchUserByID(UUID.fromString(participant.userid))
            users.add(user)
        }
        call.respond(users)
    }
}