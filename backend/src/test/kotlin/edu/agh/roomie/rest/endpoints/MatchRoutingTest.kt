package edu.agh.roomie.rest.endpoints

import edu.agh.roomie.rest.model.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import kotlin.test.*
import kotlinx.serialization.builtins.*

class MatchRoutingTest {

    @Test
    fun `test swipe endpoint with ACK status`() = EndpointTestUtils.createTestApplication { dependencies ->
        // Configure the auth routing and match routing
        application {
            with(dependencies) {
                configureAuthRouting()
                configureMatchRouting()
                configureInitialRouting()
            }
        }

        // Register two users
        val user1Response = client.post("/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(EndpointTestUtils.jsonConfiguration.encodeToString(RegisterRequest.serializer(), RegisterRequest("user1@example.com", "password123")))
        }

        val user2Response = client.post("/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(EndpointTestUtils.jsonConfiguration.encodeToString(RegisterRequest.serializer(), RegisterRequest("user2@example.com", "password123")))
        }

        val user1ResponseBody = user1Response.bodyAsText()
        val user2ResponseBody = user2Response.bodyAsText()
        val user1Id = EndpointTestUtils.jsonConfiguration.decodeFromString(AuthResponse.serializer(), user1ResponseBody).userId
        val user2Id = EndpointTestUtils.jsonConfiguration.decodeFromString(AuthResponse.serializer(), user2ResponseBody).userId

        // Add info and preferences to both users
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

        client.post("/registration/additional-info") {
            contentType(ContentType.Application.Json)
            setBody(EndpointTestUtils.jsonConfiguration.encodeToString(AdditionalInfoRequest.serializer(), AdditionalInfoRequest(user1Id, info)))
        }

        client.post("/registration/additional-preferences") {
            contentType(ContentType.Application.Json)
            setBody(EndpointTestUtils.jsonConfiguration.encodeToString(AdditionalPreferencesRequest.serializer(), AdditionalPreferencesRequest(user1Id, preferences)))
        }

        client.post("/registration/additional-info") {
            contentType(ContentType.Application.Json)
            setBody(EndpointTestUtils.jsonConfiguration.encodeToString(AdditionalInfoRequest.serializer(), AdditionalInfoRequest(user2Id, info)))
        }

        client.post("/registration/additional-preferences") {
            contentType(ContentType.Application.Json)
            setBody(EndpointTestUtils.jsonConfiguration.encodeToString(AdditionalPreferencesRequest.serializer(), AdditionalPreferencesRequest(user2Id, preferences)))
        }

        // Test swipe endpoint with ACK status
        val response = client.post("/matches/swipe/$user1Id/$user2Id?status=ACK")

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)
        val responseBody = response.bodyAsText()
        val matchStatus = EndpointTestUtils.jsonConfiguration.decodeFromString(MatchStatus.serializer(), responseBody)
        assertEquals(MatchStatus.NONE, matchStatus)
    }

    @Test
    fun `test swipe endpoint with NACK status`() = EndpointTestUtils.createTestApplication { dependencies ->
        // Configure the auth routing and match routing
        application {
            with(dependencies) {
                configureAuthRouting()
                configureMatchRouting()
                configureInitialRouting()
            }
        }

        // Register two users
        val user1Response = client.post("/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(EndpointTestUtils.jsonConfiguration.encodeToString(RegisterRequest.serializer(), RegisterRequest("user3@example.com", "password123")))
        }

        val user2Response = client.post("/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(EndpointTestUtils.jsonConfiguration.encodeToString(RegisterRequest.serializer(), RegisterRequest("user4@example.com", "password123")))
        }

        val user1ResponseBody = user1Response.bodyAsText()
        val user2ResponseBody = user2Response.bodyAsText()
        val user1Id = EndpointTestUtils.jsonConfiguration.decodeFromString(AuthResponse.serializer(), user1ResponseBody).userId
        val user2Id = EndpointTestUtils.jsonConfiguration.decodeFromString(AuthResponse.serializer(), user2ResponseBody).userId

        // Add info and preferences to both users
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

        client.post("/registration/additional-info") {
            contentType(ContentType.Application.Json)
            setBody(EndpointTestUtils.jsonConfiguration.encodeToString(AdditionalInfoRequest.serializer(), AdditionalInfoRequest(user1Id, info)))
        }

        client.post("/registration/additional-preferences") {
            contentType(ContentType.Application.Json)
            setBody(EndpointTestUtils.jsonConfiguration.encodeToString(AdditionalPreferencesRequest.serializer(), AdditionalPreferencesRequest(user1Id, preferences)))
        }

        client.post("/registration/additional-info") {
            contentType(ContentType.Application.Json)
            setBody(EndpointTestUtils.jsonConfiguration.encodeToString(AdditionalInfoRequest.serializer(), AdditionalInfoRequest(user2Id, info)))
        }

        client.post("/registration/additional-preferences") {
            contentType(ContentType.Application.Json)
            setBody(EndpointTestUtils.jsonConfiguration.encodeToString(AdditionalPreferencesRequest.serializer(), AdditionalPreferencesRequest(user2Id, preferences)))
        }

        // Test swipe endpoint with NACK status
        val response = client.post("/matches/swipe/$user1Id/$user2Id?status=NACK")

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)
        val responseBody = response.bodyAsText()
        val matchStatus = EndpointTestUtils.jsonConfiguration.decodeFromString(MatchStatus.serializer(), responseBody)
        assertEquals(MatchStatus.NONE, matchStatus)
    }

    @Test
    fun `test swipe endpoint with mutual ACK status`() = EndpointTestUtils.createTestApplication { dependencies ->
        // Configure the auth routing and match routing
        application {
            with(dependencies) {
                configureAuthRouting()
                configureMatchRouting()
                configureInitialRouting()
            }
        }

        // Register two users
        val user1Response = client.post("/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(EndpointTestUtils.jsonConfiguration.encodeToString(RegisterRequest.serializer(), RegisterRequest("user5@example.com", "password123")))
        }

        val user2Response = client.post("/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(EndpointTestUtils.jsonConfiguration.encodeToString(RegisterRequest.serializer(), RegisterRequest("user6@example.com", "password123")))
        }

        val user1ResponseBody = user1Response.bodyAsText()
        val user2ResponseBody = user2Response.bodyAsText()
        val user1Id = EndpointTestUtils.jsonConfiguration.decodeFromString(AuthResponse.serializer(), user1ResponseBody).userId
        val user2Id = EndpointTestUtils.jsonConfiguration.decodeFromString(AuthResponse.serializer(), user2ResponseBody).userId

        // Add info and preferences to both users
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

        client.post("/registration/additional-info") {
            contentType(ContentType.Application.Json)
            setBody(EndpointTestUtils.jsonConfiguration.encodeToString(AdditionalInfoRequest.serializer(), AdditionalInfoRequest(user1Id, info)))
        }

        client.post("/registration/additional-preferences") {
            contentType(ContentType.Application.Json)
            setBody(EndpointTestUtils.jsonConfiguration.encodeToString(AdditionalPreferencesRequest.serializer(), AdditionalPreferencesRequest(user1Id, preferences)))
        }

        client.post("/registration/additional-info") {
            contentType(ContentType.Application.Json)
            setBody(EndpointTestUtils.jsonConfiguration.encodeToString(AdditionalInfoRequest.serializer(), AdditionalInfoRequest(user2Id, info)))
        }

        client.post("/registration/additional-preferences") {
            contentType(ContentType.Application.Json)
            setBody(EndpointTestUtils.jsonConfiguration.encodeToString(AdditionalPreferencesRequest.serializer(), AdditionalPreferencesRequest(user2Id, preferences)))
        }

        // User 1 swipes right on User 2
        client.post("/matches/swipe/$user1Id/$user2Id?status=ACK")

        // User 2 swipes right on User 1
        val response = client.post("/matches/swipe/$user2Id/$user1Id?status=ACK")

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)
        val responseBody = response.bodyAsText()
        val matchStatus = EndpointTestUtils.jsonConfiguration.decodeFromString(MatchStatus.serializer(), responseBody)
        assertEquals(MatchStatus.ACK, matchStatus)

        // Verify that they are matched
        val matchResults = dependencies.matchService.getResultsForUser(user1Id)
        assertEquals(1, matchResults.matches.size)
        assertEquals(user2Id, matchResults.matches[0].user.id)
    }

    @Test
    fun `test swipe endpoint with invalid user IDs`() = EndpointTestUtils.createTestApplication { dependencies ->
        // Configure the match routing
        application {
            with(dependencies) {
                configureMatchRouting()
            }
        }

        // Test swipe endpoint with invalid user IDs
        val response = client.post("/matches/swipe/invalid/user?status=ACK")

        // Assert
        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals("Invalid user IDs", response.bodyAsText())
    }

    @Test
    fun `test swipe endpoint with missing status parameter`() = EndpointTestUtils.createTestApplication { dependencies ->
        // Configure the match routing
        application {
            with(dependencies) {
                configureMatchRouting()
            }
        }

        // Test swipe endpoint with missing status parameter
        val response = client.post("/matches/swipe/1/2")

        // Assert
        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals("Missing status parameter", response.bodyAsText())
    }
}
