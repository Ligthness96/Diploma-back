package tihonin.sergey.features.project

import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.server.testing.*
import java.util.UUID
import kotlin.test.*
import tihonin.sergey.plugins.*
import tihonin.sergey.utils.TestDatabase
import tihonin.sergey.utils.extractToken
import tihonin.sergey.utils.extractUserId
import tihonin.sergey.utils.testModule

class ProjectTest {
  @BeforeTest fun setup() = TestDatabase.connect()

  @Test
  fun testCreateAndFetchProject() = testApplication {
    application { testModule() }

    val randomLogin = "user_${UUID.randomUUID()}"
    val registerResponse =
        client.post("/register") {
          contentType(ContentType.Application.Json)
          setBody("""{"name":"Owner","login":"$randomLogin","password":"pass"}""")
        }
    val token = extractToken(registerResponse.bodyAsText())

    val response =
        client.post("/project/create") {
          header("Authorization", "Bearer $token")
          contentType(ContentType.Application.Json)
          setBody(
              """{"projectname":"Test Project","owner":"${extractUserId(registerResponse.bodyAsText())}"}""")
        }

    assertEquals(HttpStatusCode.OK, response.status)
    assertContains(response.bodyAsText(), "Test Project")
  }
}
