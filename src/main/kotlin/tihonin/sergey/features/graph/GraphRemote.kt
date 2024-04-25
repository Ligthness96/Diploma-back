package tihonin.sergey.features.graph

import kotlinx.serialization.Serializable

@Serializable
data class EdgesRemote(
    val projectid: String,
    val taskid: String,
    val edgefrom: String,
    val edgeto: String,
)

@Serializable
data class EdgeDeleteRemote(
    val edgefrom: String,
    val edgeto: String,
)
data class fetchEdgesReceive(
    val projectid: String,
)