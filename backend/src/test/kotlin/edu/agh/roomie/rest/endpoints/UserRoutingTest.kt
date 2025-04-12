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

class UserRoutingTest {
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
    fun testGetUserByIdEndpoint() = testApplication {
        // Configure the test application
        application {
            install(ContentNegotiation) {
                json()
            }
            routing {
                with(dependencies) {
                    route("/user") {
                        get("/{userId}") {
                            val userId = call.parameters["userId"]?.toIntOrNull()
                            if (userId == null) {
                                call.respond(HttpStatusCode.BadRequest, "Invalid user ID format")
                                return@get
                            }

                            val useFake = call.request.queryParameters["fake"]?.toBoolean() ?: false

                            if (useFake) {
                                val fakeUser = edu.agh.roomie.service.FakeUserGenerator.generateFakeUser(userId)
                                call.respond(HttpStatusCode.OK, fakeUser)
                            } else {
                                val user = userService.getUserById(userId)
                                if (user != null) {
                                    call.respond(HttpStatusCode.OK, user)
                                } else {
                                    val fakeUser = edu.agh.roomie.service.FakeUserGenerator.generateFakeUser(userId)
                                    call.respond(HttpStatusCode.OK, fakeUser)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Register a user
        val userId = userService.register(RegisterRequest(email = "user@example.com", password = "password123"))

        // Create info and preferences for the user
        val info = createTestInfo()
        val preferences = createTestPreferences()
        userService.upsertUserAdditionalData(userId, info, preferences)

        // Test getting a real user
        val response = client.get("/user/$userId")

        // Verify response
        assertEquals(HttpStatusCode.OK, response.status)
        val responseText = response.bodyAsText()
        assertTrue(responseText.contains("user@example.com"))

        // Test getting a fake user
        val fakeResponse = client.get("/user/$userId?fake=true")

        // Verify response
        assertEquals(HttpStatusCode.OK, fakeResponse.status)
        val fakeResponseText = fakeResponse.bodyAsText()
        assertFalse(fakeResponseText.contains("user@example.com"))

        // Test invalid user ID
        val invalidResponse = client.get("/user/invalid")

        // Verify response
        assertEquals(HttpStatusCode.BadRequest, invalidResponse.status)
    }

    @Test
    fun testGetFakeUsersEndpoint() = testApplication {
        // Configure the test application
        application {
            install(ContentNegotiation) {
                json()
            }
            routing {
                with(dependencies) {
                    route("/user") {
                        get("/fake") {
                            val count = call.request.queryParameters["count"]?.toIntOrNull() ?: 10
                            val fakeUsers = List(count) { index ->
                                edu.agh.roomie.service.FakeUserGenerator.generateFakeUser(1000 + index)
                            }
                            call.respond(HttpStatusCode.OK, fakeUsers)
                        }
                    }
                }
            }
        }

        // Test getting default number of fake users
        val response = client.get("/user/fake")

        // Verify response
        assertEquals(HttpStatusCode.OK, response.status)
        val responseText = response.bodyAsText()
        assertTrue(responseText.contains("[")) // Should be an array
        assertTrue(responseText.contains("]"))

        // Test getting specific number of fake users
        val countResponse = client.get("/user/fake?count=5")

        // Verify response
        assertEquals(HttpStatusCode.OK, countResponse.status)
        val countResponseText = countResponse.bodyAsText()
        assertTrue(countResponseText.contains("["))
        assertTrue(countResponseText.contains("]"))
    }
}
