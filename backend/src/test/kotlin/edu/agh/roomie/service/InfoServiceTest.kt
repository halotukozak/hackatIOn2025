package edu.agh.roomie.service

import edu.agh.roomie.TestUtils
import edu.agh.roomie.rest.model.Faculty
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class InfoServiceTest {

    @Test
    fun `test info service initialization`() {
        // Arrange & Act
        val database = TestUtils.createTestDatabase()
        val infoService = InfoService(database)

        // Assert - if no exception is thrown, the initialization was successful
        assertNotNull(infoService)
    }

    @Test
    fun `test create and retrieve info entity`() {
        // Arrange
        val database = TestUtils.createTestDatabase()
        val infoService = InfoService(database)

        // Act
        val infoEntity = transaction(database) {
            InfoService.InfoEntity.new {
                fullName = "John Doe"
                gender = 1
                age = 25
                description = "Test description"
                sleepStart = "22:00"
                sleepEnd = "06:00"
                hobbies = listOf("Reading", "Gaming")
                smoke = 0
                drink = 1
                personalityType = 2
                yearOfStudy = 3
                relationshipStatus = 1
                faculty = Faculty.WIET
            }
        }

        // Assert
        transaction(database) {
            val retrievedInfo = InfoService.InfoEntity.findById(infoEntity.id)
            assertNotNull(retrievedInfo)
            assertEquals("John Doe", retrievedInfo.fullName)
            assertEquals(1, retrievedInfo.gender)
            assertEquals(25, retrievedInfo.age)
            assertEquals("Test description", retrievedInfo.description)
            assertEquals("22:00", retrievedInfo.sleepStart)
            assertEquals("06:00", retrievedInfo.sleepEnd)
            assertEquals(2, retrievedInfo.hobbies.size)
            assertEquals("Reading", retrievedInfo.hobbies[0])
            assertEquals("Gaming", retrievedInfo.hobbies[1])
            assertEquals(0, retrievedInfo.smoke)
            assertEquals(1, retrievedInfo.drink)
            assertEquals(2, retrievedInfo.personalityType)
            assertEquals(3, retrievedInfo.yearOfStudy)
            assertEquals(1, retrievedInfo.relationshipStatus)
            assertEquals(Faculty.WIET, retrievedInfo.faculty)
        }
    }

    @Test
    fun `test update info entity`() {
        // Arrange
        val database = TestUtils.createTestDatabase()
        val infoService = InfoService(database)

        val infoEntity = transaction(database) {
            InfoService.InfoEntity.new {
                fullName = "Jane Smith"
                gender = 2
                age = 22
                description = "Initial description"
                sleepStart = "23:00"
                sleepEnd = "07:00"
                hobbies = listOf("Swimming")
                smoke = 1
                drink = 2
                personalityType = 3
                yearOfStudy = 2
                relationshipStatus = 0
                faculty = Faculty.WEAIIB
            }
        }

        // Act
        transaction(database) {
            val retrievedInfo = InfoService.InfoEntity.findById(infoEntity.id)
            assertNotNull(retrievedInfo)
            retrievedInfo.description = "Updated description"
            retrievedInfo.age = 23
            retrievedInfo.hobbies = listOf("Swimming", "Running")
        }

        // Assert
        transaction(database) {
            val updatedInfo = InfoService.InfoEntity.findById(infoEntity.id)
            assertNotNull(updatedInfo)
            assertEquals("Jane Smith", updatedInfo.fullName)
            assertEquals(23, updatedInfo.age)
            assertEquals("Updated description", updatedInfo.description)
            assertEquals(2, updatedInfo.hobbies.size)
            assertEquals("Swimming", updatedInfo.hobbies[0])
            assertEquals("Running", updatedInfo.hobbies[1])
        }
    }
}
