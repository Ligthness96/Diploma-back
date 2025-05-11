package tihonin.sergey.features.register

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import java.util.UUID
import kotlin.test.*
import tihonin.sergey.plugins.*
import tihonin.sergey.utils.TestDatabase
import tihonin.sergey.utils.testModule

class RegisterTest {
  @BeforeTest fun setup() = TestDatabase.connect()

  @Test
  fun testUserRegistration() = testApplication {
    application { testModule() }

    val randomLogin = "user_${UUID.randomUUID()}"
    val response =
        client.post("/register") {
          contentType(ContentType.Application.Json)
          setBody("""{"name":"Test User","login":"$randomLogin","password":"password123"}""")
        }

    assertEquals(HttpStatusCode.OK, response.status)
    assertContains(response.bodyAsText(), "token")
  }
}
