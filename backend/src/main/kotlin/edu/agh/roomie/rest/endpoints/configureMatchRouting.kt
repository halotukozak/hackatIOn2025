package edu.agh.roomie.rest.endpoints

import edu.agh.roomie.rest.Dependencies
import edu.agh.roomie.rest.model.MatchResponse
import edu.agh.roomie.rest.model.SwipeResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

context(Dependencies)
fun Application.configureMatchRouting() = routing {
  route("/matches") {
    post("/swipe/{userId}/{swipedUserId}") {
      val userId = call.parameters["userId"]?.toIntOrNull()
      val swipedUserId = call.parameters["swipedUserId"]?.toIntOrNull()

      if (userId == null || swipedUserId == null) {
        call.respond(HttpStatusCode.BadRequest, "Invalid user IDs")
        return@post
      }

      val user = userService.getUserById(userId)
      val swipedUser = userService.getUserById(swipedUserId)

      if (user == null || swipedUser == null) {
        call.respond(HttpStatusCode.NotFound, "One or both users not found")
        return@post
      }

      val isMatch = matchService.swipeRight(userId, swipedUserId)

      call.respond(
        HttpStatusCode.OK,
        SwipeResponse(
          isMatch = isMatch,
          message = if (isMatch) "It's a match!" else "Swipe recorded"
        )
      )
    }

    get("/notifications/{userId}") {
      val userId = call.parameters["userId"]?.toIntOrNull()

      if (userId == null) {
        call.respond(HttpStatusCode.BadRequest, "Invalid user ID")
        return@get
      }

      val matchedUserIds = matchService.getMatches(userId)

      val matchedUsers = matchedUserIds.mapNotNull { matchedUserId ->
        userService.getUserById(matchedUserId)?.let { user ->
          MatchResponse(
            userId = matchedUserId,
            name = user.info.name,
            surname = user.info.surname,
          )
        }
      }

      call.respond(HttpStatusCode.OK, matchedUsers)
    }
  }
}