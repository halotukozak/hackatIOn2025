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
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
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
    }

    var name by UsersTable.name
    var surname by UsersTable.surname
    var email by UsersTable.email
    var password by UsersTable.password
    var preferences by PreferencesService.PreferencesEntity referencedOn UsersTable.preferences
    var info by InfoService.InfoEntity referencedOn UsersTable.info
  }

  object UsersTable : IntIdTable() {
    val name = varchar("name", length = 50)
    val surname = varchar("surname", length = 50)
    val email = varchar("email", length = 50)
    val password = varchar("password", length = 100)
    val preferences = reference("preferences", PreferencesTable)
    val info = reference("info", InfosTable)
  }

  init {
    transaction(database) {
      SchemaUtils.create(UsersTable)
    }
  }

  fun register(request: RegisterRequest) = UserEntity.new {
    this.email = request.email
    this.password = hashPassword(request.password)
  }.id.value

  suspend fun authenticate(email: String, password: String): Int? = newSuspendedTransaction {
    val user = UserEntity.findByEmail(email)
    if (user != null && verifyPassword(password, user.password)) {
      user.id.value
    } else {
      null
    }
  }

  fun getUserById(id: Int): User? = UserEntity.findById(id)?.toShared()
  fun createUserAdditionalData(id: Int, info: Info, preferences: Preferences) =
    UserEntity.findByIdAndUpdate(id) { user ->
      user.info = InfoService.InfoEntity.new {
        this.age = info.age
        this.description = info.description
        this.sleepStart = info.sleepSchedule.first
        this.sleepEnd = info.sleepSchedule.second
        this.faculty = info.faculty
      }
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
