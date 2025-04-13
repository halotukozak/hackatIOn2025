//package edu.agh.roomie.rest.endpoints
//
//import edu.agh.roomie.rest.Dependencies
//import edu.agh.roomie.rest.model.Faculty
//import edu.agh.roomie.rest.model.Hobby
//import edu.agh.roomie.rest.model.Info
//import edu.agh.roomie.rest.model.Preferences
//import edu.agh.roomie.service.AuthService
//import edu.agh.roomie.service.MatchService
//import edu.agh.roomie.service.UserService
//import io.ktor.serialization.kotlinx.json.*
//import io.ktor.server.application.*
//import io.ktor.server.plugins.contentnegotiation.*
//import io.ktor.server.testing.*
//import org.jetbrains.exposed.sql.Database
//import org.jetbrains.exposed.sql.SchemaUtils
//import org.jetbrains.exposed.sql.transactions.transaction
//import kotlin.test.AfterTest
//import kotlin.test.BeforeTest
//
///**
// * Base class for routing tests that provides common setup and utility methods.
// */
//open class BaseRoutingTest {
//  protected lateinit var database: Database
//  protected lateinit var userService: UserService
//  protected lateinit var authService: AuthService
//  protected lateinit var matchService: MatchService
//  protected lateinit var dependencies: Dependencies
//
//  @BeforeTest
//  fun setUp() {
//    // Set up an in-memory H2 database for testing
//    database = Database.connect(
//      url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
//      driver = "org.h2.Driver"
//    )
//
//    // Initialize services
//    userService = UserService(database)
//    authService = AuthService()
//    matchService = MatchService(database)
//
//    // Create dependencies
//    dependencies = Dependencies(
//      database = database,
//      userService = userService,
//      authService = authService,
//      matchService = matchService
//    )
//  }
//
//  @AfterTest
//  fun testSetup() {
//    transaction(database) {
//      SchemaUtils.dropDatabase()
//    }
//  }
//
//  /**
//   * Creates a test Info object with default values.
//   */
//  protected fun createTestInfo(): Info {
//    return Info(
//      fullName = "test",
//      age = 25,
//      description = "Test description",
//      sleepSchedule = Pair("22", "6"),
//      hobbies = listOf(Hobby.music, Hobby.cooking),
//      smoke = 4,
//      drink = 4,
//      personalityType = 1,
//      yearOfStudy = 3,
//      faculty = Faculty.WI,
//      relationshipStatus = 1,
//      gender = 3,
//    )
//  }
//
//  /**
//   * Creates a test Preferences object with default values.
//   */
//  protected fun createTestPreferences(): Preferences {
//    return Preferences(
//      sleepScheduleMatters = true,
//      hobbiesMatters = true,
//      smokingImportance = 3,
//      drinkImportance = 2,
//      personalityTypeImportance = 1,
//      yearOfStudyMatters = false,
//      facultyMatters = true,
//      relationshipStatusImportance = 0
//    )
//  }
//
//  /**
//   * Configures a test application with standard settings.
//   */
//  protected fun TestApplicationBuilder.configureTestApplication(routeSetup: Application.() -> Unit) {
//    application {
//      install(ContentNegotiation) {
//        json()
//      }
//      routeSetup()
//    }
//  }
//}