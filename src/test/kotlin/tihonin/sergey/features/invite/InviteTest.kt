package tihonin.sergey.features.invite

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

class InviteTest {
  @BeforeTest fun setup() = TestDatabase.connect()

  @Test
  fun testCreateAndJoinInvite() = testApplication {
    application { testModule() }

    val randomOwnerLogin = "user_${UUID.randomUUID()}"
    val registerOwnerResponse =
        client.post("/register") {
          contentType(ContentType.Application.Json)
          setBody("""{"name":"Owner","login":"$randomOwnerLogin","password":"pass"}""")
        }
    println("registerOwnerResponse: ${registerOwnerResponse.bodyAsText()}")
    val ownerId = extractUserId(registerOwnerResponse.bodyAsText())
    val ownerToken = extractToken(registerOwnerResponse.bodyAsText())

    val projectResponse =
        client.post("/project/create") {
          header("Authorization", "Bearer $ownerToken")
          contentType(ContentType.Application.Json)
          setBody("""{"projectname":"Invite Project","owner":"$ownerId"}""")
        }
    println("projectResponse: ${projectResponse.bodyAsText()}")
    val projectId = extractProjectId(projectResponse.bodyAsText())

    val inviteCode = UUID.randomUUID().toString()

    val inviteResponse =
        client.post("/invite/create") {
          header("Authorization", "Bearer $ownerToken")
          contentType(ContentType.Application.Json)
          setBody("""{"projectid":"$projectId","code":"$inviteCode"}""")
        }
    println("inviteResponse: ${inviteResponse.bodyAsText()}")
    assertEquals(HttpStatusCode.Created, inviteResponse.status)

    val randomLogin = "user_${UUID.randomUUID()}"
    val registerUserResponse =
        client.post("/register") {
          contentType(ContentType.Application.Json)
          setBody("""{"name":"User","login":"$randomLogin","password":"pass"}""")
        }
    println("registerUserResponse: ${registerUserResponse.bodyAsText()}")
    val userId = extractUserId(registerUserResponse.bodyAsText())
    val userToken = extractToken(registerUserResponse.bodyAsText())

    val joinResponse =
        client.post("/invite/join") {
          header("Authorization", "Bearer $userToken")
          contentType(ContentType.Application.Json)
          setBody("""{"code":"$inviteCode","userid":"$userId"}""")
        }
    println("joinResponse: ${joinResponse.bodyAsText()}")
    assertEquals(HttpStatusCode.OK, joinResponse.status)
    assertContains(joinResponse.bodyAsText(), "Вы присоединились к проекту")
  }
}
