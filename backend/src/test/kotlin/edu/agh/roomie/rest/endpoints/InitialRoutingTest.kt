package edu.agh.roomie.rest.endpoints

import edu.agh.roomie.rest.model.AdditionalInfoRequest
import edu.agh.roomie.rest.model.Hobby
import edu.agh.roomie.rest.model.RegisterRequest
import edu.agh.roomie.rest.endpoints.TestUtils.performGet
import edu.agh.roomie.rest.endpoints.TestUtils.performPost
import edu.agh.roomie.rest.endpoints.TestUtils.verifyResponse
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

class InitialRoutingTest : BaseRoutingTest() {

    @Test
    fun testGetAvailableHobbiesEndpoint() = testApplication {
        // Configure the test application
        configureTestApplication {
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
        TestUtils.verifyResponseStatus(response, HttpStatusCode.OK)
        TestUtils.verifyResponseContains(response, "music")
        TestUtils.verifyResponseContains(response, "cooking")
    }

    @Test
    fun testGetAvailableDepartmentsEndpoint() = testApplication {
        // Configure the test application
        configureTestApplication {
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
        TestUtils.verifyResponse(response, HttpStatusCode.OK, "WI")
    }

    @Test
    fun testPostAdditionalDataEndpoint() = testApplication {
        // Configure the test application
        configureTestApplication {
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
        val requestBody = """
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
        """.trimIndent()

        val response = client.performPost("/registration/additional-data", requestBody)

        // Verify response
        TestUtils.verifyResponse(response, HttpStatusCode.OK, "User additional data updated successfully")

        // Verify data was updated
        val user = userService.getUserById(userId)
        assertNotNull(user)
        assertEquals("Test", user.info.name)
        assertEquals("User", user.info.surname)
        assertEquals(25, user.info.age)
    }
}
