package edu.agh.roomie.service

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

class MatchService(database: Database) {

  class MatchEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MatchEntity>(MatchesTable) {
      fun findByUsers(userId1: Int, userId2: Int): MatchEntity? =
        find {
          (MatchesTable.userId eq userId1 and (MatchesTable.matchedUserId eq userId2)) or
                  (MatchesTable.userId eq userId2 and (MatchesTable.matchedUserId eq userId1))
        }.singleOrNull()
    }

    var userId by MatchesTable.userId
    var matchedUserId by MatchesTable.matchedUserId
    var createdAt by MatchesTable.createdAt
    var isMatched by MatchesTable.isMatched
  }

  object MatchesTable : IntIdTable() {
    val userId = integer("user_id")
    val matchedUserId = integer("matched_user_id")
    val createdAt = varchar("created_at", 50)
    val isMatched = bool("is_matched").default(false)
  }

  init {
    transaction(database) {
      SchemaUtils.create(MatchesTable)
    }
  }

  suspend fun swipeRight(userId: Int, swipedUserId: Int): Boolean = newSuspendedTransaction(Dispatchers.IO) {
    val existingMatch = MatchEntity.find {
      (MatchesTable.userId eq swipedUserId) and (MatchesTable.matchedUserId eq userId)
    }.singleOrNull()

    if (existingMatch != null) {
      existingMatch.isMatched = true
      true
    } else {
      MatchEntity.new {
        this.userId = userId
        this.matchedUserId = swipedUserId
        this.createdAt = LocalDateTime.now().toString()
        this.isMatched = false
      }
      false
    }
  }

  suspend fun getMatches(userId: Int): List<Int> = newSuspendedTransaction(Dispatchers.IO) {
    MatchEntity.find {
      ((MatchesTable.userId eq userId) or (MatchesTable.matchedUserId eq userId)) and
              (MatchesTable.isMatched eq true)
    }.map {
      if (it.userId == userId) it.matchedUserId else it.userId
    }
  }
}