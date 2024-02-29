package tihonin.sergey.databasemodels.participant

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import tihonin.sergey.databasemodels.project.ProjectDTO
import tihonin.sergey.features.participant.ListParticipantsResponseRemote
import tihonin.sergey.features.participant.ParticipantRemote
import tihonin.sergey.features.participant.UserRemote
import tihonin.sergey.features.project.ProjectResponseRemote
import java.util.UUID

object Participants: Table() {
    private val projectid = Participants.uuid("projectid")
    private val userid = Participants.uuid("userid")
    private val admin = Participants.bool("admin")


    fun insert(participantDTO: ParticipantDTO){
        transaction {
            Participants.insert {
                it[projectid] = participantDTO.projectid
                it[userid] = participantDTO.userid
                it[admin] = participantDTO.admin
            }
        }
    }

    fun fetchAllByProjectID(projectid: UUID): List<ParticipantRemote>{
        return transaction {
            Participants.select { Participants.projectid.eq(projectid)}.toList().map {
                ParticipantRemote(
                    projectid = it[Participants.projectid].toString(),
                    userid = it[userid].toString(),
                    admin = it[admin]
                )
            }
        }
    }
    fun fetchAllByUserID(userid: UUID): List<ParticipantRemote>{
        return transaction {
            Participants.select { Participants.userid.eq(userid)}.toList().map {
                ParticipantRemote(
                    projectid = it[projectid].toString(),
                    userid = it[Participants.userid].toString(),
                    admin = it[admin]
                )
            }
        }
    }

    fun delete(participantDTO: ParticipantDTO): Boolean{
        return transaction {
                Participants.deleteWhere { projectid eq participantDTO.projectid and(userid eq participantDTO.userid) } > 0
            }
    }

    fun deleteAll(projectid: UUID): Boolean{
        return transaction {
            Participants.deleteWhere { Participants.projectid eq projectid } > 0
        }
    }
}