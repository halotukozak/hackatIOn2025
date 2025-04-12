package edu.agh.roomie.service

import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.*

class MatchServiceTest {
    private lateinit var database: Database
    private lateinit var matchService: MatchService

    @BeforeTest
    fun setUp() {
        // Set up an in-memory H2 database for testing
        database = Database.connect(
            url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
            driver = "org.h2.Driver"
        )

        // Initialize service
        matchService = MatchService(database)
    }

    @Test
    fun testSwipeRight_NoMatch() = runBlocking {
        // Given
        val userId = 1
        val swipedUserId = 2

        // When
        val isMatch = matchService.swipeRight(userId, swipedUserId)

        // Then
        assertFalse(isMatch, "Should not be a match on first swipe")

        // Verify the swipe was recorded in the database
        transaction(database) {
            val match = MatchService.MatchEntity.findByUsers(userId, swipedUserId)
            assertNotNull(match, "Match should exist in the database")
            assertEquals(userId, match.userId, "User ID should match")
            assertEquals(swipedUserId, match.matchedUserId, "Matched user ID should match")
            assertFalse(match.isMatched, "Match should not be marked as matched")
        }
    }

    @Test
    fun testSwipeRight_Match() = runBlocking {
        // Given
        val userId1 = 3
        val userId2 = 4

        // First swipe from user1 to user2
        matchService.swipeRight(userId1, userId2)

        // When - Second swipe from user2 to user1
        val isMatch = matchService.swipeRight(userId2, userId1)

        // Then
        assertTrue(isMatch, "Should be a match when both users swipe right")

        // Verify the match was recorded in the database
        transaction(database) {
            val match = MatchService.MatchEntity.findByUsers(userId1, userId2)
            assertNotNull(match, "Match should exist in the database")
            assertTrue(match.isMatched, "Match should be marked as matched")
        }
    }

    @Test
    fun testGetMatches_NoMatches() = runBlocking {
        // Given
        val userId = 5

        // When
        val matches = matchService.getMatches(userId)

        // Then
        assertTrue(matches.isEmpty(), "Should return empty list when no matches exist")
    }

    @Test
    fun testGetMatches_WithMatches() = runBlocking {
        // Given
        val userId = 6
        val matchedUserId1 = 7
        val matchedUserId2 = 8
        val nonMatchedUserId = 9

        // Create matches
        matchService.swipeRight(userId, matchedUserId1)
        matchService.swipeRight(matchedUserId1, userId) // This creates a match

        matchService.swipeRight(userId, matchedUserId2)
        matchService.swipeRight(matchedUserId2, userId) // This creates another match

        matchService.swipeRight(userId, nonMatchedUserId) // This doesn't create a match

        // When
        val matches = matchService.getMatches(userId)

        // Then
        assertEquals(2, matches.size, "Should return 2 matches")
        assertTrue(matches.contains(matchedUserId1), "Should contain first matched user ID")
        assertTrue(matches.contains(matchedUserId2), "Should contain second matched user ID")
        assertFalse(matches.contains(nonMatchedUserId), "Should not contain non-matched user ID")
    }
}