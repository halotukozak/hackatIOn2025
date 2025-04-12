package edu.agh.roomie.rest.model

import edu.agh.roomie.service.InfoService
import kotlinx.serialization.Serializable

@Serializable
data class Info(
  val age: Int,
  val description: String,
  val sleepSchedule: Pair<Int, Int>,
  val hobbies: List<Hobby>,
  val smoke: Boolean,
  val drink: Boolean,
  val personalityType: Int,
  val yearOfStudy: Int,
  val faculty: Faculty,
  val relationshipStatus: Int,
) {
  init {
    require(sleepSchedule.first in 0..23) { "sleepSchedule start must be between 0 and 23" }
    require(sleepSchedule.second in 0..23) { "sleepSchedule end must be between 0 and 23" }
    require(yearOfStudy > 0) { "yearOfStudy must be positive" }
  }
}

fun InfoService.InfoEntity.toShared() = Info(
  age = this.age,
  description = this.description,
  sleepSchedule = Pair(this.sleepStart, this.sleepEnd),
  hobbies = this.hobbies.map { Hobby.valueOf(it) },
  smoke = this.smoke,
  drink = this.drink,
  personalityType = this.personalityType,
  yearOfStudy = this.yearOfStudy,
  faculty = this.faculty,
  relationshipStatus = this.relationshipStatus
)
