package tihonin.sergey.features.register

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class RegisterReceiveRemote(
    val name: String,
    val login: String,
    val password: String
)

@Serializable
data class RegisterResponseRemote(
    val token: String
)
