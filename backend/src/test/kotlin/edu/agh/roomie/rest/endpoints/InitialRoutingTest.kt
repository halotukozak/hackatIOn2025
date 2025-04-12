package edu.agh.roomie.rest.endpoints

import edu.agh.roomie.rest.Dependencies
import edu.agh.roomie.rest.model.AdditionalInfoRequest
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
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import io.ktor.serialization.kotlinx.json.*
import org.jetbrains.exposed.sql.Database
import kotlin.test.*

class InitialRoutingTest {
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
    fun testGetAvailableHobbiesEndpoint() = testApplication {
        // Configure the test application
        application {
            install(ContentNegotiation) {
                json()
            }
            routing {
                with(dependencies) {
                    route("/registration") {
                        get("/available-hobbies") {
                            call.respond(HttpStatusCode.OK, Hobby.entries.toList())
                        }
                    }
                }
            }
        }

        // Test getting available hobbies
        val response = client.get("/registration/available-hobbies")

        // Verify response
        assertEquals(HttpStatusCode.OK, response.status)
        val responseText = response.bodyAsText()
        assertTrue(responseText.contains("music"))
        assertTrue(responseText.contains("cooking"))
    }

    @Test
    fun testGetAvailableDepartmentsEndpoint() = testApplication {
        // Configure the test application
        application {
            install(ContentNegotiation) {
                json()
            }
            routing {
                with(dependencies) {
                    route("/registration") {
                        get("/available-departments") {
                            call.respond(HttpStatusCode.OK, edu.agh.roomie.rest.model.Faculties.all)
                        }
                    }
                }
            }
        }

        // Test getting available departments
        val response = client.get("/registration/available-departments")

        // Verify response
        assertEquals(HttpStatusCode.OK, response.status)
        val responseText = response.bodyAsText()
        assertTrue(responseText.contains("WI"))
    }

    @Test
    fun testPostAdditionalDataEndpoint() = testApplication {
        // Configure the test application
        application {
            install(ContentNegotiation) {
                json()
            }
            routing {
                with(dependencies) {
                    route("/registration") {
                        post("/additional-data") {
                            val additionalInfoRequest = call.receive<AdditionalInfoRequest>()
                            val userId = additionalInfoRequest.userId
                            userService.upsertUserAdditionalData(
                                userId,
                                additionalInfoRequest.info,
                                additionalInfoRequest.preferences,
                            )?.let {
                                call.respond(HttpStatusCode.OK, "User additional data updated successfully")
                            } ?: run {
                                call.respond(HttpStatusCode.BadRequest, "Failed to update user additional data")
                            }
                        }
                    }
                }
            }
        }

        // Register a user
        val userId = userService.register(RegisterRequest(email = "additionaldata@example.com", password = "password123"))

        // Create info and preferences
        val info = createTestInfo()
        val preferences = createTestPreferences()

        // Test posting additional data
        val response = client.post("/registration/additional-data") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                    "userId": $userId,
                    "info": {
                        "name": "Test",
                        "surname": "User",
                        "age": 25,
                        "description": "Test description",
                        "sleepSchedule": {"first": 22, "second": 6},
                        "hobbies": ["music", "cooking"],
                        "smoke": false,
                        "drink": true,
                        "personalityType": 1,
                        "yearOfStudy": 3,
                        "faculty": "WI",
                        "relationshipStatus": 1
                    },
                    "preferences": {
                        "sleepScheduleMatters": true,
                        "hobbiesMatters": true,
                        "smokingImportance": 3,
                        "drinkImportance": 2,
                        "personalityTypeImportance": 1,
                        "yearOfStudyMatters": false,
                        "facultyMatters": true,
                        "relationshipStatusImportance": 0
                    }
                }
            """.trimIndent())
        }

        // Verify response
        assertEquals(HttpStatusCode.OK, response.status)
        val responseText = response.bodyAsText()
        assertTrue(responseText.contains("User additional data updated successfully"))

        // Verify data was updated
        val user = userService.getUserById(userId)
        assertNotNull(user)
        assertEquals("Test", user.info.name)
        assertEquals("User", user.info.surname)
        assertEquals(25, user.info.age)
    }
}