package edu.agh.roomie.rest.model

import kotlinx.serialization.Serializable

@Serializable
data class AdditionalInfoRequest(
  val userId: Int,
  val info: Info,
)

@Serializable
data class AdditionalPreferencesRequest(
  val userId: Int,
  val preferences: Preferences,
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

@Serializable
data class DeleteRequest(
  val userId: Int
)
