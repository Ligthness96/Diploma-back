package tihonin.sergey.databasemodels.edges

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction
import tihonin.sergey.databasemodels.task.Tasks
import tihonin.sergey.features.graph.EdgesRemote
import java.util.*

object Edges: Table() {
    private val projectid = Edges.uuid("projectid")
    private val taskid = Edges.uuid("taskid")
    private val edgefrom = Edges.varchar("edgefrom", 64)
    private val edgeto = Edges.varchar("edgeto", 64)


    fun createEdge(edgeDTO: EdgeDTO) {
        transaction {
            Edges.insert {
                it[taskid] = edgeDTO.taskid
                it[projectid] = edgeDTO.projectid
                it[edgefrom] = edgeDTO.edgefrom
                it[edgeto] = edgeDTO.edgeto
            }
        }
    }

    fun fetchEdges(projectid: UUID): List<EdgesRemote>? {
        return try {
            transaction {
                Edges.select { Edges.projectid.eq(projectid) }.toList()
                    .map {
                        EdgesRemote(
                            taskid = it[taskid].toString(),
                            projectid = it[Edges.projectid].toString(),
                            edgefrom = it[edgefrom],
                            edgeto = it[edgeto]
                        )
                    }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    fun fetchEdge(edgefrom: String, edgeto: String): EdgeDTO? {
        return try {
            transaction {
                val edge = Edges.select { Edges.edgefrom.eq(edgefrom).and(Edges.edgeto.eq(edgeto))}.single()
                   EdgeDTO(
                            taskid = edge[Edges.taskid],
                            projectid = edge[Edges.projectid],
                            edgefrom = edge[Edges.edgefrom],
                            edgeto = edge[Edges.edgeto]
                        )
            }
        } catch (e: Exception) {
            null
        }
    }

    fun deleteEdge(edgefrom: String, edgeto: String): Boolean {
        return transaction {
            Edges.deleteWhere {
                Edges.edgefrom.eq(edgefrom).and(Edges.edgeto.eq(edgeto))
            }
        } > 0
    }

    fun deleteEdgesWithTask(taskid: UUID): Boolean {
        return transaction {
            Edges.deleteWhere { Edges.taskid.eq(taskid).or(edgeto.eq(taskid.toString()).or { edgefrom.eq(taskid.toString()) }) }
        } > 0
    }
    fun deleteEdgesWithProject(projectid: UUID): Boolean {
        return transaction {
            Edges.deleteWhere { Edges.projectid.eq(projectid) }
        } > 0
    }

}