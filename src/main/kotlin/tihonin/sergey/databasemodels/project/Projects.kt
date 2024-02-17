package tihonin.sergey.databasemodels.project

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID


object Projects: Table() {
    private val projectid = Projects.uuid("projectid")
    private val projectname = Projects.varchar("projectname", 128)
    private val owner = Projects.uuid("owner")

    fun insert(projectDTO: ProjectDTO){
        transaction {
            Projects.insert {
                it[projectid] = projectDTO.projectid?: UUID.randomUUID()
                it[projectname] = projectDTO.projectname
                it[owner] = projectDTO.owner
            }
        }
    }

    fun fetchOwnProject(projectid: UUID): ProjectDTO?{
        return try {
            transaction {
               val project = Projects.select { Projects.projectid.eq(projectid) }.single()
                ProjectDTO(
                    projectid = project[Projects.projectid],
                    projectname = project[projectname],
                    owner = project[owner]
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    fun deleteProject(projectid: UUID): Int? {
        return try{
            transaction {
                Projects.deleteWhere { Projects.projectid.eq(projectid) }
            }
        } catch (e: Exception) {
            null
        }
    }
}