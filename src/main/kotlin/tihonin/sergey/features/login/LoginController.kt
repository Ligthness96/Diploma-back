package tihonin.sergey.features.login

import at.favre.lib.crypto.bcrypt.BCrypt
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.*
import tihonin.sergey.databasemodels.user.Users
import tihonin.sergey.databasemodels.user.Users.fetchUserByID
import java.util.*

class LoginController(private val call: ApplicationCall) {
    suspend fun performLogin() {
        val receive = call.receive<LoginReceiveRemote>()
        val userDTO = Users.fetchUser(receive.login)
        if (userDTO == null) {
            call.respond(HttpStatusCode.NoContent, "Неверный логин или пароль")
        } else {
            if(BCrypt.verifyer().verify(receive.password.toByteArray(), userDTO.passwordHash.toByteArray()).verified) {
                val token = JWT.create()
                    .withClaim("userid", userDTO.userid.toString())
                    .withIssuedAt(Date(System.currentTimeMillis()))
                    .withExpiresAt(Date(System.currentTimeMillis() + 2592000000))
                    .sign(Algorithm.HMAC256("secret"))

                call.respond(LoginResponseRemote(
                    userDTO.userid.toString(),
                    userDTO.name,
                    userDTO.login,
                    token))
            } else {
                call.respond(HttpStatusCode.NoContent, "Неверный логин или пароль")
            }
        }
    }

    suspend fun loginCheck() {
        val token = call.request.headers["Authorization"]?.replace("Bearer ".toRegex(), "")
        val jwt = JWT.decode(token)
        val user = fetchUserByID(UUID.fromString(jwt.getClaim("userid").asString()))
        call.respond(user)
    }
}