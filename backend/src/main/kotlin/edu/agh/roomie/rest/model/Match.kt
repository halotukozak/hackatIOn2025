package edu.agh.roomie.rest.model

import kotlinx.serialization.Serializable

@Serializable
enum class MatchStatus {
    ACK, NACK, NONE
}

@Serializable
data class Match(
    val user: User,
    val score: Int,
)

@Serializable
data class MatchResultResponse(
    val matches: List<Match>,
    val sentRequests: List<Match>,
    val receivedRequests: List<Match>,
)
