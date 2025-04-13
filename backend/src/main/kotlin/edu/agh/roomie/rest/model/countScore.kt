package edu.agh.roomie.rest.model

import kotlin.math.log10
import kotlin.reflect.full.memberProperties


class CostFunction {

    companion object {
        private var MAX_VALUE = 1.0
        private var MIN_VALUE = 0.0
        private var MAX_INT_PREFERENCES_VALUE = 3

        fun countScore(user: User, other: User): Int {
            var totalCost = 0.0
            val importantPreferencesCount = countImportantPreferences(user.preferences)
            val defaultAddCost = MAX_VALUE / if (importantPreferencesCount == 0) 1 else importantPreferencesCount

            if(user.preferences.sleepScheduleMatters) {
                val userSleepStart = parseSleepHours(user.info.sleepSchedule.first)
                val userSleepEnd = parseSleepHours(user.info.sleepSchedule.second)
                val otherSleepStart = parseSleepHours(other.info.sleepSchedule.first)
                val otherSleepEnd = parseSleepHours(other.info.sleepSchedule.second)

                val intersect = calculateIntersect(userSleepStart, userSleepEnd, otherSleepStart, otherSleepEnd).toDouble()
                val union = calculateUnion(userSleepStart, userSleepEnd, otherSleepStart, otherSleepEnd).toDouble()

                totalCost +=  defaultAddCost * log10(1 + 9 * ((union - intersect) / union)) / log10(10.0)
            }

            if (user.preferences.hobbiesMatters) {
                var hobbiesCost = defaultAddCost
                for (hobby in user.info.hobbies) {
                    if (other.info.hobbies.contains(hobby)) {
                        hobbiesCost -= defaultAddCost / user.info.hobbies.size
                    }
                }
                totalCost += hobbiesCost
            }

            if (user.preferences.smokingImportance != null && user.info.smoke != other.info.smoke) {
                val smokeCost = getDiff(user.info.smoke, other.info.smoke, 2)
                totalCost += (user.preferences.smokingImportance / MAX_INT_PREFERENCES_VALUE) * smokeCost * defaultAddCost
            }

            if (user.preferences.drinkImportance != null && user.info.drink == other.info.drink) {
                val drinkCost = getDiff(user.info.drink, other.info.drink, 2)
                totalCost += (user.preferences.drinkImportance / MAX_INT_PREFERENCES_VALUE) * drinkCost * defaultAddCost
            }

            if (user.preferences.personalityTypeImportance != null && user.info.personalityType == other.info.personalityType) {
                val personalityTypeCost = getLnDiff(user.info.personalityType, other.info.personalityType, 100)
                totalCost += (user.preferences.personalityTypeImportance / MAX_INT_PREFERENCES_VALUE) * personalityTypeCost * defaultAddCost
            }

            if (user.preferences.yearOfStudyMatters && user.info.yearOfStudy != other.info.yearOfStudy) {
                totalCost += getDiff(user.info.yearOfStudy, other.info.yearOfStudy, 5) * defaultAddCost
            }

            if (user.preferences.facultyMatters && user.info.faculty != other.info.faculty) {
                totalCost += defaultAddCost
            }

            if (user.preferences.relationshipStatusImportance != null && user.info.relationshipStatus != other.info.relationshipStatus) {
                if (user.info.relationshipStatus == null || other.info.relationshipStatus == null) {
                    totalCost += defaultAddCost
                } else {
                    val relationshipStatusCost = getDiff(user.info.relationshipStatus, other.info.relationshipStatus, 2)
                    totalCost += (user.preferences.relationshipStatusImportance / MAX_INT_PREFERENCES_VALUE) * relationshipStatusCost * defaultAddCost
                }
            }

            val costAsInt = (totalCost * 100).toInt()
            if (costAsInt < 0) {
                return 0
            } else if (costAsInt > 100) {
                return 100
            }
            return 100 - costAsInt
        }


        private fun countImportantPreferences(preferences: Preferences): Int {
            val checkedIntegers = Preferences::class.memberProperties
                .filter { it.returnType.classifier == Int::class }
                .count { it.get(preferences) != null }

            val trueBooleans = Preferences::class.memberProperties
                .filter { it.returnType.classifier == Boolean::class }
                .count { it.get(preferences) == true }

            return trueBooleans + checkedIntegers
        }


        private fun parseSleepHours(time: String): Int {
            val parts = time.split(":")
            return parts[0].toInt()
        }


        private fun hourRangeSet(start: Int, end: Int): Set<Int> {
            return if (start < end) {
                (start until end).toSet()
            } else {
                (start until 24).toSet() + (0 until end).toSet()
            }
        }

        private fun calculateIntersect(start1: Int, end1: Int, start2: Int, end2: Int): Int {
            val range1 = hourRangeSet(start1, end1)
            val range2 = hourRangeSet(start2, end2)
            return range1.intersect(range2).size
        }

        private fun calculateUnion(start1: Int, end1: Int, start2: Int, end2: Int): Int {
            val range1 = hourRangeSet(start1, end1)
            val range2 = hourRangeSet(start2, end2)
            return range1.union(range2).size
        }

        private fun getDiff(userValue: Int, otherValue: Int, maxDiff: Int): Double {
            val diff = kotlin.math.abs(userValue - otherValue).toDouble()
            return diff / maxDiff
        }

        private fun getLnDiff(userValue: Int, otherValue: Int, maxDiff: Int): Double {
            val diff = kotlin.math.abs(userValue - otherValue).toDouble()
            val scaled = diff / maxDiff
            return log10(1 + 9 * scaled) / log10(10.0)
        }
    }
}