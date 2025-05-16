package tihonin.sergey.features.graph

import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.server.testing.*
import java.util.UUID
import kotlin.test.*
import tihonin.sergey.plugins.*
import tihonin.sergey.utils.TestDatabase
import tihonin.sergey.utils.extractProjectId
import tihonin.sergey.utils.extractToken
import tihonin.sergey.utils.extractUserId
import tihonin.sergey.utils.testModule

class GraphTest {
  @BeforeTest fun setup() = TestDatabase.connect()

  @Test
  fun testCreateAndDeleteEdge() = testApplication {
    application { testModule() }

    val registerResponse =
        client.post("/register") {
          contentType(ContentType.Application.Json)
          setBody("""{"name":"User","login":"user","password":"pass"}""")
        }
    val userId = extractUserId(registerResponse.bodyAsText())
    val token = extractToken(registerResponse.bodyAsText())

    val projectResponse =
        client.post("/project/create") {
          header("Authorization", "Bearer $token")
          contentType(ContentType.Application.Json)
          setBody("""{"projectname":"Graph Project","owner":"$userId"}""")
        }
    val projectId = extractProjectId(projectResponse.bodyAsText())

    val taskId = UUID.randomUUID().toString()

    val edgeResponse =
        client.post("/graph/edge/create") {
          header("Authorization", "Bearer $token")
          contentType(ContentType.Application.Json)
          setBody(
              """{
                "projectid":"$projectId",
                "taskid":"$taskId",
                "edgefrom":"taskA",
                "edgeto":"taskB"
            }""")
        }
    assertEquals(HttpStatusCode.OK, edgeResponse.status)

    val fetchEdgesResponse =
        client.get("/graph/edges?projectid=$projectId") { header("Authorization", "Bearer $token") }
    assertEquals(HttpStatusCode.OK, fetchEdgesResponse.status)
    assertContains(fetchEdgesResponse.bodyAsText(), "taskA")

    val deleteEdgeResponse =
        client.post("/graph/edge/delete") {
          header("Authorization", "Bearer $token")
          contentType(ContentType.Application.Json)
          setBody("""{"edgefrom":"taskA","edgeto":"taskB"}""")
        }
    assertEquals(HttpStatusCode.OK, deleteEdgeResponse.status)
  }
}
