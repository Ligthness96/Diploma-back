package tihonin.sergey.databasemodels.projecttree

import java.util.*

class ProjecttreeDTO(
    val projectid: UUID,
    val nodes: List<Node>,
    val edges: List<Edge>
)