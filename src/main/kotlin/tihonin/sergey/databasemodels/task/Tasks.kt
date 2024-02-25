package tihonin.sergey.databasemodels.task

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.transactions.transaction
import tihonin.sergey.features.task.FetchTaskResponse
import java.util.*

object Tasks: Table() {
    private val taskid = Tasks.uuid("taskid")
    private val projectid = Tasks.uuid("projectid")
    private val taskname = Tasks.varchar("taskname", 128)
    private val executor = Tasks.uuid("executor")
    private val datestart = Tasks.date("datestart")
    private val dateend = Tasks.date("dateend")

    fun insert(taskDTO: TaskDTO) {
        transaction {
            Tasks.insert {
                it[taskid] = taskDTO.taskid ?: UUID.randomUUID()
                it[projectid] = taskDTO.projectid
                it[taskname] = taskDTO.taskname
                it[executor] = taskDTO.executor
                it[datestart] = taskDTO.datestart
                it[dateend] = taskDTO.dateend
            }
        }
    }

    fun fetchTaskByID(taskid: UUID): TaskDTO? {
        return try {
            transaction {
                val task = Tasks.select { Tasks.projectid.eq(taskid)}.single()
                TaskDTO(
                    taskid = task[Tasks.taskid],
                    projectid = task[projectid],
                    taskname = task[taskname],
                    executor = task[executor],
                    datestart = task[datestart],
                    dateend = task[dateend]
                )
            }
        } catch (e: Exception) {
          null
        }
    }

    fun editTask(taskDTO: TaskDTO, taskid: UUID): Boolean {
        return transaction {
            Tasks.update({ Tasks.taskid.eq(taskid)}){
                it[taskname] = taskDTO.taskname
                it[executor] = taskDTO.executor
                it[datestart] = taskDTO.datestart
                it[dateend] = taskDTO.dateend
            }
        } > 0
    }

    fun fetchAllTask(projectid: UUID): List<FetchTaskResponse> {
        return try {
            transaction {
                Tasks.select { Tasks.projectid.eq(taskid) }.toList()
                    .map {
                        FetchTaskResponse(
                            taskid = it[taskid].toString(),
                            projectid = it[Tasks.projectid].toString(),
                            taskname = it[taskname],
                            executor = it[executor].toString(),
                            datestart = it[datestart].toString(),
                            dateend = it[dateend].toString()
                        )
                    }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun deleteTaskByID(taskid: UUID): Boolean {
        return transaction {
            Tasks.deleteWhere { Tasks.taskid.eq(taskid) }
        } > 0
    }
    fun deleteAllTasks(projectid: UUID): Boolean {
        return transaction {
            Tasks.deleteWhere { Tasks.projectid.eq(taskid) }
        } > 0
    }

}