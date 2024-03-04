package tihonin.sergey.features.login

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureLoginRouting() {

    routing {
        post("/login") {
            val loginController = LoginController(call)
            loginController.performLogin()
        }
        authenticate("auth-jwt") {
            get("/login/me") {
                val loginController = LoginController(call)
                loginController.loginCheck()
            }
        }
    }
}