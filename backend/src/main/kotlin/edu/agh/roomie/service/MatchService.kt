package edu.agh.roomie.service

import edu.agh.roomie.rest.model.*
import edu.agh.roomie.service.UserService.UsersTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class MatchService(database: Database) {

  class InvitationEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<InvitationEntity>(InvitationTable) {
      fun findMatchesForUser(userId: Int) = find {
        (InvitationTable.userId eq userId) and (InvitationTable.requestStatus eq MatchStatus.ACK) and (InvitationTable.responseStatus eq MatchStatus.ACK)
      }.toList().map { it.matchedUserId }

      fun findRequestReceivedForUser(userId: Int) = find {
        (InvitationTable.matchedUserId eq userId) and (InvitationTable.responseStatus eq MatchStatus.NONE) and (InvitationTable.requestStatus eq MatchStatus.ACK)
      }.toList().map { it.userId }

      fun findResponseSentForUser(userId: Int) = find {
        (InvitationTable.userId eq userId) and (InvitationTable.requestStatus eq MatchStatus.ACK) and (InvitationTable.responseStatus eq MatchStatus.NONE)
      }.toList().map { it.matchedUserId }
    }

    var userId by InvitationTable.userId
    var matchedUserId by InvitationTable.matchedUserId
    var createdAt by InvitationTable.createdAt
    var requestStatus by InvitationTable.requestStatus
    var responseStatus by InvitationTable.responseStatus
  }

  object InvitationTable : IntIdTable() {
    val userId = reference("user_id", UsersTable)
    val matchedUserId = reference("matched_user_id", UsersTable)
    val createdAt = varchar("created_at", 50).default(System.currentTimeMillis().toString())
    val requestStatus = enumeration<MatchStatus>("request_status")
    val responseStatus = enumeration<MatchStatus>("response_status").default(MatchStatus.NONE)
  }

  init {
    transaction(database) {
      SchemaUtils.create(InvitationTable)
    }
  }

  suspend fun getResultsForUser(userId: Int): MatchResultResponse = dbQuery {
    val user = UserService.UserEntity.findById(userId)!!.toShared()

    val matches = InvitationEntity.findMatchesForUser(userId)
    val sentRequests = InvitationEntity.findResponseSentForUser(userId)
    val receivedRequests = InvitationEntity.findRequestReceivedForUser(userId)
    val allUsers = UserService.UserEntity.findByIds(
      matches + sentRequests + receivedRequests
    )

    MatchResultResponse(
      allUsers.filter { it.id in matches }.map { Match(it.toShared(), countScore(it.toShared(), user)) },
      allUsers.filter { it.id in sentRequests }.map { Match(it.toShared(), countScore(it.toShared(), user)) },
      allUsers.filter { it.id in receivedRequests }.map { Match(it.toShared(), countScore(it.toShared(), user)) }
    )
  }

  suspend fun getAvailableMatchesForUser(userId: Int): List<User> = dbQuery {
    val requestsSent = InvitationEntity.findResponseSentForUser(userId)

    UserService.UserEntity
      .find {
        (UsersTable.id neq userId) and (UsersTable.id notInList requestsSent)
      }
      .map { it.toShared() }
  }

  suspend fun getResponseSentForUser(userId: Int): List<User> = dbQuery {
    val requests = InvitationEntity.findResponseSentForUser(userId)
    UserService.UserEntity.findByIds(requests).map { it.toShared() }
  }

  suspend fun getRequestReceivedForUser(userId: Int): List<User> = dbQuery {
    val requests = InvitationEntity.findRequestReceivedForUser(userId)
    UserService.UserEntity.findByIds(requests).map { it.toShared() }
  }

  suspend fun registerSwipe(thisUserId: Int, swipedUserId: Int, status: MatchStatus) = dbQuery {
    val thisUser = UserService.UserEntity.findById(thisUserId)
    val swipedUser = UserService.UserEntity.findById(swipedUserId)

    if (thisUser == null || swipedUser == null) {
      throw IllegalArgumentException("User not found")
    }

    val result = InvitationTable.upsertReturning(
      where = {
        (InvitationTable.userId eq thisUser.id) and (InvitationTable.matchedUserId eq swipedUser.id)
      }
    ) {
      it[requestStatus] = status
    }.single()[InvitationTable.responseStatus]

    InvitationTable.upsert(
      where = {
        (InvitationTable.matchedUserId eq thisUser.id) and (InvitationTable.userId eq swipedUser.id)
      }
    ) {
      it[responseStatus] = status
    }

    result
  }
}
