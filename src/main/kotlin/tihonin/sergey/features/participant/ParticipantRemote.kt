package tihonin.sergey.features.participant

import kotlinx.serialization.Serializable

@Serializable
data class ParticipantRemote(
    val projectid: String,
    val userid: String,
    val admin: Boolean
)

@Serializable
data class ListParticipantsResponseRemote(
    val participants: List<ParticipantRemote>
)

@Serializable
data class UserRemote(
    val userid: String,
    val name: String,
    val admin: Boolean
)
@Serializable
data class ListUsersResponseRemote(
    val users: List<UserRemote>
)

