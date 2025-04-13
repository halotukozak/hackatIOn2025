package edu.agh.roomie.rest.endpoints

import edu.agh.roomie.rest.Dependencies
import edu.agh.roomie.rest.model.MatchStatus
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

context(Dependencies)
fun Application.configureMatchRouting() = routing {
    route("/matches") {
        post("/swipe/{userId}/{swipedUserId}") {
            val userId = call.parameters["userId"]?.toIntOrNull()
            val swipedUserId = call.parameters["swipedUserId"]?.toIntOrNull()
            val status = call.request.queryParameters["status"]?.let {
                MatchStatus.valueOf(it.uppercase(Locale.getDefault()))
            } ?: run {
                call.respond(HttpStatusCode.BadRequest, "Missing status parameter")
                return@post
            }

            if (userId == null || swipedUserId == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid user IDs")
                return@post
            }

            val matchResult = matchService.registerSwipe(userId, swipedUserId, status)

            call.respond(HttpStatusCode.OK, matchResult)
        }
    }
}
