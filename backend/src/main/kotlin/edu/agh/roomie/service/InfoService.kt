package edu.agh.roomie.service

import edu.agh.roomie.rest.model.Faculty
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class InfoService(database: Database) {

  class InfoEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<InfoEntity>(InfosTable)

    var age by InfosTable.age
    var description by InfosTable.description
    var sleepStart by InfosTable.sleepStart
    var sleepEnd by InfosTable.sleepEnd
    var hobbies by InfosTable.hobbies
    var smoke by InfosTable.smoke
    var drink by InfosTable.drink
    var personalityType by InfosTable.personalityType
    var yearOfStudy by InfosTable.yearOfStudy
    var relationshipStatus by InfosTable.relationshipStatus
    var faculty by InfosTable.faculty
  }

  object InfosTable : IntIdTable() {
    val age = integer("age")
    val description = varchar("description", length = 255)
    val smoke = bool("smoke")
    val drink = bool("drink")
    val faculty = enumeration<Faculty>("departament")
    val sleepStart = integer("sleepStart")
    val sleepEnd = integer("sleepEnd")
    val hobbies = varchar("hobbies", length = 1000)
    val personalityType = integer("personality_type")
    val yearOfStudy = integer("year_of_study")
    val relationshipStatus = integer("relationship_status")
  }

  init {
    transaction(database) {
      SchemaUtils.create(InfosTable)
    }
  }
}

