package tihonin.sergey.databasemodels.participant

import java.util.UUID

class ParticipantDTO(
    val projectid: UUID,
    val userid: UUID,
    val admin: Boolean
)