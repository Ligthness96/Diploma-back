package tihonin.sergey.features.login

import at.favre.lib.crypto.bcrypt.BCrypt
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import tihonin.sergey.databasemodels.token.TokenDTO
import tihonin.sergey.databasemodels.token.Tokens
import tihonin.sergey.databasemodels.user.Users
import tihonin.sergey.utils.TokenCheck
import java.util.*

class LoginController(private val call: ApplicationCall) {
    suspend fun performLogin() {
        val receive = call.receive<LoginReceiveRemote>()
        val userDTO = Users.fetchUser(receive.login)
        if (userDTO == null) {
            call.respond(HttpStatusCode.NotFound, "User not found")
        } else {
            if(BCrypt.verifyer().verify(receive.password.toByteArray(), userDTO.passwordHash.toByteArray()).verified) {
                val token = JWT.create()
                    .withClaim("username", receive.login)
                    .withIssuedAt(Date(System.currentTimeMillis()))
                    .sign(Algorithm.HMAC256("secret"))
                Tokens.insert(
                    TokenDTO(
                        login = userDTO.login,
                        token = token
                    )
                )

                call.respond(UserResponseRemote(
                    userDTO.userid.toString(),
                    userDTO.name,
                    userDTO.login,
                    token))
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid password")
            }
        }
    }

    suspend fun performLogout(){
        val token = call.request.headers["TOKEN"]
        if (TokenCheck.isTokenValid(token.orEmpty())) {
            val receive = call.receive<LogoutReceiveRemote>()
            Tokens.deleteToken(TokenDTO(
                login = receive.login,
                token = receive.token
            ))
            call.respond(HttpStatusCode.OK, "Logout successful")
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Token expired")
        }
    }
}