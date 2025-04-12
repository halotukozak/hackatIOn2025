package edu.agh.roomie.service

import edu.agh.roomie.rest.model.User
import edu.agh.roomie.rest.model.Info
import edu.agh.roomie.rest.model.Preferences
import edu.agh.roomie.rest.model.toShared
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

class UserService(database: Database) {
  class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(UsersTable)

    var name by UsersTable.name
    var surname by UsersTable.surname
    var email by UsersTable.email
    var age by UsersTable.age
    var preferences by PreferencesService.PreferencesEntity referencedOn UsersTable.preferences
    var info by InfoService.InfoEntity referencedOn UsersTable.info
  }

  object UsersTable : IntIdTable() {
    val name = varchar("name", length = 50)
    val surname = varchar("surname", length = 50)
    val email = varchar("email", length = 50)
    val age = integer("age")
    val preferences = reference("preferences", PreferencesTable)
    val info = reference("info", InfosTable)
  }

  init {
    transaction(database) {
      SchemaUtils.create(UsersTable)
    }
  }

  suspend fun create(user: User): Int = newSuspendedTransaction {
    val user = UserEntity.new {
      this.name = user.name
      this.surname = user.surname
      this.email = user.email
      this.age = user.age
    }
    user.preferences = PreferencesService.PreferencesEntity.new {
      this.sleepScheduleMatters = user.preferences.sleepScheduleMatters
      this.hobbiesMatters = user.preferences.hobbiesMatters
      this.smokingImportance = user.preferences.smokingImportance
      this.drinkImportance = user.preferences.drinkImportance
      this.personalityTypeImportance = user.preferences.personalityTypeImportance
      this.yearOfStudyMatters = user.preferences.yearOfStudyMatters
      this.facultyMatters = user.preferences.facultyMatters
      this.relationshipStatusImportance = user.preferences.relationshipStatusImportance
    }
    val info = user.info

    user.info = InfoService.InfoEntity.new {
      this.description = info.description
      this.sleepStart = 0 // Default value
      this.sleepEnd = 0 // Default value
      this.hobbies = info.hobbies
      this.smoke = info.smoke
      this.drink = info.drink
      this.personalityType = info.personalityType
      this.yearOfStudy = info.yearOfStudy
      this.faculty = info.faculty
      this.relationshipStatus = info.relationshipStatus
    }
    user.id.value
  }

  fun read(id: Int): User? = UserEntity.findById(id)?.let { user ->
    User(
      name = user.name,
      surname = user.surname,
      email = user.email,
      age = user.age,
      info = user.info.toShared(),
      preferences = user.preferences.toShared(),
    )
  }
}
