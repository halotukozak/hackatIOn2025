package edu.agh.roomie.rest.model

import edu.agh.roomie.service.InfoService
import kotlinx.serialization.Serializable

@Serializable
data class Info(
  val age: Int,
  val departament: Departament,
  val description: String,
  val smoke: Boolean,
  val drink: Boolean,
)

fun InfoService.InfoEntity.toShared() = Info(
  age = this.age,
  departament = this.departament,
  description = this.description,
  smoke = this.smoke,
  drink = this.drink,
)
