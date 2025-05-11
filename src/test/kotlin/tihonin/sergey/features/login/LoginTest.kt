package tihonin.sergey.features.login

import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.server.testing.*
import java.util.UUID
import kotlin.test.*
import tihonin.sergey.plugins.*
import tihonin.sergey.utils.TestDatabase
import tihonin.sergey.utils.testModule

class LoginTest {
  @BeforeTest fun setup() = TestDatabase.connect()

  @Test
  fun testLoginUser() = testApplication {
    application { testModule() }

    val randomLogin = "user_${UUID.randomUUID()}"

    client.post("/register") {
      contentType(ContentType.Application.Json)
      setBody("""{"name":"Test","login":"$randomLogin","password":"pass123"}""")
    }

    val loginResponse =
        client.post("/login") {
          contentType(ContentType.Application.Json)
          setBody("""{"login":"$randomLogin","password":"pass123"}""")
        }

    assertEquals(HttpStatusCode.OK, loginResponse.status)
    assertContains(loginResponse.bodyAsText(), "token")
  }
}
