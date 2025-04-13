package edu.agh.roomie

import edu.agh.roomie.rest.Dependencies
import edu.agh.roomie.rest.model.MatchStatus
import edu.agh.roomie.service.FakeUserGenerator
import edu.agh.roomie.service.InfoService
import edu.agh.roomie.service.MatchService.InvitationEntity
import edu.agh.roomie.service.PreferencesService
import edu.agh.roomie.service.UserService
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import kotlin.random.Random

fun Application.configureDatabases(): Database {
  val database = if (isDeployment) Database.connect(
    url = "jdbc:postgresql://dpg-cvtqhis9c44c738puva0-a.oregon-postgres.render.com/roomie_hkrz",
    driver = "org.postgresql.Driver",
    user = "roomie",
    password = "A9hloke0pADrSXz8TzIGEUTYfiJzQM72"
  ) else Database.connect(
    url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
    user = "root",
    driver = "org.h2.Driver",
    password = "",
  )
  return database
}

context(Dependencies)
fun generateFakeData() {
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
      this.smokingImportance = fakeUser.preferences.smokingImportance
      this.drinkImportance = fakeUser.preferences.drinkImportance
      this.personalityTypeImportance = fakeUser.preferences.personalityTypeImportance
      this.yearOfStudyMatters = fakeUser.preferences.yearOfStudyMatters
      this.facultyMatters = fakeUser.preferences.facultyMatters
      this.relationshipStatusImportance = fakeUser.preferences.relationshipStatusImportance
    }

    UserService.UserEntity.new {
      email = fakeUser.email
      password = "password123"
      info = fakeInfo
      preferences = fakePreferences
    }
  }

  users.asSequence().shuffled().chunked(2).take(30).map { it[0] to it[1] }.forEach { (user1, user2) ->
    InvitationEntity.new {
      userId = user1.id
      matchedUserId = user2.id
      requestStatus = if (Random.nextBoolean()) MatchStatus.ACK else MatchStatus.NACK
      responseStatus = FakeUserGenerator.randomMatchStatus()
    }
  }
}
