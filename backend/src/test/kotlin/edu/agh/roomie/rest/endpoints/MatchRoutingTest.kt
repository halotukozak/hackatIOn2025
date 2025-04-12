package edu.agh.roomie.rest.endpoints

import edu.agh.roomie.rest.Dependencies
import edu.agh.roomie.rest.model.Faculty
import edu.agh.roomie.rest.model.Hobby
import edu.agh.roomie.rest.model.Info
import edu.agh.roomie.rest.model.Preferences
import edu.agh.roomie.rest.model.RegisterRequest
import edu.agh.roomie.service.AuthService
import edu.agh.roomie.service.MatchService
import edu.agh.roomie.service.UserService
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import io.ktor.serialization.kotlinx.json.*
import org.jetbrains.exposed.sql.Database
import kotlin.test.*

class MatchRoutingTest {
    private lateinit var database: Database
    private lateinit var userService: UserService
    private lateinit var authService: AuthService
    private lateinit var matchService: MatchService
    private lateinit var dependencies: Dependencies

    @BeforeTest
    fun setUp() {
        // Set up an in-memory H2 database for testing
        database = Database.connect(
            url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
            driver = "org.h2.Driver"
        )

        // Initialize services
        userService = UserService(database)
        authService = AuthService()
        matchService = MatchService(database)
        
        // Create dependencies
        dependencies = Dependencies(
            database = database,
            userService = userService,
            authService = authService,
            matchService = matchService
        )
    }
    
    // Helper methods to create test data
    private fun createTestInfo(): Info {
        return Info(
            name = "Test",
            surname = "User",
            age = 25,
            description = "Test description",
            sleepSchedule = Pair(22, 6),
            hobbies = listOf(Hobby.music, Hobby.cooking),
            smoke = false,
            drink = true,
            personalityType = 1,
            yearOfStudy = 3,
            faculty = Faculty.WI,
            relationshipStatus = 1
        )
    }

    private fun createTestPreferences(): Preferences {
        return Preferences(
            sleepScheduleMatters = true,
            hobbiesMatters = true,
            smokingImportance = 3,
            drinkImportance = 2,
            personalityTypeImportance = 1,
            yearOfStudyMatters = false,
            facultyMatters = true,
            relationshipStatusImportance = 0
        )
    }

    @Test
    fun testSwipeEndpoint() = testApplication {
        // Configure the test application
        application {
            install(ContentNegotiation) {
                json()
            }
            routing {
                with(dependencies) {
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
                                edu.agh.roomie.rest.model.SwipeResponse(
                                    isMatch = isMatch,
                                    message = if (isMatch) "It's a match!" else "Swipe recorded"
                                )
                            )
                        }
                    }
                }
            }
        }

        // Register two users
        val userId1 = userService.register(RegisterRequest(email = "user1@example.com", password = "password123"))
        val userId2 = userService.register(RegisterRequest(email = "user2@example.com", password = "password123"))

        // Create info and preferences for both users
        val info = createTestInfo()
        val preferences = createTestPreferences()
        userService.upsertUserAdditionalData(userId1, info, preferences)
        userService.upsertUserAdditionalData(userId2, info, preferences)

        // Test swipe endpoint
        val response = client.post("/matches/swipe/$userId1/$userId2")

        // Verify response
        assertEquals(HttpStatusCode.OK, response.status)
        val responseText = response.bodyAsText()
        assertTrue(responseText.contains("isMatch"))
        assertTrue(responseText.contains("message"))
        
        // Test invalid user IDs
        val invalidResponse = client.post("/matches/swipe/invalid/$userId2")
        assertEquals(HttpStatusCode.BadRequest, invalidResponse.status)
        
        // Test non-existent user
        val nonExistentResponse = client.post("/matches/swipe/999/$userId2")
        assertEquals(HttpStatusCode.NotFound, nonExistentResponse.status)
    }

    @Test
    fun testGetNotificationsEndpoint() = testApplication {
        // Configure the test application
        application {
            install(ContentNegotiation) {
                json()
            }
            routing {
                with(dependencies) {
                    route("/matches") {
                        get("/notifications/{userId}") {
                            val userId = call.parameters["userId"]?.toIntOrNull()

                            if (userId == null) {
                                call.respond(HttpStatusCode.BadRequest, "Invalid user ID")
                                return@get
                            }

                            val matchedUserIds = matchService.getMatches(userId)

                            val matchedUsers = matchedUserIds.mapNotNull { matchedUserId ->
                                userService.getUserById(matchedUserId)?.let { user ->
                                    edu.agh.roomie.rest.model.MatchResponse(
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
            }
        }

        // Register two users
        val userId1 = userService.register(RegisterRequest(email = "user1@example.com", password = "password123"))
        val userId2 = userService.register(RegisterRequest(email = "user2@example.com", password = "password123"))

        // Create info and preferences for both users
        val info = createTestInfo()
        val preferences = createTestPreferences()
        userService.upsertUserAdditionalData(userId1, info, preferences)
        userService.upsertUserAdditionalData(userId2, info, preferences)

        // Create a match between the users
        matchService.swipeRight(userId1, userId2)
        matchService.swipeRight(userId2, userId1)

        // Test get notifications endpoint
        val response = client.get("/matches/notifications/$userId1")

        // Verify response
        assertEquals(HttpStatusCode.OK, response.status)
        val responseText = response.bodyAsText()
        assertTrue(responseText.contains("userId"))
        assertTrue(responseText.contains("name"))
        assertTrue(responseText.contains("surname"))
        
        // Test invalid user ID
        val invalidResponse = client.get("/matches/notifications/invalid")
        assertEquals(HttpStatusCode.BadRequest, invalidResponse.status)
    }
}