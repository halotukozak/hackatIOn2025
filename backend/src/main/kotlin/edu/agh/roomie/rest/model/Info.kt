package edu.agh.roomie.rest.model

import edu.agh.roomie.service.InfoService
import kotlinx.serialization.Serializable

@Serializable
data class Info(
  val description: String,
  val sleepSchedule: Pair<Int, Int>,
  val hobbies: String,
  val smoke: Int,
  val drink: Int,
  val personalityType: Int,
  val yearOfStudy: Int,
  val faculty: String,
  val relationshipStatus: Int,
) {
  init {
    require(sleepSchedule.first in 0..23) { "sleepSchedule start must be between 0 and 23" }
    require(sleepSchedule.second in 0..23) { "sleepSchedule end must be between 0 and 23" }
    require(sleepSchedule.first < sleepSchedule.second) { "sleepSchedule start must be less than end" }
    require(yearOfStudy > 0) { "yearOfStudy must be positive" }
  }
}

fun InfoService.InfoEntity.toShared() = Info(
  description = this.description,
  sleepSchedule = Pair(this.sleepStart, this.sleepEnd),
  hobbies = this.hobbies,
  smoke = this.smoke,
  drink = this.drink,
  personalityType = this.personalityType,
  yearOfStudy = this.yearOfStudy,
  faculty = this.faculty,
  relationshipStatus = this.relationshipStatus
)
