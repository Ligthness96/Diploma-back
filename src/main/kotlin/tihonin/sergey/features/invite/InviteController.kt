package tihonin.sergey.features.invite

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import tihonin.sergey.databasemodels.invite.InviteDTO
import tihonin.sergey.databasemodels.invite.Invites
import tihonin.sergey.databasemodels.participant.ParticipantDTO
import tihonin.sergey.databasemodels.participant.Participants
import java.time.LocalDate
import java.util.UUID

class InviteController(private val call: ApplicationCall) {

    suspend fun createInvite(){
        val receive = call.receive<CreateInviteReceiveRemote>()
        Invites.insert(InviteDTO(
            UUID.fromString(receive.projectid),
            receive.code,
            LocalDate.now()
        ))
        call.respond(HttpStatusCode.Created, message = "Код приглашения успешно создан")
    }

    suspend fun joinInvite() {
        val receive = call.receive<JoinInviteReceiveRemote>()
        val invite = Invites.fetch(receive.code)
        if (invite!=null){
            val isExpired = invite.created.plusDays(7).isBefore(LocalDate.now())
            if (isExpired) {
                Invites.delete(receive.code)
                call.respond(HttpStatusCode.NoContent, "Срок действия кода истек, запросите новый код прглашения")
            } else {
                Participants.insert(ParticipantDTO(
                    invite.projectid,
                    UUID.fromString(receive.userid),
                    admin = false
                ))
                Invites.delete(receive.code)
                call.respond(HttpStatusCode.OK, "Вы присоединились к проекту")
            }
        } else {
            call.respond(HttpStatusCode.NotFound, "Неверный код")
        }
    }

}