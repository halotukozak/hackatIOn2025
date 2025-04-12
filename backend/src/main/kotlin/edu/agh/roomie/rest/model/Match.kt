package edu.agh.roomie.rest.model

import kotlinx.serialization.Serializable

@Serializable
data class MatchResponse(
    val userId: Int,
    val name: String,
    val surname: String
)

@Serializable
data class SwipeResponse(
    val isMatch: Boolean,
    val message: String
)
