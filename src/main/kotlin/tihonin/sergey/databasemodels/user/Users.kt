package tihonin.sergey.databasemodels.user

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import tihonin.sergey.features.login.UserResponseRemote
import java.util.*

object Users: Table() {
    private val userid = Users.uuid("userid")
    private val name = Users.varchar("name", 128)
    private val login = Users.varchar("login", 64)
    private val passwordHash = Users.varchar("passwordhash", 255)

    fun insert(userDTO: UserDTO) {
        transaction {
            Users.insert {
                it[userid] = userDTO.userid ?: UUID.randomUUID()
                it[name] = userDTO.name
                it[login] = userDTO.login
                it[passwordHash] = userDTO.passwordHash
            }
        }
    }



    fun fetchUser(login: String): UserDTO? {
        return try {
            transaction {
                val user = Users.select { Users.login.eq(login) }.single()
                UserDTO(
                    userid = user[userid],
                    name = user[name],
                    login = user[Users.login],
                    passwordHash = user[passwordHash]
                )
            }
        } catch (e: Exception) {
            null
        }
    }
    fun fetchUserByID(userid: UUID): UserResponseRemote {
        return transaction {
                val user = Users.select { Users.userid eq userid }.single()
                UserResponseRemote(
                    userid = user[Users.userid].toString(),
                    name = user[name]
                )
            }
    }
}

