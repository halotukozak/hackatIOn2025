package edu.agh.roomie.rest.endpoints

import edu.agh.roomie.rest.Dependencies
import edu.agh.roomie.rest.model.AdditionalInfoRequest
import edu.agh.roomie.rest.model.Departaments
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
        call.respond(HttpStatusCode.OK, Departaments.all)
      }

      post("/additional-data") {
        val additionalInfoRequest = call.receive<AdditionalInfoRequest>()
        val userId = additionalInfoRequest.userId
        userService.createUserAdditionalData(
          userId,
          additionalInfoRequest.info,
          additionalInfoRequest.preferences,
        )?.let {
          call.respond(HttpStatusCode.OK, "User additional data updated successfully")
        } ?: run {
          call.respond(HttpStatusCode.BadRequest, "Failed to update user additional data")
        }
      }
    }
  }
}
