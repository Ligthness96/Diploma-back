package tihonin.sergey.databasemodels.project

import kotlinx.serialization.json.Json
import java.util.*

class ProjecttreeDTO(
    val projectid: UUID,
    val nodes: List<Node>,
    val edges: List<Edge>
)