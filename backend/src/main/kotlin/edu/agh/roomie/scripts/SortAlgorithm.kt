package edu.agh.roomie.scripts

import edu.agh.roomie.rest.model.User

class SortAlgorithm {
    companion object {

        fun sortUsers(user: User, users: List<User>): List<Pair<User, Double>> {
            return users
                .map { otherUser -> otherUser to CostFunction.calculateCost(user, otherUser) }
                .sortedBy { it.second }
        }

        fun getList(user: User, others: List<User>): List<Pair<User, Double>> {
            return sortUsers(user, others)
        }
    }

}