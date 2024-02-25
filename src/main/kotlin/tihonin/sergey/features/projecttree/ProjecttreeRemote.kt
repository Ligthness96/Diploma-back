package tihonin.sergey.features.projecttree

import kotlinx.serialization.Serializable
import tihonin.sergey.databasemodels.projecttree.Edge
import tihonin.sergey.databasemodels.projecttree.Node

@Serializable
data class ProjecttreeRequest(
    val projectid: String,
    val nodes: List<Node>,
    val edges: List<Edge>
)