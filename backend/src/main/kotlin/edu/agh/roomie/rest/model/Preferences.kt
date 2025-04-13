package edu.agh.roomie.rest.model

import edu.agh.roomie.service.PreferencesService
import kotlinx.serialization.Serializable

@Serializable
data class Preferences(
  val sleepScheduleMatters: Boolean,
  val hobbiesMatters: Boolean,
  val smokingImportance: Int?,
  val drinkImportance: Int?,
  val personalityTypeImportance: Int?,
  val yearOfStudyMatters: Boolean,
  val facultyMatters: Boolean,
  val relationshipStatusImportance: Int?
) {
  init {
    smokingImportance?.let { require(it in 0..3) { "smokingImportance must be between 0 and 2" } }
    drinkImportance?.let { require(it in 0..3) { "drinkImportance must be between 0 and 2" } }
    personalityTypeImportance?.let { require(it in 0..101) { "personalityTypeImportance must be between 0 and 100" } }
    relationshipStatusImportance?.let { require(it in 0..3) { "relationshipStatusImportance must be between 0 and 2" } }
  }
}

fun PreferencesService.PreferencesEntity.toShared() = Preferences(
  sleepScheduleMatters = this.sleepScheduleMatters,
  hobbiesMatters = this.hobbiesMatters,
  smokingImportance = this.smokingImportance,
  drinkImportance = this.drinkImportance,
  personalityTypeImportance = this.personalityTypeImportance,
  yearOfStudyMatters = this.yearOfStudyMatters,
  facultyMatters = this.facultyMatters,
  relationshipStatusImportance = this.relationshipStatusImportance
)

