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
    var smoke by InfosTable.smoke
    var drink by InfosTable.drink
  }

  object InfosTable : IntIdTable() {
    val description = varchar("description", length = 255)
    val smoke = bool("smoke")
    val drink = bool("drink")
  }

  init {
    transaction(database) {
      SchemaUtils.create(InfosTable)
    }
  }

  suspend fun create(user: Info): Int = dbQuery {
    InfosTable.insert {
      it[description] = user.description
      it[smoke] = user.smoke
      it[drink] = user.drink
    }[InfosTable.id].value
  }

  suspend fun read(id: Int): Info? = dbQuery {
    InfosTable.selectAll()
      .where { InfosTable.id eq id }
      .map { Info(it[InfosTable.description], it[InfosTable.smoke], it[InfosTable.drink]) }
      .singleOrNull()
  }

  suspend fun update(id: Int, info: Info) {
    dbQuery {
      InfosTable.update({ InfosTable.id eq id }) {
        it[description] = info.description
        it[smoke] = info.smoke
        it[drink] = info.drink
      }
    }
  }

  suspend fun delete(id: Int) = dbQuery {
    InfosTable.deleteWhere { InfosTable.id.eq(id) }
  }

  private suspend fun <T> dbQuery(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }
}

