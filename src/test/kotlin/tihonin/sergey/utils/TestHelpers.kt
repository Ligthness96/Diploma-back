package tihonin.sergey.utils

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

fun extractUserId(response: String): String {
  require(response.trim().startsWith("{")) { "Not a valid JSON response: $response" }
  val json = Json.parseToJsonElement(response).jsonObject
  return json["userid"]!!.jsonPrimitive.content
}

fun extractToken(responseBody: String): String =
    Json.parseToJsonElement(responseBody).jsonObject["token"]!!.jsonPrimitive.content

fun extractProjectId(responseBody: String): String =
    Json.parseToJsonElement(responseBody).jsonObject["projectid"]!!.jsonPrimitive.content

fun extractTaskId(response: String): String {
  val array = Json.parseToJsonElement(response).jsonArray
  return array.first().jsonObject["taskid"]!!.jsonPrimitive.content
}
