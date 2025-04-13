//package edu.agh.roomie.rest.endpoints
//
//import edu.agh.roomie.rest.model.RegisterRequest
//import io.ktor.client.request.*
//import io.ktor.client.statement.*
//import io.ktor.http.*
//import io.ktor.server.response.*
//import io.ktor.server.routing.*
//import io.ktor.server.testing.*
//import kotlin.test.Test
//
//class UserMatchesRoutingTest : BaseRoutingTest() {
//
//    @Test
//    fun testGetUserMatchesEndpoint() = testApplication {
//        // Configure the test application
//        configureTestApplication {
//            routing {
//                with(dependencies) {
//                    route("/user") {
//                        route("/{userId}") {
//                            get("/matches") {
//                                val userId = call.parameters["userId"]?.toIntOrNull()
//                                val user = userId?.let { userService.getUserById(it) }
//                                if (user == null) {
//                                    call.respond(HttpStatusCode.BadRequest, "Invalid user ID")
//                                    return@get
//                                }
//
//                                val results = matchService.getResultsForUser(userId)
//                                call.respond(HttpStatusCode.OK, results)
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        // Register two users
//        val userId1 = userService.register(RegisterRequest(email = "user3@example.com", password = "password123"))
//        val userId2 = userService.register(RegisterRequest(email = "user4@example.com", password = "password123"))
//
//        // Test getting user matches
//        val response = client.get("/user/$userId1/matches")
//
//        // Verify response
//        TestUtils.verifyResponseStatus(response, HttpStatusCode.OK)
//
//        // Test with invalid user ID
//        val invalidResponse = client.get("/user/invalid/matches")
//        TestUtils.verifyResponseStatus(invalidResponse, HttpStatusCode.BadRequest)
//    }
//}