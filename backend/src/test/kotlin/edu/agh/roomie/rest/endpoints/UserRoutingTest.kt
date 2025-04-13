package edu.agh.roomie.rest.endpoints

import edu.agh.roomie.rest.model.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UserRoutingTest {

  @Test
  fun `test get user by id endpoint`() = EndpointTestUtils.createTestApplication { dependencies ->
    // Configure the auth routing and user routing
    application {
      with(dependencies) {
        configureAuthRouting()
        configureUserRouting()
        configureInitialRouting()
      }
    }

    // Register a user
    val registerResponse = client.post("/auth/register") {
      contentType(ContentType.Application.Json)
      setBody(
        EndpointTestUtils.jsonConfiguration.encodeToString(
          RegisterRequest.serializer(),
          RegisterRequest("user-test@example.com", "password123")
        )
      )
    }

    val responseBody = registerResponse.bodyAsText()
    val authResponse = EndpointTestUtils.jsonConfiguration.decodeFromString(AuthResponse.serializer(), responseBody)
    val userId = authResponse.userId

    // Add info and preferences to the user
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
      setBody(
        EndpointTestUtils.jsonConfiguration.encodeToString(
          AdditionalInfoRequest.serializer(),
          AdditionalInfoRequest(userId, info)
        )
      )
    }

    client.post("/registration/additional-preferences") {
      contentType(ContentType.Application.Json)
      setBody(
        EndpointTestUtils.jsonConfiguration.encodeToString(
          AdditionalPreferencesRequest.serializer(),
          AdditionalPreferencesRequest(userId, preferences)
        )
      )
    }

    // Test get user by id endpoint
    val response = client.get("/user/$userId")

    // Assert
    assertEquals(HttpStatusCode.OK, response.status)
    val userResponseBody = response.bodyAsText()
    val user = EndpointTestUtils.jsonConfiguration.decodeFromString(User.serializer(), userResponseBody)
    assertEquals(userId, user.id)
    assertEquals("user-test@example.com", user.email)
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
    assertEquals(true, user.preferences.sleepScheduleMatters)
    assertEquals(true, user.preferences.hobbiesMatters)
    assertEquals(2, user.preferences.smokingImportance)
    assertEquals(1, user.preferences.drinkImportance)
    assertEquals(3, user.preferences.personalityTypeImportance)
    assertEquals(false, user.preferences.yearOfStudyMatters)
    assertEquals(true, user.preferences.facultyMatters)
    assertEquals(0, user.preferences.relationshipStatusImportance)
  }

  @Test
  fun `test get user by id with invalid id`() = EndpointTestUtils.createTestApplication { dependencies ->
    // Configure the user routing
    application {
      with(dependencies) {
        configureUserRouting()
      }
    }

    // Test get user by id endpoint with invalid id
    val response = client.get("/user/invalid")

    // Assert
    assertEquals(HttpStatusCode.BadRequest, response.status)
    assertEquals("Invalid user ID format", response.bodyAsText())
  }

  @Test
  fun `test get user by id with non-existent id`() = EndpointTestUtils.createTestApplication { dependencies ->
    // Configure the user routing
    application {
      with(dependencies) {
        configureUserRouting()
      }
    }

    // Test get user by id endpoint with non-existent id
    val response = client.get("/user/999")

    // Assert
    assertEquals(HttpStatusCode.NotFound, response.status)
    assertEquals("User not found", response.bodyAsText())
  }

  @Test
  fun `test get user discover endpoint`() = EndpointTestUtils.createTestApplication { dependencies ->
    // Configure the auth routing, user routing, and initial routing
    application {
      with(dependencies) {
        configureAuthRouting()
        configureUserRouting()
        configureInitialRouting()
      }
    }

    // Register two users with different genders
    val user1Response = client.post("/auth/register") {
      contentType(ContentType.Application.Json)
      setBody(
        EndpointTestUtils.jsonConfiguration.encodeToString(
          RegisterRequest.serializer(),
          RegisterRequest("user1-discover@example.com", "password123")
        )
      )
    }

    val user2Response = client.post("/auth/register") {
      contentType(ContentType.Application.Json)
      setBody(
        EndpointTestUtils.jsonConfiguration.encodeToString(
          RegisterRequest.serializer(),
          RegisterRequest("user2-discover@example.com", "password123")
        )
      )
    }

    val user1ResponseBody = user1Response.bodyAsText()
    val user2ResponseBody = user2Response.bodyAsText()
    val user1Id =
      EndpointTestUtils.jsonConfiguration.decodeFromString(AuthResponse.serializer(), user1ResponseBody).userId
    val user2Id =
      EndpointTestUtils.jsonConfiguration.decodeFromString(AuthResponse.serializer(), user2ResponseBody).userId

    // Add info and preferences to both users with different genders
    val info1 = Info(
      fullName = "User One",
      gender = 1, // Male
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

    val info2 = Info(
      fullName = "User Two",
      gender = 1,
      age = 23,
      description = "Test description",
      sleepSchedule = Pair("23:00", "07:00"),
      hobbies = listOf(Hobby.swimming, Hobby.running),
      smoke = 1,
      drink = 0,
      personalityType = 3,
      yearOfStudy = 2,
      relationshipStatus = 0,
      faculty = Faculty.WIMiR
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
      setBody(
        EndpointTestUtils.jsonConfiguration.encodeToString(
          AdditionalInfoRequest.serializer(),
          AdditionalInfoRequest(user1Id, info1)
        )
      )
    }

    client.post("/registration/additional-preferences") {
      contentType(ContentType.Application.Json)
      setBody(
        EndpointTestUtils.jsonConfiguration.encodeToString(
          AdditionalPreferencesRequest.serializer(),
          AdditionalPreferencesRequest(user1Id, preferences)
        )
      )
    }

    client.post("/registration/additional-info") {
      contentType(ContentType.Application.Json)
      setBody(
        EndpointTestUtils.jsonConfiguration.encodeToString(
          AdditionalInfoRequest.serializer(),
          AdditionalInfoRequest(user2Id, info2)
        )
      )
    }

    client.post("/registration/additional-preferences") {
      contentType(ContentType.Application.Json)
      setBody(
        EndpointTestUtils.jsonConfiguration.encodeToString(
          AdditionalPreferencesRequest.serializer(),
          AdditionalPreferencesRequest(user2Id, preferences)
        )
      )
    }

    // Test get user discover endpoint for user1
    val response = client.get("/user/$user1Id/discover")

    // Assert
    assertEquals(HttpStatusCode.OK, response.status)
    val matchesResponseBody = response.bodyAsText()
    val matches = EndpointTestUtils.jsonConfiguration.decodeFromString(
      kotlinx.serialization.builtins.ListSerializer(Match.serializer()),
      matchesResponseBody
    )
    assertTrue(matches.isNotEmpty())
    assertEquals(user2Id, matches[0].user.id)
  }

  @Test
  fun `test get user matches endpoint`() = EndpointTestUtils.createTestApplication { dependencies ->
    // Configure the auth routing, user routing, match routing, and initial routing
    application {
      with(dependencies) {
        configureAuthRouting()
        configureUserRouting()
        configureMatchRouting()
        configureInitialRouting()
      }
    }

    // Register two users
    val user1Response = client.post("/auth/register") {
      contentType(ContentType.Application.Json)
      setBody(
        EndpointTestUtils.jsonConfiguration.encodeToString(
          RegisterRequest.serializer(),
          RegisterRequest("user1-matches@example.com", "password123")
        )
      )
    }

    val user2Response = client.post("/auth/register") {
      contentType(ContentType.Application.Json)
      setBody(
        EndpointTestUtils.jsonConfiguration.encodeToString(
          RegisterRequest.serializer(),
          RegisterRequest("user2-matches@example.com", "password123")
        )
      )
    }

    val user1ResponseBody = user1Response.bodyAsText()
    val user2ResponseBody = user2Response.bodyAsText()
    val user1Id =
      EndpointTestUtils.jsonConfiguration.decodeFromString(AuthResponse.serializer(), user1ResponseBody).userId
    val user2Id =
      EndpointTestUtils.jsonConfiguration.decodeFromString(AuthResponse.serializer(), user2ResponseBody).userId

    // Add info and preferences to both users
    val info1 = Info(
      fullName = "User One",
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

    val info2 = Info(
      fullName = "User Two",
      gender = 2,
      age = 23,
      description = "Test description",
      sleepSchedule = Pair("23:00", "07:00"),
      hobbies = listOf(Hobby.swimming, Hobby.running),
      smoke = 1,
      drink = 0,
      personalityType = 3,
      yearOfStudy = 2,
      relationshipStatus = 0,
      faculty = Faculty.WIMiR
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
      setBody(
        EndpointTestUtils.jsonConfiguration.encodeToString(
          AdditionalInfoRequest.serializer(),
          AdditionalInfoRequest(user1Id, info1)
        )
      )
    }

    client.post("/registration/additional-preferences") {
      contentType(ContentType.Application.Json)
      setBody(
        EndpointTestUtils.jsonConfiguration.encodeToString(
          AdditionalPreferencesRequest.serializer(),
          AdditionalPreferencesRequest(user1Id, preferences)
        )
      )
    }

    client.post("/registration/additional-info") {
      contentType(ContentType.Application.Json)
      setBody(
        EndpointTestUtils.jsonConfiguration.encodeToString(
          AdditionalInfoRequest.serializer(),
          AdditionalInfoRequest(user2Id, info2)
        )
      )
    }

    client.post("/registration/additional-preferences") {
      contentType(ContentType.Application.Json)
      setBody(
        EndpointTestUtils.jsonConfiguration.encodeToString(
          AdditionalPreferencesRequest.serializer(),
          AdditionalPreferencesRequest(user2Id, preferences)
        )
      )
    }

    // Create a match between the users
    client.post("/matches/swipe/$user1Id/$user2Id?status=ACK")
    client.post("/matches/swipe/$user2Id/$user1Id?status=ACK")

    // Test get user matches endpoint
    val response = client.get("/user/$user1Id/matches")

    // Assert
    assertEquals(HttpStatusCode.OK, response.status)
    val matchResultsResponseBody = response.bodyAsText()
    val matchResults =
      EndpointTestUtils.jsonConfiguration.decodeFromString(MatchResultResponse.serializer(), matchResultsResponseBody)
    assertEquals(1, matchResults.matches.size)
    assertEquals(user2Id, matchResults.matches[0].user.id)
    assertEquals(0, matchResults.sentRequests.size)
    assertEquals(0, matchResults.receivedRequests.size)
  }

  @Test
  fun `test get all users endpoint`() = EndpointTestUtils.createTestApplication { dependencies ->
    // Configure the auth routing and user routing
    application {
      with(dependencies) {
        configureAuthRouting()
        configureUserRouting()
      }
    }

    // Register a few users
    client.post("/auth/register") {
      contentType(ContentType.Application.Json)
      setBody(
        EndpointTestUtils.jsonConfiguration.encodeToString(
          RegisterRequest.serializer(),
          RegisterRequest("user1-all@example.com", "password123")
        )
      )
    }

    client.post("/auth/register") {
      contentType(ContentType.Application.Json)
      setBody(
        EndpointTestUtils.jsonConfiguration.encodeToString(
          RegisterRequest.serializer(),
          RegisterRequest("user2-all@example.com", "password123")
        )
      )
    }

    // Test get all users endpoint
    val response = client.get("/users")

    // Assert
    assertEquals(HttpStatusCode.OK, response.status)
    val usersResponseBody = response.bodyAsText()
    val users = EndpointTestUtils.jsonConfiguration.decodeFromString(
      kotlinx.serialization.builtins.ListSerializer(User.serializer()),
      usersResponseBody
    )
    assertTrue(users.size >= 2)
    assertTrue(users.any { it.email == "user1-all@example.com" })
    assertTrue(users.any { it.email == "user2-all@example.com" })
  }
}
