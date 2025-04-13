package edu.agh.roomie

import edu.agh.roomie.rest.Dependencies
import edu.agh.roomie.service.FakeUserGenerator
import edu.agh.roomie.service.InfoService
import edu.agh.roomie.service.MatchService.MatchEntity
import edu.agh.roomie.service.PreferencesService
import edu.agh.roomie.service.UserService
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import kotlin.random.Random

fun Application.configureDatabases(): Database {
  val database = Database.connect(
    url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
    user = "root",
    driver = "org.h2.Driver",
    password = "",
  )
  return database
}

context(Dependencies)
fun generateFakeData() = transaction(database) {
  addLogger(StdOutSqlLogger)

  val users = (1..100).map { i ->
    val fakeUser = FakeUserGenerator.generateFakeUser(i)
    val fakeInfo = InfoService.InfoEntity.new {
      this.fullName = fakeUser.info.fullName
      this.gender = fakeUser.info.gender
      this.age = fakeUser.info.age
      this.description = fakeUser.info.description
      this.sleepStart = fakeUser.info.sleepSchedule.first
      this.sleepEnd = fakeUser.info.sleepSchedule.second
      this.hobbies = fakeUser.info.hobbies.map { it.name }
      this.smoke = fakeUser.info.smoke
      this.drink = fakeUser.info.drink
      this.personalityType = fakeUser.info.personalityType
      this.yearOfStudy = fakeUser.info.yearOfStudy
      this.relationshipStatus = fakeUser.info.relationshipStatus
      this.faculty = fakeUser.info.faculty
    }
    val fakePreferences = PreferencesService.PreferencesEntity.new {
      this.sleepScheduleMatters = fakeUser.preferences.sleepScheduleMatters
      this.hobbiesMatters = fakeUser.preferences.hobbiesMatters
      this.smokingImportance = fakeUser.preferences.smokingImportance ?: 0
      this.drinkImportance = fakeUser.preferences.drinkImportance ?: 0
      this.personalityTypeImportance = fakeUser.preferences.personalityTypeImportance ?: 0
      this.yearOfStudyMatters = fakeUser.preferences.yearOfStudyMatters
      this.facultyMatters = fakeUser.preferences.facultyMatters
      this.relationshipStatusImportance = fakeUser.preferences.relationshipStatusImportance ?: 0
    }

    UserService.UserEntity.new {
      email = fakeUser.email
      password = "password123"
      info = fakeInfo
      preferences = fakePreferences
    }
  }

  users.asSequence().shuffled().chunked(2).map { it[0] to it[1] }.forEach { (user1, user2) ->
    MatchEntity.new {
      this.userId = user1.id.value
      this.matchedUserId = user2.id.value
      this.createdAt = LocalDateTime.now().toString()
      this.isMatched = Random.nextBoolean()
    }
  }
}
