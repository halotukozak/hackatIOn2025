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
//class UserAdditionalRoutingTest : BaseRoutingTest() {
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
//        // Create info and preferences for both users
//        val info = createTestInfo()
//        val preferences = createTestPreferences()
//        userService.upsertUserInfo(userId1, info)
//        userService.upsertUserPreferences(userId1, preferences)
//        userService.upsertUserInfo(userId2, info)
//        userService.upsertUserPreferences(userId2, preferences)
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
//        // Create info and preferences for both users
//        val info = createTestInfo()
//        val preferences = createTestPreferences()
//        userService.upsertUserInfo(userId1, info)
//        userService.upsertUserPreferences(userId1, preferences)
//        userService.upsertUserInfo(userId2, info)
//        userService.upsertUserPreferences(userId2, preferences)
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
//
//    @Test
//    fun testGetAllUsersEndpoint() = testApplication {
//        // Configure the test application
//        configureTestApplication {
//            routing {
//                with(dependencies) {
//                    get("/users") {
//                        val users = userService.getAllUsers()
//                        call.respond(HttpStatusCode.OK, users)
//                    }
//                }
//            }
//        }
//
//        // Register a few users
//        userService.register(RegisterRequest(email = "user5@example.com", password = "password123"))
//        userService.register(RegisterRequest(email = "user6@example.com", password = "password123"))
//
//        // Test getting all users
//        val response = client.get("/users")
//
//        // Verify response
//        TestUtils.verifyResponseStatus(response, HttpStatusCode.OK)
//        TestUtils.verifyResponseContains(response, "user5@example.com")
//        TestUtils.verifyResponseContains(response, "user6@example.com")
//    }
//}