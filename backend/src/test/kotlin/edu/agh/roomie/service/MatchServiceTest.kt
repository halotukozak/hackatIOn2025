package edu.agh.roomie.service

import edu.agh.roomie.rest.model.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.*

class MatchServiceTest {
    private lateinit var database: Database
    private lateinit var userService: UserService
    private lateinit var matchService: MatchService

    private val testUser1 = RegisterRequest("user1@test.com", "password1")
    private val testUser2 = RegisterRequest("user2@test.com", "password2")
    private val testUser3 = RegisterRequest("user3@test.com", "password3")

    private var user1Id: Int = 0
    private var user2Id: Int = 0
    private var user3Id: Int = 0

    @BeforeTest
    fun setup() {
        // Set up in-memory database for testing
        database = Database.connect(
            url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
            user = "root",
            driver = "org.h2.Driver",
            password = ""
        )

        userService = UserService(database)
        matchService = MatchService(database)

        // Create test users
        runBlocking {
            user1Id = userService.register(testUser1)
            user2Id = userService.register(testUser2)
            user3Id = userService.register(testUser3)

            // Set info and preferences for each user
            userService.upsertUserInfo(user1Id, createTestInfo("User 1"))
            userService.upsertUserPreferences(user1Id, createTestPreferences())

            userService.upsertUserInfo(user2Id, createTestInfo("User 2"))
            userService.upsertUserPreferences(user2Id, createTestPreferences())

            userService.upsertUserInfo(user3Id, createTestInfo("User 3"))
            userService.upsertUserPreferences(user3Id, createTestPreferences())
        }
    }

