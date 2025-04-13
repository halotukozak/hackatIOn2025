package edu.agh.roomie.service

import edu.agh.roomie.rest.model.*
import edu.agh.roomie.service.InfoService.InfosTable
import edu.agh.roomie.service.PreferencesService.PreferencesTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.security.MessageDigest

class UserService(database: Database) {
  private fun hashPassword(password: String): String {
    val md = MessageDigest.getInstance("SHA-256")
    val hash = md.digest(password.toByteArray())
    return hash.joinToString("") { "%02x".format(it) }
  }

  private fun verifyPassword(password: String, hashedPassword: String): Boolean {
    return hashPassword(password) == hashedPassword
  }

  class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(UsersTable) {
      fun findByEmail(email: String): UserEntity? = find { UsersTable.email eq email }.singleOrNull()
      fun findByIds(ids: List<EntityID<Int>>): List<UserEntity> = find { UsersTable.id inList ids }.toList()
    }

    var email by UsersTable.email
    var password by UsersTable.password
    var preferences by PreferencesService.PreferencesEntity optionalReferencedOn UsersTable.preferences
    var info by InfoService.InfoEntity optionalReferencedOn UsersTable.info

//    val matches by MatchService.MatchEntity via MatchService.MatchesTable
  }

  object UsersTable : IntIdTable() {
    val email = varchar("email", length = 50)
    val password = varchar("password", length = 100)
    val preferences = reference("preferences", PreferencesTable).nullable()
    val info = reference("info", InfosTable).nullable()
  }

  init {
    transaction(database) {
      SchemaUtils.create(UsersTable)
    }
  }

  suspend fun register(request: RegisterRequest) = transaction {
    UserEntity.new {
      this.email = request.email
      this.password = hashPassword(request.password)
    }.id.value
  }

  suspend fun authenticate(email: String, password: String): Int? = transaction {
    val user = UserEntity.findByEmail(email)
    if (user != null && verifyPassword(password, user.password)) {
      user.id.value
    } else {
      null
    }
  }

  suspend fun getUserById(id: Int): User? = transaction {
    UserEntity.findById(id)?.toShared()
  }

  suspend fun upsertUserInfo(id: Int, info: Info) = transaction {
    UserEntity.findByIdAndUpdate(id) { user ->
      user.info = InfoService.InfoEntity.new {
        this.fullName = info.fullName
        this.gender = info.gender
        this.age = info.age
        this.description = info.description
        this.sleepStart = info.sleepSchedule.first
        this.sleepEnd = info.sleepSchedule.second
        this.hobbies = info.hobbies.map { it.name }
        this.smoke = info.smoke
        this.drink = info.drink
        this.personalityType = info.personalityType
        this.yearOfStudy = info.yearOfStudy
        this.relationshipStatus = info.relationshipStatus
        this.faculty = info.faculty
      }
    }
  }

  suspend fun upsertUserPreferences(id: Int, preferences: Preferences) = transaction {
    UserEntity.findByIdAndUpdate(id) { user ->
      user.preferences = PreferencesService.PreferencesEntity.new {
        this.sleepScheduleMatters = preferences.sleepScheduleMatters
        this.hobbiesMatters = preferences.hobbiesMatters
        this.smokingImportance = preferences.smokingImportance
        this.drinkImportance = preferences.drinkImportance
        this.personalityTypeImportance = preferences.personalityTypeImportance
        this.yearOfStudyMatters = preferences.yearOfStudyMatters
        this.facultyMatters = preferences.facultyMatters
        this.relationshipStatusImportance = preferences.relationshipStatusImportance
      }
    }
  }

  suspend fun removeUser(id: Int) = transaction {
    UserEntity.findById(id)?.delete() ?: throw IllegalStateException("User with id $id not found")
  }

  suspend fun getAllUsers() = transaction {
    UserEntity.all().map { it.toShared() }
  }
}
