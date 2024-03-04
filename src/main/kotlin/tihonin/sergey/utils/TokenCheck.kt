package tihonin.sergey.utils

import io.ktor.server.auth.jwt.*
import java.util.*

object  TokenCheck {
    fun validateCredential(credential: JWTCredential): JWTPrincipal? {
        if (credential.expiresAt?.after(Date()) == true && credential.payload.getClaim("userid").asString().isNotEmpty()) {
            return JWTPrincipal(credential.payload)
        }
        return null
    }
}