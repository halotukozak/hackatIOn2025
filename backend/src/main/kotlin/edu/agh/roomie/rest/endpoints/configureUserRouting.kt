package edu.agh.roomie.rest.endpoints

import edu.agh.roomie.rest.Dependencies
import edu.agh.roomie.service.FakeUserGenerator
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
      }

      val useFake = call.request.queryParameters["fake"]?.toBoolean() ?: false

      if (useFake) {
        val fakeUser = FakeUserGenerator.generateFakeUser(userId)
        call.respond(HttpStatusCode.OK, fakeUser)
      } else {
        val user = userService.getUserById(userId)
        if (user != null) {
          call.respond(HttpStatusCode.OK, user)
        } else {
          val fakeUser = FakeUserGenerator.generateFakeUser(userId)
          call.respond(HttpStatusCode.OK, fakeUser)
        }
    }

    // Endpoint to generate multiple fake users
    get("/fake") {
      val count = call.request.queryParameters["count"]?.toIntOrNull() ?: 10
      val fakeUsers = List(count) { index ->
        FakeUserGenerator.generateFakeUser(1000 + index)
      }
      call.respond(HttpStatusCode.OK, fakeUsers)
    }
  }
}