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

    var fullName by InfosTable.fullName
    var gender by InfosTable.gender
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
    val fullName = varchar("fullName", length = 100)
    val gender = integer("gender")
    val age = integer("age")
    val description = varchar("description", length = 255)
    val smoke = integer("smoke")
    val drink = integer("drink")
    val faculty = enumeration<Faculty>("departament")
    val sleepStart = varchar("sleepStart", length = 5)
    val sleepEnd = varchar("sleepEnd", length = 5)
    val hobbies = array<String>("hobbies")
    val personalityType = integer("personality_type")
    val yearOfStudy = integer("year_of_study")
    val relationshipStatus = integer("relationship_status").nullable()
  }

  init {
    transaction(database) {
      SchemaUtils.create(InfosTable)
    }
  }
}

