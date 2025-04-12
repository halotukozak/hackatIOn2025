package edu.agh.roomie.rest.model

import edu.agh.roomie.service.PreferencesService
import kotlinx.serialization.Serializable

@Serializable
data class Preferences(
  val smoke: Boolean,
  val drink: Boolean,
  val level: Int,
) {
  init {
    require(level in 1..<5) { "level must be between 1 and 5 or empty" }
  }
}

fun PreferencesService.PreferencesEntity.toShared() = Preferences(
  smoke = this.smoke,
  drink = this.drink,
  level = this.level,
)