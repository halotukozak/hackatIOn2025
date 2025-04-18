package edu.agh.roomie.rest.endpoints

import edu.agh.roomie.rest.Dependencies
import edu.agh.roomie.rest.model.AdditionalInfoRequest
import edu.agh.roomie.rest.model.AdditionalPreferencesRequest
import edu.agh.roomie.rest.model.Faculties
import edu.agh.roomie.rest.model.Hobby
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

context(Dependencies)
fun Application.configureInitialRouting() {
  routing {
    route("/registration") {
      get("/available-hobbies") {
        call.respond(HttpStatusCode.OK, Hobby.entries.toList())
      }
      get("/available-departments") {
        call.respond(HttpStatusCode.OK, Faculties.all)
      }

      post("/additional-info") {
        val additionalInfoRequest = call.receive<AdditionalInfoRequest>()
        val userId = additionalInfoRequest.userId
        userService.upsertUserInfo(
          userId,
          additionalInfoRequest.info,
        )?.let {
          call.respond(HttpStatusCode.OK, "User additional data updated successfully")
        } ?: run {
          call.respond(HttpStatusCode.BadRequest, "Failed to update user additional data")
        }
      }

      post("/additional-preferences") {
        val additionalPreferencesRequest = call.receive<AdditionalPreferencesRequest>()
        val userId = additionalPreferencesRequest.userId
        userService.upsertUserPreferences(
          userId,
          additionalPreferencesRequest.preferences,
        )?.let {
          call.respond(HttpStatusCode.OK, "User preferences updated successfully")
        } ?: run {
          call.respond(HttpStatusCode.BadRequest, "Failed to update user preferences")
        }
      }
    }
  }
}