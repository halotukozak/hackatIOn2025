package edu.agh.roomie.service

import kotlin.test.*

class AuthServiceTest {
    private lateinit var authService: AuthService

    @BeforeTest
    fun setUp() {
        authService = AuthService()
    }

    @Test
    fun testGenerateToken() {
        // Given
        val userId = 123

        // When
        val token = authService.generateToken(userId)

        // Then
        assertNotNull(token, "Token should not be null")
        assertTrue(token.isNotEmpty(), "Token should not be empty")
        assertTrue(token.contains("."), "Token should contain a dot separator")
    }

    @Test
    fun testValidateToken() {
        // Given
        val userId = 456
        val token = authService.generateToken(userId)

        // When
        val validatedUserId = authService.validateToken(token)

        // Then
        assertNotNull(validatedUserId, "Validated user ID should not be null")
        assertEquals(userId, validatedUserId, "Validated user ID should match the original user ID")
    }

    @Test
    fun testValidateInvalidToken() {
        // Given
        val invalidToken = "invalid.token"

        // When
        val validatedUserId = authService.validateToken(invalidToken)

        // Then
        assertNull(validatedUserId, "Validated user ID should be null for an invalid token")
    }

    @Test
    fun testRemoveToken() {
        // Given
        val userId = 789
        val token = authService.generateToken(userId)

        // When
        authService.removeToken(token)
        val validatedUserId = authService.validateToken(token)

        // Then
        assertNull(validatedUserId, "Validated user ID should be null after token removal")
    }
}