package edu.agh.roomie.service

import edu.agh.roomie.TestUtils
import edu.agh.roomie.rest.model.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.*

class MatchServiceTest {

    @Test
    fun `test match service initialization`() {
        // Arrange & Act
        val database = TestUtils.createTestDatabase()
        val matchService = MatchService(database)

        // Assert - if no exception is thrown, the initialization was successful
        assertNotNull(matchService)
    }

    @Test
    fun `test register swipe and get results`() {
        // Arrange
        val database = TestUtils.createTestDatabase()

        // Create necessary tables
        transaction(database) {
            SchemaUtils.create(InfoService.InfosTable)
            SchemaUtils.create(PreferencesService.PreferencesTable)
            SchemaUtils.create(UserService.UsersTable)
            SchemaUtils.create(MatchService.InvitationTable)
        }

        val infoService = InfoService(database)
        val preferencesService = PreferencesService(database)
        val userService = UserService(database)
        val matchService = MatchService(database)

        // Create two users
        val user1Id = userService.register(RegisterRequest("user1@example.com", "password123"))
        val user2Id = userService.register(RegisterRequest("user2@example.com", "password123"))

        // Add info and preferences to users
        val info1 = Info(
            fullName = "User One",
            gender = 1,
            age = 25,
            description = "Test description 1",
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
            description = "Test description 2",
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

        userService.upsertUserInfo(user1Id, info1)
        userService.upsertUserInfo(user2Id, info2)
        userService.upsertUserPreferences(user1Id, preferences)
        userService.upsertUserPreferences(user2Id, preferences)

        // Act - User 1 swipes right on User 2
        val responseStatus = matchService.registerSwipe(user1Id, user2Id, MatchStatus.ACK)

        // Assert
        assertEquals(MatchStatus.NONE, responseStatus)

        // Get available matches for User 2
        val availableMatches = matchService.getAvailableMatchesForUser(user2Id)
        assertTrue(availableMatches.isNotEmpty())
        assertTrue(availableMatches.any { it.id == user1Id })

        // User 2 swipes right on User 1
        matchService.registerSwipe(user2Id, user1Id, MatchStatus.ACK)

        // Get results for User 1
        val results1 = matchService.getResultsForUser(user1Id)
        assertEquals(1, results1.matches.size)
        assertEquals(user2Id, results1.matches[0].user.id)
        assertEquals(0, results1.sentRequests.size)
        assertEquals(0, results1.receivedRequests.size)

        // Get results for User 2
        val results2 = matchService.getResultsForUser(user2Id)
        assertEquals(1, results2.matches.size)
        assertEquals(user1Id, results2.matches[0].user.id)
        assertEquals(0, results2.sentRequests.size)
        assertEquals(0, results2.receivedRequests.size)
    }

    @Test
    fun `test register swipe with rejection`() {
        // Arrange
        val database = TestUtils.createTestDatabase()

        // Create necessary tables
        transaction(database) {
            SchemaUtils.create(InfoService.InfosTable)
            SchemaUtils.create(PreferencesService.PreferencesTable)
            SchemaUtils.create(UserService.UsersTable)
            SchemaUtils.create(MatchService.InvitationTable)
        }

        val infoService = InfoService(database)
        val preferencesService = PreferencesService(database)
        val userService = UserService(database)
        val matchService = MatchService(database)

        // Create two users
        val user1Id = userService.register(RegisterRequest("user3@example.com", "password123"))
        val user2Id = userService.register(RegisterRequest("user4@example.com", "password123"))

        // Add info and preferences to users
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

        userService.upsertUserInfo(user1Id, info)
        userService.upsertUserInfo(user2Id, info)
        userService.upsertUserPreferences(user1Id, preferences)
        userService.upsertUserPreferences(user2Id, preferences)

        // Act - User 1 swipes right on User 2
        matchService.registerSwipe(user1Id, user2Id, MatchStatus.ACK)

        // User 2 swipes left on User 1
        matchService.registerSwipe(user2Id, user1Id, MatchStatus.NACK)

        // Assert
        val results1 = matchService.getResultsForUser(user1Id)
        assertEquals(0, results1.matches.size)
        assertEquals(0, results1.sentRequests.size)
        assertEquals(0, results1.receivedRequests.size)

        val results2 = matchService.getResultsForUser(user2Id)
        assertEquals(0, results2.matches.size)
        assertEquals(0, results2.sentRequests.size)
        assertEquals(0, results2.receivedRequests.size)
    }

    @Test
    fun `test get request received for user`() {
        // Arrange
        val database = TestUtils.createTestDatabase()

        // Create necessary tables
        transaction(database) {
            SchemaUtils.create(InfoService.InfosTable)
            SchemaUtils.create(PreferencesService.PreferencesTable)
            SchemaUtils.create(UserService.UsersTable)
            SchemaUtils.create(MatchService.InvitationTable)
        }

        val infoService = InfoService(database)
        val preferencesService = PreferencesService(database)
        val userService = UserService(database)
        val matchService = MatchService(database)

        // Create two users
        val user1Id = userService.register(RegisterRequest("user5@example.com", "password123"))
        val user2Id = userService.register(RegisterRequest("user6@example.com", "password123"))

        // Add info and preferences to users
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

        userService.upsertUserInfo(user1Id, info)
        userService.upsertUserInfo(user2Id, info)
        userService.upsertUserPreferences(user1Id, preferences)
        userService.upsertUserPreferences(user2Id, preferences)

        // Act - User 1 swipes right on User 2
        matchService.registerSwipe(user1Id, user2Id, MatchStatus.ACK)

        // Assert
        val receivedRequests = matchService.getRequestReceivedForUser(user2Id)
        assertEquals(1, receivedRequests.size)
        assertEquals(user1Id, receivedRequests[0].id)
    }
}
