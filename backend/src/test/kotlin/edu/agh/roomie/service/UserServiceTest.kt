package edu.agh.roomie.service

import edu.agh.roomie.TestUtils
import edu.agh.roomie.rest.model.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.*

class UserServiceTest {

    @Test
    fun `test user service initialization`() {
        // Arrange & Act
        val database = TestUtils.createTestDatabase()
        val userService = UserService(database)

        // Assert - if no exception is thrown, the initialization was successful
        assertNotNull(userService)
    }

    @Test
    fun `test register and authenticate user`() {
        // Arrange
        val database = TestUtils.createTestDatabase()
        val userService = UserService(database)
        val email = "test@example.com"
        val password = "password123"

        // Act
        val userId = userService.register(RegisterRequest(email, password))
        val authenticatedUserId = userService.authenticate(email, password)

        // Assert
        assertNotNull(userId)
        assertEquals(userId, authenticatedUserId)
    }

    @Test
    fun `test authenticate with wrong password`() {
        // Arrange
        val database = TestUtils.createTestDatabase()
        val userService = UserService(database)
        val email = "test2@example.com"
        val password = "password123"

        // Act
        userService.register(RegisterRequest(email, password))
        val authenticatedUserId = userService.authenticate(email, "wrongpassword")

        // Assert
        assertNull(authenticatedUserId)
    }

    @Test
    fun `test get user by id`() {
        // Arrange
        val database = TestUtils.createTestDatabase()
        val userService = UserService(database)
        val email = "test3@example.com"
        val password = "password123"

        // Act
        val userId = userService.register(RegisterRequest(email, password))
        val user = userService.getUserById(userId)

        // Assert
        assertNotNull(user)
        assertEquals(email, user.email)
    }

    @Test
    fun `test upsert user info`() {
        // Arrange
        val database = TestUtils.createTestDatabase()
        transaction(database) {
            SchemaUtils.create(InfoService.InfosTable)
        }
        val infoService = InfoService(database)
        val userService = UserService(database)
        val email = "test4@example.com"
        val password = "password123"
        val userId = userService.register(RegisterRequest(email, password))

        val info = Info(
            fullName = "John Doe",
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

        // Act
        userService.upsertUserInfo(userId, info)
        val user = userService.getUserById(userId)

        // Assert
        assertNotNull(user)
        assertNotNull(user.info)
        assertEquals("John Doe", user.info?.fullName)
        assertEquals(1, user.info?.gender)
        assertEquals(25, user.info?.age)
        assertEquals("Test description", user.info?.description)
        assertEquals("22:00", user.info?.sleepSchedule?.first)
        assertEquals("06:00", user.info?.sleepSchedule?.second)
        assertEquals(2, user.info?.hobbies?.size)
        assertEquals(Hobby.music, user.info?.hobbies?.get(0))
        assertEquals(Hobby.cooking, user.info?.hobbies?.get(1))
        assertEquals(0, user.info?.smoke)
        assertEquals(1, user.info?.drink)
        assertEquals(2, user.info?.personalityType)
        assertEquals(3, user.info?.yearOfStudy)
        assertEquals(1, user.info?.relationshipStatus)
        assertEquals(Faculty.WIET, user.info?.faculty)
    }

    @Test
    fun `test upsert user preferences`() {
        // Arrange
        val database = TestUtils.createTestDatabase()
        transaction(database) {
            SchemaUtils.create(PreferencesService.PreferencesTable)
        }
        val preferencesService = PreferencesService(database)
        val userService = UserService(database)
        val email = "test5@example.com"
        val password = "password123"
        val userId = userService.register(RegisterRequest(email, password))

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

        // Act
        userService.upsertUserPreferences(userId, preferences)
        val user = userService.getUserById(userId)

        // Assert
        assertNotNull(user)
        assertNotNull(user.preferences)
        assertEquals(true, user.preferences?.sleepScheduleMatters)
        assertEquals(true, user.preferences?.hobbiesMatters)
        assertEquals(2, user.preferences?.smokingImportance)
        assertEquals(1, user.preferences?.drinkImportance)
        assertEquals(3, user.preferences?.personalityTypeImportance)
        assertEquals(false, user.preferences?.yearOfStudyMatters)
        assertEquals(true, user.preferences?.facultyMatters)
        assertEquals(0, user.preferences?.relationshipStatusImportance)
    }

    @Test
    fun `test remove user`() {
        // Arrange
        val database = TestUtils.createTestDatabase()
        val userService = UserService(database)
        val email = "test6@example.com"
        val password = "password123"
        val userId = userService.register(RegisterRequest(email, password))

        // Act
        userService.removeUser(userId)
        val user = userService.getUserById(userId)

        // Assert
        assertNull(user)
    }

    @Test
    fun `test get all users`() {
        // Arrange
        val database = TestUtils.createTestDatabase()
        val userService = UserService(database)
        val email1 = "test7@example.com"
        val email2 = "test8@example.com"
        val password = "password123"

        // Act
        userService.register(RegisterRequest(email1, password))
        userService.register(RegisterRequest(email2, password))
        val users = userService.getAllUsers()

        // Assert
        assertTrue(users.size >= 2)
        assertTrue(users.any { it.email == email1 })
        assertTrue(users.any { it.email == email2 })
    }
}
