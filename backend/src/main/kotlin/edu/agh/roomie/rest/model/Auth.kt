package edu.agh.roomie.rest.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
  val name: String,
  val surname: String,
  val email: String,
  val password: String,
  val age: Int,
  val info: Info,
  val preferences: Preferences
)

@Serializable
data class LoginRequest(
  val email: String,
  val password: String
)

@Serializable
data class AuthResponse(
  val token: String,
  val userId: Int
)
