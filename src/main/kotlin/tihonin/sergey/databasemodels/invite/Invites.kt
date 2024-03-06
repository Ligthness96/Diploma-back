package tihonin.sergey.databasemodels.invite

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

object Invites: Table() {
    private val projectid = Invites.uuid("projectid")
    private val code = Invites.varchar("code", 50)
    private val created = Invites.date("created")

    fun insert(inviteDTO: InviteDTO) {
        transaction {
            Invites.insert {
                it[projectid] = inviteDTO.projectid
                it[code] = inviteDTO.code
                it[created] = inviteDTO.created
            }
        }
    }

    fun fetch(code: String): InviteDTO? {
        return try {
            transaction {
                val invite = Invites.select { Invites.code.eq(code) }.single()
                InviteDTO(
                    projectid = invite[projectid],
                    code = invite[Invites.code],
                    created = invite[created]
                )
            }
        } catch (e: Exception){
            null
        }
    }

    fun delete(code: String): Boolean {
        return transaction {
            Invites.deleteWhere { Invites.code.eq(code) } > 0
        }

    }
}