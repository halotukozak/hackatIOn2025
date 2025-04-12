package edu.agh.roomie.rest.model

import kotlinx.serialization.Serializable

@Serializable
data class AdditionalInfoRequest(
  val age: Int,
  val info: Info,
  val preferences: Preferences,
  val departament: Departament,
)

@Serializable
data class LoginRequest(
  val email: String,
  val password: String
)

@Serializable
data class RegisterRequest(
  val email: String,
  val password: String
)

@Serializable
data class AuthResponse(
  val token: String,
  val userId: Int
)
