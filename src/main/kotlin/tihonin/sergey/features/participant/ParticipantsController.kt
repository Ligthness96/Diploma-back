package tihonin.sergey.features.participant

import io.ktor.server.application.*
import io.ktor.server.response.*
import tihonin.sergey.databasemodels.participant.Participants
import tihonin.sergey.databasemodels.user.Users
import java.util.*

class ParticipantsController(private val call: ApplicationCall) {
    suspend fun fetchParticipants(){
        val id = call.request.queryParameters["projectid"]
        val participants = Participants.fetchAllByProjectID(UUID.fromString(id))
        val users = mutableListOf<UserRemote>()
        for(participant in participants){
            val user = Users.fetchUserByID(UUID.fromString(participant.userid))
            if (user!=null){
                users.add(UserRemote(
                    userid = user.userid,
                    name = user.name,
                    admin = participant.admin
                ))
            }
        }
        call.respond(users)
        //call.respondText("users")
    }
}