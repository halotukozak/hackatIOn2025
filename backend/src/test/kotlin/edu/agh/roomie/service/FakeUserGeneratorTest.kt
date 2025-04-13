package edu.agh.roomie.service

import edu.agh.roomie.rest.model.*
import kotlin.test.*

class FakeUserGeneratorTest {
    
    @Test
    fun `test generate fake user`() {
        // Act
        val userId = 123
        val user = FakeUserGenerator.generateFakeUser(userId)
        
        // Assert
        assertEquals(userId, user.id)
        assertTrue(user.email.endsWith("$userId@example.com"))
        assertNotNull(user.info)
        assertNotNull(user.preferences)
    }
    
    @Test
    fun `test generate fake info`() {
        // Act
        val info = FakeUserGenerator.generateFakeInfo()
        
        // Assert
        assertNotNull(info.fullName)
        assertTrue(info.fullName.isNotBlank())
        
        assertTrue(info.gender in 1..3)
        assertTrue(info.age in 18..30)
        
        assertNotNull(info.description)
        assertTrue(info.description.isNotBlank())
        
        assertNotNull(info.sleepSchedule)
        val (sleepStart, sleepEnd) = info.sleepSchedule
        assertTrue(sleepStart.matches(Regex("\\d{2}:\\d{2}")))
        assertTrue(sleepEnd.matches(Regex("\\d{2}:\\d{2}")))
        
        assertNotNull(info.hobbies)
        assertTrue(info.hobbies.isNotEmpty())
        assertTrue(info.hobbies.size in 1..5)
        
        assertTrue(info.smoke in 0..3)
        assertTrue(info.drink in 0..3)
        
        assertTrue(info.personalityType in 1..100)
        assertTrue(info.yearOfStudy in 1..6)
        
        assertNotNull(info.faculty)
        assertTrue(info.faculty in Faculty.entries)
        
        if (info.relationshipStatus != null) {
            assertTrue(info.relationshipStatus in 1..3)
        }
    }
    
    @Test
    fun `test generate fake preferences`() {
        // Act
        val preferences = FakeUserGenerator.generateFakePreferences()
        
        // Assert
        assertNotNull(preferences.sleepScheduleMatters)
        assertNotNull(preferences.hobbiesMatters)
        
        if (preferences.smokingImportance != null) {
            assertTrue(preferences.smokingImportance in 0..3)
        }
        
        if (preferences.drinkImportance != null) {
            assertTrue(preferences.drinkImportance in 0..3)
        }
        
        if (preferences.personalityTypeImportance != null) {
            assertTrue(preferences.personalityTypeImportance in 0..3)
        }
        
        assertNotNull(preferences.yearOfStudyMatters)
        assertNotNull(preferences.facultyMatters)
        
        if (preferences.relationshipStatusImportance != null) {
            assertTrue(preferences.relationshipStatusImportance in 0..3)
        }
    }
    
    @Test
    fun `test random match status`() {
        // Act
        val status = FakeUserGenerator.randomMatchStatus()
        
        // Assert
        assertNotNull(status)
        assertTrue(status in MatchStatus.entries)
    }
    
    @Test
    fun `test multiple fake users have different values`() {
        // Act
        val user1 = FakeUserGenerator.generateFakeUser(1)
        val user2 = FakeUserGenerator.generateFakeUser(2)
        
        // Assert
        assertNotEquals(user1.id, user2.id)
        assertNotEquals(user1.email, user2.email)
        assertNotEquals(user1.info.fullName, user2.info.fullName)
    }
}