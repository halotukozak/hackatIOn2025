package edu.agh.roomie.rest.endpoints

import edu.agh.roomie.rest.model.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlin.test.*

class InitialRoutingTest {

    @Test
    fun `test available hobbies endpoint`() = EndpointTestUtils.createTestApplication { dependencies ->
        // Configure the initial routing
        application {
            with(dependencies) {
                configureInitialRouting()
            }
        }

        // Test available hobbies endpoint
        val response = client.get("/registration/available-hobbies")

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)
        val responseBody = response.bodyAsText()
        val hobbies = EndpointTestUtils.jsonConfiguration.decodeFromString(ListSerializer(Hobby.serializer()), responseBody)
        assertNotNull(hobbies)
        assertTrue(hobbies.isNotEmpty())
        assertTrue(hobbies.contains(Hobby.music))
        assertTrue(hobbies.contains(Hobby.cooking))
    }

    @Test
    fun `test available departments endpoint`() = EndpointTestUtils.createTestApplication { dependencies ->
        // Configure the initial routing
        application {
            with(dependencies) {
                configureInitialRouting()
            }
        }

        // Test available departments endpoint
        val response = client.get("/registration/available-departments")

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)
        val responseBody = response.bodyAsText()
        val departments = EndpointTestUtils.jsonConfiguration.decodeFromString(kotlinx.serialization.json.JsonObject.serializer(), responseBody)
        assertNotNull(departments)
        assertTrue(departments.isNotEmpty())
        // Check if WIET and WI keys exist in the JSON object
        assertTrue(departments.containsKey("WIET"))
        assertTrue(departments.containsKey("WI"))
    }

    @Test
    fun `test additional info endpoint`() = EndpointTestUtils.createTestApplication { dependencies ->
        // Configure the initial routing and auth routing
        application {
            with(dependencies) {
                configureAuthRouting()
                configureInitialRouting()
            }
        }

        // Register a user first
        val registerResponse = client.post("/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(EndpointTestUtils.jsonConfiguration.encodeToString(RegisterRequest.serializer(), RegisterRequest("info-test@example.com", "password123")))
        }

        val responseBody = registerResponse.bodyAsText()
        val authResponse = EndpointTestUtils.jsonConfiguration.decodeFromString(AuthResponse.serializer(), responseBody)
        val userId = authResponse.userId

        // Create info data
        val info = Info(
            fullName = "Test User",
            gender = 1,
            age = 25,
            description = "Test description",
            sleepSchedule = Pair("22:00", "06:00"),
            hobbies = listOf(Hobby.music, Hobby.cooking),
            smoke = 0,
            drink = 1,
            personalityType = 2,
            yearOfStudy = 3,
            relationshipStatus = 1,
            faculty = Faculty.WIET
        )

        // Test additional info endpoint
        val response = client.post("/registration/additional-info") {
            contentType(ContentType.Application.Json)
            setBody(EndpointTestUtils.jsonConfiguration.encodeToString(AdditionalInfoRequest.serializer(), AdditionalInfoRequest(userId, info)))
        }

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("User additional data updated successfully", response.bodyAsText())

        // Verify the info was updated by checking the user
        val user = dependencies.userService.getUserById(userId)
        assertNotNull(user)
        assertEquals("Test User", user.info.fullName)
        assertEquals(1, user.info.gender)
        assertEquals(25, user.info.age)
        assertEquals("Test description", user.info.description)
        assertEquals("22:00", user.info.sleepSchedule.first)
        assertEquals("06:00", user.info.sleepSchedule.second)
        assertEquals(2, user.info.hobbies.size)
        assertTrue(user.info.hobbies.contains(Hobby.music))
        assertTrue(user.info.hobbies.contains(Hobby.cooking))
        assertEquals(0, user.info.smoke)
        assertEquals(1, user.info.drink)
        assertEquals(2, user.info.personalityType)
        assertEquals(3, user.info.yearOfStudy)
        assertEquals(1, user.info.relationshipStatus)
        assertEquals(Faculty.WIET, user.info.faculty)
    }

    @Test
    fun `test additional preferences endpoint`() = EndpointTestUtils.createTestApplication { dependencies ->
        // Configure the initial routing and auth routing
        application {
            with(dependencies) {
                configureAuthRouting()
                configureInitialRouting()
            }
        }

        // Register a user first
        val registerResponse = client.post("/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(EndpointTestUtils.jsonConfiguration.encodeToString(RegisterRequest.serializer(), RegisterRequest("preferences-test@example.com", "password123")))
        }

        val responseBody = registerResponse.bodyAsText()
        val authResponse = EndpointTestUtils.jsonConfiguration.decodeFromString(AuthResponse.serializer(), responseBody)
        val userId = authResponse.userId

        // Create preferences data
        val preferences = Preferences(
            sleepScheduleMatters = true,
            hobbiesMatters = true,
            smokingImportance = 2,
            drinkImportance = 1,
            personalityTypeImportance = 3,
            yearOfStudyMatters = false,
            facultyMatters = true,
            relationshipStatusImportance = 0
        )

        // Test additional preferences endpoint
        val response = client.post("/registration/additional-preferences") {
            contentType(ContentType.Application.Json)
            setBody(EndpointTestUtils.jsonConfiguration.encodeToString(AdditionalPreferencesRequest.serializer(), AdditionalPreferencesRequest(userId, preferences)))
        }

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("User preferences updated successfully", response.bodyAsText())

        // Verify the preferences were updated by checking the user
        val user = dependencies.userService.getUserById(userId)
        assertNotNull(user)
        assertEquals(true, user.preferences.sleepScheduleMatters)
        assertEquals(true, user.preferences.hobbiesMatters)
        assertEquals(2, user.preferences.smokingImportance)
        assertEquals(1, user.preferences.drinkImportance)
        assertEquals(3, user.preferences.personalityTypeImportance)
        assertEquals(false, user.preferences.yearOfStudyMatters)
        assertEquals(true, user.preferences.facultyMatters)
        assertEquals(0, user.preferences.relationshipStatusImportance)
    }
}
