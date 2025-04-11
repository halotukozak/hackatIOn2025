package edu.agh.roomie.rest.model

import edu.agh.roomie.scheme.InfoService
import kotlinx.serialization.Serializable

@Serializable
data class Info(
  val description: String,
  val smoke: Boolean,
  val drink: Boolean,
)

fun InfoService.InfoEntity.toShared() = Info(
  description = this.description,
  smoke = this.smoke,
  drink = this.drink,
)