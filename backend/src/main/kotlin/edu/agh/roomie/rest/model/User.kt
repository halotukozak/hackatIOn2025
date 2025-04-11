package edu.agh.roomie.rest.model

import edu.agh.roomie.scheme.UserService
import kotlinx.serialization.Serializable

@Serializable
data class User(
  val name: String,
  val surname: String,
  val email: String,
  val age: Int,
  val info: Info,
  val preferences: Preferences
)

fun UserService.UserEntity.toShared() = User(
  name = this.name,
  surname = this.surname,
  email = this.email,
  age = this.age,
  info = this.info.toShared(),
  preferences = this.preferences.toShared(),
)
