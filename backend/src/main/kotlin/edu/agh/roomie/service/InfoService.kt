package edu.agh.roomie.service

import edu.agh.roomie.rest.model.Departament
import edu.agh.roomie.rest.model.Info
import edu.agh.roomie.rest.model.toShared
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
    var smoke by InfosTable.smoke
    var drink by InfosTable.drink
    var departament by InfosTable.departament
  }

  object InfosTable : IntIdTable() {
    val age = integer("age")
    val description = varchar("description", length = 255)
    val smoke = bool("smoke")
    val drink = bool("drink")
    val departament = enumeration<Departament>("departament")
  }

  init {
    transaction(database) {
      SchemaUtils.create(InfosTable)
    }
  }

  fun create(info: Info) = InfoEntity.new {
    this.age = info.age
    this.description = info.description
    this.smoke = info.smoke
    this.drink = info.drink
    this.departament = info.departament
  }

  fun read(id: Int): Info? =
    InfoEntity.findById(id)?.toShared()
}

