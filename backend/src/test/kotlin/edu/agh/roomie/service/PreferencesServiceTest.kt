package edu.agh.roomie.service

import edu.agh.roomie.TestUtils
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class PreferencesServiceTest {
    
    @Test
    fun `test preferences service initialization`() {
        // Arrange & Act
        val database = TestUtils.createTestDatabase()
        val preferencesService = PreferencesService(database)
        
        // Assert - if no exception is thrown, the initialization was successful
        assertNotNull(preferencesService)
        
        // Create the table since PreferencesService doesn't do it automatically
        transaction(database) {
            SchemaUtils.create(PreferencesService.PreferencesTable)
        }
    }
    
    @Test
    fun `test create and retrieve preferences entity`() {
        // Arrange
        val database = TestUtils.createTestDatabase()
        val preferencesService = PreferencesService(database)
        
        // Create the table
        transaction(database) {
            SchemaUtils.create(PreferencesService.PreferencesTable)
        }
        
        // Act
        val preferencesEntity = transaction(database) {
            PreferencesService.PreferencesEntity.new {
                sleepScheduleMatters = true
                hobbiesMatters = true
                smokingImportance = 2
                drinkImportance = 1
                personalityTypeImportance = 3
                yearOfStudyMatters = false
                facultyMatters = true
                relationshipStatusImportance = 0
            }
        }
        
        // Assert
        transaction(database) {
            val retrievedPreferences = PreferencesService.PreferencesEntity.findById(preferencesEntity.id)
            assertNotNull(retrievedPreferences)
            assertEquals(true, retrievedPreferences.sleepScheduleMatters)
            assertEquals(true, retrievedPreferences.hobbiesMatters)
            assertEquals(2, retrievedPreferences.smokingImportance)
            assertEquals(1, retrievedPreferences.drinkImportance)
            assertEquals(3, retrievedPreferences.personalityTypeImportance)
            assertEquals(false, retrievedPreferences.yearOfStudyMatters)
            assertEquals(true, retrievedPreferences.facultyMatters)
            assertEquals(0, retrievedPreferences.relationshipStatusImportance)
        }
    }
    
    @Test
    fun `test update preferences entity`() {
        // Arrange
        val database = TestUtils.createTestDatabase()
        val preferencesService = PreferencesService(database)
        
        // Create the table
        transaction(database) {
            SchemaUtils.create(PreferencesService.PreferencesTable)
        }
        
        val preferencesEntity = transaction(database) {
            PreferencesService.PreferencesEntity.new {
                sleepScheduleMatters = false
                hobbiesMatters = false
                smokingImportance = null
                drinkImportance = null
                personalityTypeImportance = 1
                yearOfStudyMatters = true
                facultyMatters = false
                relationshipStatusImportance = null
            }
        }
        
        // Act
        transaction(database) {
            val retrievedPreferences = PreferencesService.PreferencesEntity.findById(preferencesEntity.id)
            assertNotNull(retrievedPreferences)
            retrievedPreferences.sleepScheduleMatters = true
            retrievedPreferences.smokingImportance = 2
            retrievedPreferences.facultyMatters = true
        }
        
        // Assert
        transaction(database) {
            val updatedPreferences = PreferencesService.PreferencesEntity.findById(preferencesEntity.id)
            assertNotNull(updatedPreferences)
            assertEquals(true, updatedPreferences.sleepScheduleMatters)
            assertEquals(false, updatedPreferences.hobbiesMatters)
            assertEquals(2, updatedPreferences.smokingImportance)
            assertNull(updatedPreferences.drinkImportance)
            assertEquals(1, updatedPreferences.personalityTypeImportance)
            assertEquals(true, updatedPreferences.yearOfStudyMatters)
            assertEquals(true, updatedPreferences.facultyMatters)
            assertNull(updatedPreferences.relationshipStatusImportance)
        }
    }
}