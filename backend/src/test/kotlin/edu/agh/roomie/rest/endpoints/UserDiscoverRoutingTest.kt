//package edu.agh.roomie.rest.endpoints
//
//import edu.agh.roomie.rest.model.Match
//import edu.agh.roomie.rest.model.RegisterRequest
//import edu.agh.roomie.rest.model.countScore
//import io.ktor.client.request.*
//import io.ktor.client.statement.*
//import io.ktor.http.*
//import io.ktor.server.response.*
//import io.ktor.server.routing.*
//import io.ktor.server.testing.*
//import kotlin.test.Test
//
//class UserDiscoverRoutingTest : BaseRoutingTest() {
//
//    @Test
//    fun testGetUserDiscoverEndpoint() = testApplication {
//        // Configure the test application
//        configureTestApplication {
//            routing {
//                with(dependencies) {
//                    route("/user") {
//                        route("/{userId}") {
//                            get("/discover") {
//                                val userId = call.parameters["userId"]?.toIntOrNull()
//                                val user = userId?.let { userService.getUserById(it) }
//                                if (user == null) {
//                                    call.respond(HttpStatusCode.BadRequest, "Invalid user ID")
//                                    return@get
//                                }
//                                val availableMatches = matchService.getAvailableMatchesForUser(userId)
//                                val results = availableMatches.map {
//                                    Match(it, countScore(it, user))
//                                }
//                                call.respond(HttpStatusCode.OK, results)
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        // Register two users
//        val userId1 = userService.register(RegisterRequest(email = "user1@example.com", password = "password123"))
//        val userId2 = userService.register(RegisterRequest(email = "user2@example.com", password = "password123"))
//
//        // Test getting discover matches
//        val response = client.get("/user/$userId1/discover")
//
//        // Verify response
//        TestUtils.verifyResponseStatus(response, HttpStatusCode.OK)
//
//        // Test with invalid user ID
//        val invalidResponse = client.get("/user/invalid/discover")
//        TestUtils.verifyResponseStatus(invalidResponse, HttpStatusCode.BadRequest)
//    }
//}