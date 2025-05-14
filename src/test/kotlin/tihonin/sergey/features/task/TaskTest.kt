package tihonin.sergey.features.task

import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*
import tihonin.sergey.plugins.*
import tihonin.sergey.utils.TestDatabase
import tihonin.sergey.utils.extractProjectId
import tihonin.sergey.utils.extractTaskId
import tihonin.sergey.utils.extractToken
import tihonin.sergey.utils.extractUserId
import tihonin.sergey.utils.testModule

class TaskTest {
  @BeforeTest fun setup() = TestDatabase.connect()

  @Test
  fun testCreateEditDeleteTask() = testApplication {
    application { testModule() }

    val registerResponse =
        client.post("/register") {
          contentType(ContentType.Application.Json)
          setBody("""{"name":"Executor","login":"executor","password":"password"}""")
        }
    val userId = extractUserId(registerResponse.bodyAsText())
    val token = extractToken(registerResponse.bodyAsText())

    val projectResponse =
        client.post("/project/create") {
          header("Authorization", "Bearer $token")
          contentType(ContentType.Application.Json)
          setBody("""{"projectname":"Project Test","owner":"$userId"}""")
        }
    val projectId = extractProjectId(projectResponse.bodyAsText())

    val createTaskResponse =
        client.post("/task/create") {
          header("Authorization", "Bearer $token")
          contentType(ContentType.Application.Json)
          setBody(
              """{
                "projectid":"$projectId",
                "taskname":"Initial Task",
                "executor":"$userId",
                "datestart":"2024-05-01",
                "dateend":"2024-05-10"
            }""")
        }
    assertEquals(HttpStatusCode.Created, createTaskResponse.status)

    val tasksResponse =
        client.get("/task/fetch/all?projectid=$projectId") {
          header("Authorization", "Bearer $token")
        }
    assertEquals(HttpStatusCode.OK, tasksResponse.status)
    assertContains(tasksResponse.bodyAsText(), "Initial Task")

    val taskId = extractTaskId(tasksResponse.bodyAsText())

    val editTaskResponse =
        client.post("/task/edit") {
          header("Authorization", "Bearer $token")
          contentType(ContentType.Application.Json)
          setBody(
              """{
                "taskid":"$taskId",
                "projectid":"$projectId",
                "taskname":"Edited Task",
                "executor":"$userId",
                "datestart":"2024-05-02",
                "dateend":"2024-05-12",
                "iscomplete":false
            }""")
        }
    assertEquals(HttpStatusCode.OK, editTaskResponse.status)
    assertContains(editTaskResponse.bodyAsText(), "Edited Task")

    val deleteTaskResponse =
        client.post("/task/delete") {
          header("Authorization", "Bearer $token")
          contentType(ContentType.Application.Json)
          setBody("""{"taskid":"$taskId"}""")
        }
    assertEquals(HttpStatusCode.OK, deleteTaskResponse.status)
  }
}
