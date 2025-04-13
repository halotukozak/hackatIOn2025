package edu.agh.roomie.rest.endpoints

import edu.agh.roomie.rest.Dependencies
import edu.agh.roomie.rest.model.Match
import edu.agh.roomie.rest.model.CostFunction
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

context(Dependencies)
fun Application.configureUserRouting() = routing {
    route("/user") {
        route("/{userId}") {
            get {
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
            get("/discover"){
                val userId = call.parameters["userId"]?.toIntOrNull()
                val user = userId?.let { userService.getUserById(it) }
                if (user == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid user ID")
                    return@get
                }
                val availableMatches = matchService.getAvailableMatchesForUser(userId)
                val results = availableMatches
                    .filter { it.info.gender == user.info.gender }
                    .map { Match(it, CostFunction.countScore(it, user)) }
                    .sortedByDescending { it.score }
                call.respond(HttpStatusCode.OK, results)
            }
            get("/matches") {
                val userId = call.parameters["userId"]?.toIntOrNull()
                val user = userId?.let { userService.getUserById(it) }
                if (user == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid user ID")
                    return@get
                }

                val results = matchService.getResultsForUser(userId)
                call.respond(HttpStatusCode.OK, results)
            }
        }
    }
    get("/users") {
        val users = userService.getAllUsers()
        call.respond(HttpStatusCode.OK, users)
    }
}