    // Helper methods to create test data
    private fun createTestInfo(name: String): Info {
        return Info(
            fullName = name,
            gender = 1,
            age = 25,
            description = "Test description for $name",
            sleepSchedule = Pair("22:00", "06:00"),
            hobbies = listOf(Hobby.music, Hobby.cooking),
            smoke = 1,
            drink = 2,
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

    @AfterTest
    fun tearDown() {
        // Clean up database after tests
        transaction(database) {
            SchemaUtils.drop(MatchService.InvitationTable, UserService.UsersTable)
        }
    }

    @Test
    fun testRegisterSwipe() = runBlocking {
        // Test registering a swipe with ACK status
        val result = matchService.registerSwipe(user1Id, user2Id, MatchStatus.ACK)

        // The result should be NONE since this is the first interaction
        assertEquals(MatchStatus.NONE, result)

        // Test that user1 has sent a request to user2
        val sentRequests = matchService.getResponseSentForUser(user1Id)
        assertEquals(1, sentRequests.size)
        assertEquals(testUser2.email, sentRequests[0].email)

        // Test that user2 has received a request from user1
        val receivedRequests = matchService.getRequestReceivedForUser(user2Id)
        assertEquals(1, receivedRequests.size)
        assertEquals(testUser1.email, receivedRequests[0].email)
    }

    @Test
    fun testMatchCreation() = runBlocking {
        // User1 swipes right on User2
        matchService.registerSwipe(user1Id, user2Id, MatchStatus.ACK)

        // User2 swipes right on User1
        val result = matchService.registerSwipe(user2Id, user1Id, MatchStatus.ACK)

        // The result should be ACK since both users have swiped right on each other
        assertEquals(MatchStatus.ACK, result)

        // Test that a match has been created
        val matchResults = matchService.getResultsForUser(user1Id)
        assertEquals(1, matchResults.matches.size)
        assertEquals(testUser2.email, matchResults.matches[0].user.email)

        // Test from user2's perspective
        val matchResults2 = matchService.getResultsForUser(user2Id)
        assertEquals(1, matchResults2.matches.size)
        assertEquals(testUser1.email, matchResults2.matches[0].user.email)
    }

    @Test
    fun testGetAvailableMatchesForUser() = runBlocking {
        // Initially, user1 should see user2 and user3 as available matches
        val availableMatches = matchService.getAvailableMatchesForUser(user1Id)
        assertEquals(2, availableMatches.size)
        assertTrue(availableMatches.any { it.email == testUser2.email })
        assertTrue(availableMatches.any { it.email == testUser3.email })

        // After user1 swipes on user2, user2 should still be available
        matchService.registerSwipe(user1Id, user2Id, MatchStatus.ACK)
        val availableMatchesAfterSwipe = matchService.getAvailableMatchesForUser(user1Id)
        assertEquals(1, availableMatchesAfterSwipe.size)
        assertEquals(testUser3.email, availableMatchesAfterSwipe[0].email)
    }

    @Test
    fun testGetResponseSentForUser() = runBlocking {
        // Initially, user1 should have no sent responses
        val initialSentResponses = matchService.getResponseSentForUser(user1Id)
        assertEquals(0, initialSentResponses.size)

        // After user1 swipes on user2, user1 should have one sent response
        matchService.registerSwipe(user1Id, user2Id, MatchStatus.ACK)
        val sentResponses = matchService.getResponseSentForUser(user1Id)
        assertEquals(1, sentResponses.size)
        assertEquals(testUser2.email, sentResponses[0].email)
    }

    @Test
    fun testGetRequestReceivedForUser() = runBlocking {
        // Initially, user2 should have no received requests
        val initialReceivedRequests = matchService.getRequestReceivedForUser(user2Id)
        assertEquals(0, initialReceivedRequests.size)

        // After user1 swipes on user2, user2 should have one received request
        matchService.registerSwipe(user1Id, user2Id, MatchStatus.ACK)
        val receivedRequests = matchService.getRequestReceivedForUser(user2Id)
        assertEquals(1, receivedRequests.size)
        assertEquals(testUser1.email, receivedRequests[0].email)
    }

    @Test
    fun testGetResultsForUser() = runBlocking {
        // Initially, user1 should have no matches, sent requests, or received requests
        val initialResults = matchService.getResultsForUser(user1Id)
        assertEquals(0, initialResults.matches.size)
        assertEquals(0, initialResults.sentRequests.size)
        assertEquals(0, initialResults.receivedRequests.size)

        // User1 swipes right on User2
        matchService.registerSwipe(user1Id, user2Id, MatchStatus.ACK)

        // User1 should now have one sent request
        val resultsAfterSentRequest = matchService.getResultsForUser(user1Id)
        assertEquals(0, resultsAfterSentRequest.matches.size)
        assertEquals(1, resultsAfterSentRequest.sentRequests.size)
        assertEquals(0, resultsAfterSentRequest.receivedRequests.size)
        assertEquals(testUser2.email, resultsAfterSentRequest.sentRequests[0].user.email)

        // User3 swipes right on User1
        matchService.registerSwipe(user3Id, user1Id, MatchStatus.ACK)

        // User1 should now have one sent request and one received request
        val resultsAfterReceivedRequest = matchService.getResultsForUser(user1Id)
        assertEquals(0, resultsAfterReceivedRequest.matches.size)
        assertEquals(1, resultsAfterReceivedRequest.sentRequests.size)
        assertEquals(1, resultsAfterReceivedRequest.receivedRequests.size)
        assertEquals(testUser2.email, resultsAfterReceivedRequest.sentRequests[0].user.email)
        assertEquals(testUser3.email, resultsAfterReceivedRequest.receivedRequests[0].user.email)

        // User2 swipes right on User1
        matchService.registerSwipe(user2Id, user1Id, MatchStatus.ACK)

        // User1 should now have one match and one received request
        val resultsAfterMatch = matchService.getResultsForUser(user1Id)
        assertEquals(1, resultsAfterMatch.matches.size)
        assertEquals(0, resultsAfterMatch.sentRequests.size)
        assertEquals(1, resultsAfterMatch.receivedRequests.size)
        assertEquals(testUser2.email, resultsAfterMatch.matches[0].user.email)
        assertEquals(testUser3.email, resultsAfterMatch.receivedRequests[0].user.email)
    }

    @Test
    fun testNegativeSwipe() = runBlocking {
        // User1 swipes left on User2
        val result = matchService.registerSwipe(user1Id, user2Id, MatchStatus.NACK)

        // The result should be NONE since this is the first interaction
        assertEquals(MatchStatus.NONE, result)

        // User1 should have no sent requests
        val sentRequests = matchService.getResponseSentForUser(user1Id)
        assertEquals(0, sentRequests.size)

        // User2 should have no received requests
        val receivedRequests = matchService.getRequestReceivedForUser(user2Id)
        assertEquals(0, receivedRequests.size)
    }
}
