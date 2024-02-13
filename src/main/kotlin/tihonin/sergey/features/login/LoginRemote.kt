package tihonin.sergey.features.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginReceiveRemote(
    val login: String,
    val password: String
)

@Serializable
data class LogoutReceiveRemote(
    val login: String,
    val token: String
)

@Serializable
data class UserResponseRemote(
    val userid: String,
    val name: String,
    val login: String,
    val token: String
)