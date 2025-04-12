package edu.agh.roomie.service

import edu.agh.roomie.rest.model.RegisterRequest
import edu.agh.roomie.rest.model.User
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
    var age by UsersTable.age
    var preferences by PreferencesService.PreferencesEntity referencedOn UsersTable.preferences
    var info by InfoService.InfoEntity referencedOn UsersTable.info
  }

  object UsersTable : IntIdTable() {
    val name = varchar("name", length = 50)
    val surname = varchar("surname", length = 50)
    val email = varchar("email", length = 50)
    val password = varchar("password", length = 100)
    val age = integer("age")
    val preferences = reference("preferences", PreferencesTable)
    val info = reference("info", InfosTable)
  }

  init {
    transaction(database) {
      SchemaUtils.create(UsersTable)
    }
  }

  suspend fun create(user: User, password: String? = null): Int = newSuspendedTransaction {
    val user = UserEntity.new {
      this.name = user.name
      this.surname = user.surname
      this.email = user.email
      this.age = user.age
      this.password = password?.let { hashPassword(it) } ?: ""
    }
    user.preferences = PreferencesService.PreferencesEntity.new {
      this.description = user.preferences.description
      this.smoke = user.preferences.smoke
      this.drink = user.preferences.drink
    }
    user.info = InfoService.InfoEntity.new {
      this.description = user.info.description
      this.smoke = user.info.smoke
      this.drink = user.info.drink
    }
    user.id.value
  }

  suspend fun register(request: RegisterRequest): Int = newSuspendedTransaction {
    val user = User(
      name = request.name,
      surname = request.surname,
      email = request.email,
      age = request.age,
      info = request.info,
      preferences = request.preferences
    )
    create(user, request.password)
  }

  suspend fun authenticate(email: String, password: String): Int? = newSuspendedTransaction {
    val user = UserEntity.findByEmail(email)
    if (user != null && verifyPassword(password, user.password)) {
      user.id.value
    } else {
      null
    }
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
