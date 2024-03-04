package tihonin.sergey.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import tihonin.sergey.utils.TokenCheck.validateCredential

fun Application.configureSecurity() {
    // Please read the jwt property from the config file if you are using EngineMain
    //val jwtAudience = "jwt-audience"
    //val jwtDomain = "https://jwt-provider-domain/"
    val jwtRealm = "ktor sample app"
    val jwtSecret = "secret"
    authentication{
        jwt("auth-jwt") {
            realm = jwtRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .build()
            )
            validate { credential ->
                validateCredential(credential)
            }
        }
    }
}
