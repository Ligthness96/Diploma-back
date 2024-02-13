package tihonin.sergey.databasemodels.user

import java.util.UUID

class UserDTO(
    val userid: UUID?,
    val name: String,
    val login: String,
    val passwordHash: String,
)
