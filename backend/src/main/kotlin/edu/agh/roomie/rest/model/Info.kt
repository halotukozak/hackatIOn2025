package edu.agh.roomie.rest.model

import edu.agh.roomie.service.InfoService
import kotlinx.serialization.Serializable

@Serializable
data class Info(
  val fullName: String,
  val gender: Int,
  val age: Int,
  val description: String,
  val sleepSchedule: Pair<String, String>,
  val hobbies: List<Hobby>,
  val smoke: Int,
  val drink: Int,
  val personalityType: Int,
  val yearOfStudy: Int,
  val faculty: Faculty,
  val relationshipStatus: Int,
) {
  init {
    require(yearOfStudy > 0) { "yearOfStudy must be positive" }
  }
}

fun InfoService.InfoEntity.toShared() = Info(
  fullName = this.fullName,
  gender = this.gender,
  age = this.age,
  description = this.description,
  sleepSchedule = Pair(this.sleepStart.toString(), this.sleepEnd.toString()),
  hobbies = this.hobbies.map { Hobby.valueOf(it) },
  smoke = this.smoke,
  drink = this.drink,
  personalityType = this.personalityType,
  yearOfStudy = this.yearOfStudy,
  faculty = this.faculty,
  relationshipStatus = this.relationshipStatus
)
