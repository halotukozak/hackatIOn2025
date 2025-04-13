package edu.agh.roomie.rest.model

import edu.agh.roomie.service.UserService
import kotlinx.serialization.Serializable

@Serializable
data class User(
  val id: Int,
  val email: String,
  val info: Info,
  val preferences: Preferences
)

fun UserService.UserEntity.toShared() = User(
  id = this.id.value,
  email = this.email,
  info = this.info?.toShared() ?: throw IllegalStateException("UserEntity.info is null"),
  preferences = this.preferences?.toShared() ?: throw IllegalStateException("UserEntity.preferences is null"),
)
