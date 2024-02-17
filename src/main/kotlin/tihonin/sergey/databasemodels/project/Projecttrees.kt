package tihonin.sergey.databasemodels.project

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.util.reflect.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.buildJsonArray
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.json.json
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

@Serializable
data class Node(val id: Int, val label: String, val title: String)
@Serializable
data class Edge(val from: Int, val to: Int)

val format = Json { prettyPrint = true }
object Projecttrees: Table() {
    private val projectid = Projecttrees.uuid("projectid")
    private val nodes = Projecttrees.json<List<Node>>("nodes", format)
    private val edges = Projecttrees.json<List<Edge>>("edges", format)

    fun insert(projecttreeDTO: ProjecttreeDTO){
        val nodesList = Gson().fromJson(projecttreeDTO.nodes.toString(), Array<Node>::class.java).asList()
        val edgesList = Gson().fromJson(projecttreeDTO.edges.toString(), Array<Edge>::class.java).asList()
        transaction {
            Projecttrees.insert{
                it[projectid] = projecttreeDTO.projectid
                it[nodes] = nodesList
                it[edges] = edgesList
            }
        }
    }

    fun fetchOwnProjecttree(projectid: UUID): ProjecttreeDTO?{
        return try {
            transaction {
                val projecttree = Projecttrees.select { Projecttrees.projectid.eq(projectid) }.single()
                ProjecttreeDTO(
                    projectid = projecttree[Projecttrees.projectid],
                    nodes = projecttree[nodes],
                    edges = projecttree[edges]
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    fun deleteProjecttree(projectid: UUID): Int? {
        return try{
            transaction {
                Projecttrees.deleteWhere { Projecttrees.projectid.eq(projectid) }
            }
        } catch (e: Exception) {
            null
        }
    }
}