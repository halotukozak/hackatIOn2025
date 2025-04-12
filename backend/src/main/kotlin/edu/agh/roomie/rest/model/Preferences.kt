package edu.agh.roomie.rest.model

import edu.agh.roomie.scheme.PreferencesService
import kotlinx.serialization.Serializable

@Serializable
data class Preferences(
  val sleepScheduleMatters: Boolean,
  val hobbiesMatters: Boolean,
  val smokingImportance: Int,
  val drinkImportance: Int,
  val personalityTypeImportance: Int,
  val yearOfStudyMatters: Boolean,
  val facultyMatters: Boolean,
  val relationshipStatusImportance: Int
) {
  init {
    require(smokingImportance in 0..5) { "smokingImportance must be between 0 and 5" }
    require(drinkImportance in 0..5) { "drinkImportance must be between 0 and 5" }
    require(personalityTypeImportance in 0..5) { "personalityTypeImportance must be between 0 and 5" }
    require(relationshipStatusImportance in 0..5) { "relationshipStatusImportance must be between 0 and 5" }
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
