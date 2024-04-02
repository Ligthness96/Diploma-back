package tihonin.sergey.features.register

import at.favre.lib.crypto.bcrypt.BCrypt
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.jetbrains.exposed.exceptions.ExposedSQLException
import tihonin.sergey.databasemodels.user.UserDTO
import tihonin.sergey.databasemodels.user.Users
import tihonin.sergey.features.login.LoginResponseRemote
import java.util.*

class RegisterController(private val call: ApplicationCall) {

    suspend fun registerNewUser() {
        val receive = call.receive<RegisterReceiveRemote>()
        val userDTO = Users.fetchUser(receive.login)
        if (userDTO != null) {
            call.respond(HttpStatusCode.Conflict, "User already exists")
        } else {
            val pHash = BCrypt.withDefaults().hashToString(10, receive.password.toCharArray())
            try {
                Users.insert(
                    UserDTO(
                        userid = null,
                        name = receive.name,
                        login = receive.login,
                        passwordHash = pHash
                    )
                )
            } catch (e: ExposedSQLException) {
                call.respond(HttpStatusCode.Conflict, "Database  error")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Can't create user ${e.localizedMessage}")
            }
            val user = Users.fetchUser(receive.login)
            if (user != null) {
                val token = JWT.create()
                    .withClaim("userid", user.userid.toString())
                    .withIssuedAt(Date(System.currentTimeMillis()))
                    .withExpiresAt(Date(System.currentTimeMillis() + 2592000000))
                    .sign(Algorithm.HMAC256("secret"))
                call.respond(LoginResponseRemote(
                    userid = user.userid.toString(),
                    name = user.name,
                    login = user.login,
                    token = token
                ))
            }
        }
    }
}