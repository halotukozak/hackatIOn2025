package edu.agh.roomie.service

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database

class PreferencesService(database: Database) {
  class PreferencesEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PreferencesEntity>(PreferencesTable)

    var description by PreferencesTable.description
    var smoke by PreferencesTable.smoke
    var drink by PreferencesTable.drink
    var level by PreferencesTable.level
  }

  object PreferencesTable : IntIdTable() {
    val description = varchar("description", length = 255)
    val smoke = bool("smoke")
    val drink = bool("drink")
    val level = integer("level")
  }
}