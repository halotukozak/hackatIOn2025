package edu.agh.roomie.rest.endpoints

import edu.agh.roomie.rest.Dependencies
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

context(Dependencies)
fun Application.configureUserRouting() = routing {
  route("/user") {
    get("/{userId}") {
      val userId = call.parameters["userId"]?.toIntOrNull()
      if (userId == null) {
        call.respond(HttpStatusCode.BadRequest, "Invalid user ID format")
        return@get
      } else {
        val user = userService.getUserById(userId)
        if (user != null) {
          call.respond(HttpStatusCode.OK, user)
        } else {
          call.respond(HttpStatusCode.NotFound, "User not found")
        }
      }
    }
  }
  get("/users") {
    val users = userService.getAllUsers()
    call.respond(HttpStatusCode.OK, users)
  }
}
