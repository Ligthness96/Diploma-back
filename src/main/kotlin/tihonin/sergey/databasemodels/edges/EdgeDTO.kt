package tihonin.sergey.databasemodels.edges


import java.util.*

class EdgeDTO(
    val projectid: UUID,
    val taskid: UUID,
    val edgefrom: String,
    val edgeto: String
)