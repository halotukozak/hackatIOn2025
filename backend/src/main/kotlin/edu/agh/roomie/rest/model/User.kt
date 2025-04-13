package edu.agh.roomie.rest.model

import edu.agh.roomie.service.UserService
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.load

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
  info = this.info?.toShared() ?: Info(
    fullName = "Unknown",
    gender = 0,
    age = 0,
    description = "",
    sleepSchedule = Pair("00:00", "00:00"),
    hobbies = emptyList(),
    smoke = 0,
    drink = 0,
    personalityType = 0,
    yearOfStudy = 1,
    faculty = Faculty.WI,
    relationshipStatus = 0
  ),
  preferences = this.preferences?.toShared() ?: Preferences(
    sleepScheduleMatters = false,
    hobbiesMatters = false,
    smokingImportance = null,
    drinkImportance = null,
    personalityTypeImportance = null,
    yearOfStudyMatters = false,
    facultyMatters = false,
    relationshipStatusImportance = null
  ),
)
