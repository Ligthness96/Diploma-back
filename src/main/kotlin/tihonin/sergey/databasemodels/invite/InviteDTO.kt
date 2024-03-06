package tihonin.sergey.databasemodels.invite

import java.time.LocalDate
import java.util.*

class InviteDTO(
    val projectid: UUID,
    val code: String,
    val created: LocalDate
)