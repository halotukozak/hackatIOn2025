package edu.agh.roomie.service

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class AuthServiceTest {
    
    @Test
    fun `test generate token`() {
        // Arrange
        val authService = AuthService()
        val userId = 123
        
        // Act
        val token = authService.generateToken(userId)
        
        // Assert
        assertNotNull(token)
        val parts = token.split(".")
        assertEquals(2, parts.size)
    }
    
    @Test
    fun `test validate token`() {
        // Arrange
        val authService = AuthService()
        val userId = 456
        val token = authService.generateToken(userId)
        
        // Act
        val retrievedUserId = authService.validateToken(token)
        
        // Assert
        assertEquals(userId, retrievedUserId)
    }
    
    @Test
    fun `test validate invalid token`() {
        // Arrange
        val authService = AuthService()
        val invalidToken = "invalid.token"
        
        // Act
        val retrievedUserId = authService.validateToken(invalidToken)
        
        // Assert
        assertNull(retrievedUserId)
    }
    
    @Test
    fun `test remove token`() {
        // Arrange
        val authService = AuthService()
        val userId = 789
        val token = authService.generateToken(userId)
        
        // Act
        authService.removeToken(token)
        val retrievedUserId = authService.validateToken(token)
        
        // Assert
        assertNull(retrievedUserId)
    }
}