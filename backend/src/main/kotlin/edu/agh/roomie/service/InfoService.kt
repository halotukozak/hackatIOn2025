package edu.agh.roomie.service

import edu.agh.roomie.rest.model.Info
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class InfoService(database: Database) {

  class InfoEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<InfoEntity>(InfosTable)

    var description by InfosTable.description
    var sleepStart by InfosTable.sleepStart
    var sleepEnd by InfosTable.sleepEnd
    var hobbies by InfosTable.hobbies
    var smoke by InfosTable.smoke
    var drink by InfosTable.drink
    var personalityType by InfosTable.personalityType
    var yearOfStudy by InfosTable.yearOfStudy
    var faculty by InfosTable.faculty
    var relationshipStatus by InfosTable.relationshipStatus
  }

  object InfosTable : IntIdTable() {
    val description = varchar("description", length = 255)
    val sleepStart = integer("sleepStart")
    val sleepEnd = integer("sleepEnd")
    val hobbies = varchar("hobbies", length = 1000)
    val smoke = integer("smoke")
    val drink = integer("drink")
    val personalityType = integer("personality_type")
    val yearOfStudy = integer("year_of_study")
    val faculty = varchar("faculty", length = 255)
    val relationshipStatus = integer("relationship_status")
  }

  init {
    transaction(database) {
      SchemaUtils.create(InfosTable)
    }
  }

  suspend fun create(user: Info): Int = dbQuery {
    InfosTable.insert {
      it[description] = user.description
      it[sleepStart] = user.sleepSchedule.first
      it[sleepEnd] = user.sleepSchedule.second
      it[hobbies] = user.hobbies
      it[smoke] = user.smoke
      it[drink] = user.drink
      it[personalityType] = user.personalityType
      it[yearOfStudy] = user.yearOfStudy
      it[faculty] = user.faculty
      it[relationshipStatus] = user.relationshipStatus
    }[InfosTable.id].value
  }

  suspend fun read(id: Int): Info? = dbQuery {
    InfosTable.selectAll()
      .where { InfosTable.id eq id }
      .map { 
        Info(
          description = it[InfosTable.description],
          sleepSchedule = Pair (
            it[InfosTable.sleepStart],
            it[InfosTable.sleepEnd]
          ),
          hobbies = it[InfosTable.hobbies],
          smoke = it[InfosTable.smoke],
          drink = it[InfosTable.drink],
          personalityType = it[InfosTable.personalityType],
          yearOfStudy = it[InfosTable.yearOfStudy],
          faculty = it[InfosTable.faculty],
          relationshipStatus = it[InfosTable.relationshipStatus]
        ) 
      }
      .singleOrNull()
  }

  suspend fun update(id: Int, info: Info) {
    dbQuery {
      InfosTable.update({ InfosTable.id eq id }) {
        it[description] = info.description
        it[sleepStart] = info.sleepSchedule.first
        it[sleepEnd] = info.sleepSchedule.second
        it[hobbies] = info.hobbies
        it[smoke] = info.smoke
        it[drink] = info.drink
        it[personalityType] = info.personalityType
        it[yearOfStudy] = info.yearOfStudy
        it[faculty] = info.faculty
        it[relationshipStatus] = info.relationshipStatus
      }
    }
  }

  suspend fun delete(id: Int) = dbQuery {
    InfosTable.deleteWhere { InfosTable.id.eq(id) }
  }

  private suspend fun <T> dbQuery(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }
}
