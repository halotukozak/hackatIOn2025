package edu.agh.roomie.service

import edu.agh.roomie.rest.model.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.*

class UserServiceTest {
  private lateinit var database: Database
  private lateinit var userService: UserService
  private lateinit var infoService: InfoService
  private lateinit var preferencesService: PreferencesService

  @BeforeTest
  fun setUp() {
    // Set up an in-memory H2 database for testing
    database = Database.connect(
      url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
      driver = "org.h2.Driver"
    )

    // Initialize services
    infoService = InfoService(database)
    preferencesService = PreferencesService(database)
    userService = UserService(database)
  }

  @Test
  fun testRegister() = runBlocking {
    // Given
    val email = "test@example.com"
    val password = "password123"
    val registerRequest = RegisterRequest(email, password)

    // When
    val userId = userService.register(registerRequest)

    // Then
    assertNotNull(userId, "User ID should not be null")
    assertTrue(userId > 0, "User ID should be positive")

    // Verify user was created in the database
    transaction(database) {
      val user = UserService.UserEntity.findById(userId)
      assertNotNull(user, "User should exist in the database")
      assertEquals(email, user.email, "User email should match")
      // Password should be hashed, so we can't directly compare it
      assertNotEquals(password, user.password, "Password should be hashed")
    }
  }

  @Test
  fun testAuthenticate() = runBlocking {
    // Given
    val email = "auth@example.com"
    val password = "securePassword"
    val registerRequest = RegisterRequest(email, password)
    val userId = userService.register(registerRequest)

    // When
    val authenticatedUserId = userService.authenticate(email, password)
    val invalidPasswordResult = userService.authenticate(email, "wrongPassword")
    val nonExistentUserResult = userService.authenticate("nonexistent@example.com", password)

    // Then
    assertEquals(userId, authenticatedUserId, "Authenticated user ID should match registered user ID")
    assertNull(invalidPasswordResult, "Authentication with wrong password should fail")
    assertNull(nonExistentUserResult, "Authentication with non-existent user should fail")
  }

  @Test
  fun testGetUserById() = runBlocking {
    // Given
    val email = "getuser@example.com"
    val password = "password123"
    val registerRequest = RegisterRequest(email, password)
    val userId = userService.register(registerRequest)

    // Create info and preferences for the user
    val info = createTestInfo()
    val preferences = createTestPreferences()
    userService.upsertUserAdditionalData(userId, info, preferences)

    // When
    val user = userService.getUserById(userId)
    val nonExistentUser = userService.getUserById(-1)

    // Then
    assertNotNull(user, "User should be found")
    assertEquals(email, user.email, "User email should match")
    assertNull(nonExistentUser, "Non-existent user should not be found")
  }

  @Test
  fun testUpsertUserAdditionalData() = runBlocking {
    // Given
    val email = "additionaldata@example.com"
    val password = "password123"
    val registerRequest = RegisterRequest(email, password)
    val userId = userService.register(registerRequest)

    val info = createTestInfo()
    val preferences = createTestPreferences()

    // When
    userService.upsertUserAdditionalData(userId, info, preferences)

    // Then
    val user = userService.getUserById(userId)
    assertNotNull(user, "User should be found")
    assertEquals(email, user.email, "User email should match")

    // Verify info
    assertEquals(info.age, user.info.age, "User age should match")
    assertEquals(info.description, user.info.description, "User description should match")
    assertEquals(info.sleepSchedule, user.info.sleepSchedule, "User sleep schedule should match")
    assertEquals(info.faculty, user.info.faculty, "User faculty should match")

    // Verify preferences
    assertEquals(
      preferences.sleepScheduleMatters,
      user.preferences.sleepScheduleMatters,
      "Sleep schedule preference should match"
    )
    assertEquals(preferences.hobbiesMatters, user.preferences.hobbiesMatters, "Hobbies preference should match")
    assertEquals(preferences.smokingImportance, user.preferences.smokingImportance, "Smoking importance should match")
    assertEquals(preferences.drinkImportance, user.preferences.drinkImportance, "Drink importance should match")
  }

  // Helper methods to create test data
  private fun createTestInfo(): Info {
    return Info(
      name = "Test",
      surname = "User",
      age = 25,
      description = "Test description",
      sleepSchedule = Pair(22, 6),
      hobbies = listOf(Hobby.music, Hobby.cooking),
      smoke = false,
      drink = true,
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

  @Test
  fun testRemoveUser() {
    runBlocking {
      // Given
      val email = "delete@example.com"
      val password = "password123"
      val registerRequest = RegisterRequest(email, password)
      val userId = userService.register(registerRequest)

      // Add info and preferences to the user
      val info = createTestInfo()
      val preferences = createTestPreferences()
      userService.upsertUserAdditionalData(userId, info, preferences)

      // Verify user exists before deletion
      val userBeforeDeletion = userService.getUserById(userId)
      assertNotNull(userBeforeDeletion, "User should exist before deletion")

      // When
      userService.removeUser(userId)

      // Then
      val userAfterDeletion = userService.getUserById(userId)
      assertNull(userAfterDeletion, "User should not exist after deletion")
    }
  }

  @Test
  fun testRemoveUserWithAdditionalData() {
    runBlocking {
      // Given
      val email = "deletewithadditionaldata@example.com"
      val password = "password123"
      val registerRequest = RegisterRequest(email, password)
      val userId = userService.register(registerRequest)

      // Add additional data
      val info = createTestInfo()
      val preferences = createTestPreferences()
      userService.upsertUserAdditionalData(userId, info, preferences)

      // Verify user and additional data exist before deletion
      val userBeforeDeletion = userService.getUserById(userId)
      assertNotNull(userBeforeDeletion, "User should exist before deletion")
      assertNotNull(userBeforeDeletion.info, "User info should exist before deletion")
      assertNotNull(userBeforeDeletion.preferences, "User preferences should exist before deletion")

      // When
      userService.removeUser(userId)

      // Then
      val userAfterDeletion = userService.getUserById(userId)
      assertNull(userAfterDeletion, "User should not exist after deletion")
    }
  }

  @Test
  fun testRemoveNonExistentUser() {
    // Given
    val nonExistentUserId = -1

    // When/Then
    var exceptionThrown = false
    try {
      runBlocking {
        userService.removeUser(nonExistentUserId)
      }
    } catch (e: IllegalStateException) {
      exceptionThrown = true
      assertEquals("User with id $nonExistentUserId not found", e.message)
    }
    assertTrue(exceptionThrown, "Expected IllegalStateException was not thrown")
  }
}
