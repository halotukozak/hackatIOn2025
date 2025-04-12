package edu.agh.roomie.rest.model

import edu.agh.roomie.service.InfoService
import kotlinx.serialization.Serializable

@Serializable
data class Info(
  val description: String,
  val sleepSchedule: Pair<Int, Int>,
  val hobbies: List<Hobby>,
  val smoke: Int,
  val drink: Int,
  val personalityType: Int,
  val yearOfStudy: Int,
  val faculty: Departament,
  val relationshipStatus: Int,
) {
  init {
    require(sleepSchedule.first in 0..23) { "sleepSchedule start must be between 0 and 23" }
    require(sleepSchedule.second in 0..23) { "sleepSchedule end must be between 0 and 23" }
    require(yearOfStudy > 0) { "yearOfStudy must be positive" }
  }
}

fun InfoService.InfoEntity.toShared() = Info(
  description = this.description,
  sleepSchedule = Pair(this.sleepStart, this.sleepEnd),
  hobbies = this.hobbies.split(",").filter { it.isNotEmpty() }.map { hobbyName -> 
    try {
      Hobby.valueOf(hobbyName.trim())
    } catch (e: IllegalArgumentException) {
      null
    }
  }.filterNotNull(),
  smoke = this.smoke,
  drink = this.drink,
  personalityType = this.personalityType,
  yearOfStudy = this.yearOfStudy,
  faculty = try {
    Departament.valueOf(this.faculty)
  } catch (e: IllegalArgumentException) {
    Departament.WI // Default value if the faculty is not found
  },
  relationshipStatus = this.relationshipStatus
)
