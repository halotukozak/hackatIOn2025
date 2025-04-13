package edu.agh.roomie.scripts

import kotlin.math.log10
import edu.agh.roomie.rest.model.Preferences
import edu.agh.roomie.rest.model.User
import kotlin.reflect.full.memberProperties

class CostFunction {

    companion object {
        private var MAX_VALUE = 1.0
        private var MIN_VALUE = 0.0
        private var NON_BOOLEAN_PREFERENCES_NUM = 4
        private var MAX_INT_PREFERENCES_VALUE = 5

        fun calculateCost(user: User, other: User): Double {
            var totalCost = 0.0
            val defaultAdd = MAX_VALUE / (NON_BOOLEAN_PREFERENCES_NUM + countTrueBooleans(user.preferences))

            if(user.preferences.sleepScheduleMatters) {
                val userSleepStart = user.info.sleepSchedule.first
                val userSleepEnd = user.info.sleepSchedule.second
                val otherSleepStart = other.info.sleepSchedule.first
                val otherSleepEnd = other.info.sleepSchedule.second

                val intersect = calculateIntersect(userSleepStart, userSleepEnd, otherSleepStart, otherSleepEnd).toDouble()
                val union = calculateUnion(userSleepStart, userSleepEnd, otherSleepStart, otherSleepEnd).toDouble()

                totalCost +=  defaultAdd * log10(1 + 9 * ((union - intersect) / union)) / log10(10.0)
            }

            if (user.preferences.hobbiesMatters) {
                var hasSameHobby = false
                for (hobby in user.info.hobbies) {
                    if (other.info.hobbies.contains(hobby)) {
                        totalCost -= 0.05
                        hasSameHobby = true
                    }
                }
                if (!hasSameHobby) {
                    totalCost += defaultAdd
                }
            }

            if (user.info.smoke == other.info.smoke) {
                totalCost += (user.preferences.smokingImportance / MAX_INT_PREFERENCES_VALUE) * defaultAdd
            }

            if (user.info.drink == other.info.drink) {
                totalCost += (user.preferences.drinkImportance / MAX_INT_PREFERENCES_VALUE) * defaultAdd
            }

            val personalityDiff = getLnDiff(user.info.personalityType, other.info.personalityType, 100)
            totalCost += (user.preferences.personalityTypeImportance / MAX_INT_PREFERENCES_VALUE) * personalityDiff * defaultAdd

            if (user.preferences.yearOfStudyMatters) {
                totalCost += getDiff(user.info.yearOfStudy, other.info.yearOfStudy, 5) * defaultAdd
            }

            if (user.preferences.facultyMatters) {
                if (user.info.faculty != other.info.faculty) {
                    totalCost += 0.1
                } else {
                    val extraCost = defaultAdd  // TODO
                    totalCost += extraCost
                }
            }

            val relationshipStatusDiff = getDiff(user.info.relationshipStatus, other.info.relationshipStatus, 100)
            totalCost += (user.preferences.personalityTypeImportance / MAX_INT_PREFERENCES_VALUE) * relationshipStatusDiff * defaultAdd

            return totalCost
        }


        private fun countTrueBooleans(preferences: Preferences): Int {
            return Preferences::class.memberProperties
                .filter { it.returnType.classifier == Boolean::class }
                .count { it.get(preferences) == true }
        }

        fun hourRangeSet(start: Int, end: Int): Set<Int> {
            return if (start < end) {
                (start until end).toSet()
            } else {
                (start until 24).toSet() + (0 until end).toSet()
            }
        }

        fun calculateIntersect(start1: Int, end1: Int, start2: Int, end2: Int): Int {
            val range1 = hourRangeSet(start1, end1)
            val range2 = hourRangeSet(start2, end2)
            return range1.intersect(range2).size
        }

        fun calculateUnion(start1: Int, end1: Int, start2: Int, end2: Int): Int {
            val range1 = hourRangeSet(start1, end1)
            val range2 = hourRangeSet(start2, end2)
            return range1.union(range2).size
        }

        fun getDiff(userValue: Int, otherValue: Int, maxDiff: Int): Double {
            val diff = kotlin.math.abs(userValue - otherValue).toDouble()
            return diff / maxDiff
        }

        fun getLnDiff(userValue: Int, otherValue: Int, maxDiff: Int): Double {
            val diff = kotlin.math.abs(userValue - otherValue).toDouble()
            val scaled = diff / maxDiff
            return log10(1 + 9 * scaled) / log10(10.0)
        }
    }
}