package edu.agh.roomie.scripts

import edu.agh.roomie.rest.model.*
import kotlin.test.Test
import kotlin.test.assertTrue

class CompanionTest {

    @Test
    fun `calculateCost should return 0 when users have identical preferences and info`() {
        val user1 = User(
            email = "user1@example.com",
            info = Info(
                age = 20,
                description = "",
                sleepSchedule = Pair(22, 6),
                hobbies = listOf(Hobby.basketball, Hobby.climbing),
                smoke = false,
                drink = false,
                personalityType = 5,
                yearOfStudy = 2,
                faculty = Faculty.WEAIIB,
                relationshipStatus = 1
            ),
            preferences = Preferences(
                sleepScheduleMatters = true,
                hobbiesMatters = true,
                smokingImportance = 5,
                drinkImportance = 5,
                personalityTypeImportance = 5,
                yearOfStudyMatters = true,
                facultyMatters = true,
                relationshipStatusImportance = 1
            )
        )
        val user2 = user1.copy()

        val cost = CostFunction.calculateCost(user1, user2)
        System.out.println("Equal: " + cost)
        assertTrue(cost > 0.95, "Cost calculation failed for identical users")
    }

    @Test
    fun `calculateCost should handle differences in sleep schedule`() {
        val user1 = User(
            email = "user1@example.com",
            info = Info(
                age = 20,
                description = "",
                sleepSchedule = Pair(22, 6),
                hobbies = listOf(Hobby.basketball, Hobby.climbing),
                smoke = false,
                drink = false,
                personalityType = 5,
                yearOfStudy = 2,
                faculty = Faculty.WEAIIB,
                relationshipStatus = 1
            ),
            preferences = Preferences(
                sleepScheduleMatters = true,
                hobbiesMatters = false,
                smokingImportance = 5,
                drinkImportance = 5,
                personalityTypeImportance = 5,
                yearOfStudyMatters = false,
                facultyMatters = false,
                relationshipStatusImportance = 1
            )
        )
        val user2 = User(
            email = "user2@example.com",
            info = Info(
                age = 20,
                description = "",
                sleepSchedule = Pair(18, 5),
                hobbies = listOf(Hobby.basketball, Hobby.climbing),
                smoke = false,
                drink = false,
                personalityType = 5,
                yearOfStudy = 2,
                faculty = Faculty.WEAIIB,
                relationshipStatus = 1
            ),
            preferences = Preferences(
                sleepScheduleMatters = true,
                hobbiesMatters = false,
                smokingImportance = 5,
                drinkImportance = 5,
                personalityTypeImportance = 5,
                yearOfStudyMatters = false,
                facultyMatters = false,
                relationshipStatusImportance = 1
            )
        )

        val cost = CostFunction.calculateCost(user1, user2)
        System.out.println("Sleep: " + cost)
        assertTrue(cost < 0.2, "Sleep")
    }

    @Test
    fun `calculateCost should handle differences in sleep no shared hobbies`() {
        val user1 = User(
            email = "user1@example.com",
            info = Info(
                age = 20,
                description = "",
                sleepSchedule = Pair(22, 6),
                hobbies = listOf(Hobby.basketball, Hobby.climbing),
                smoke = false,
                drink = false,
                personalityType = 5,
                yearOfStudy = 2,
                faculty = Faculty.WEAIIB,
                relationshipStatus = 1
            ),
            preferences = Preferences(
                sleepScheduleMatters = true,
                hobbiesMatters = true,
                smokingImportance = 5,
                drinkImportance = 5,
                personalityTypeImportance = 5,
                yearOfStudyMatters = true,
                facultyMatters = true,
                relationshipStatusImportance = 1
            )
        )
        val user2 = User(
            email = "user2@example.com",
            info = Info(
                age = 20,
                description = "",
                sleepSchedule = Pair(22, 6),
                hobbies = listOf(Hobby.cooking, Hobby.cycling),
                smoke = false,
                drink = false,
                personalityType = 5,
                yearOfStudy = 2,
                faculty = Faculty.WEAIIB,
                relationshipStatus = 1
            ),
            preferences = Preferences(
                sleepScheduleMatters = true,
                hobbiesMatters = true,
                smokingImportance = 5,
                drinkImportance = 5,
                personalityTypeImportance = 5,
                yearOfStudyMatters = true,
                facultyMatters = true,
                relationshipStatusImportance = 1
            )
        )

        val cost = CostFunction.calculateCost(user1, user2)
        System.out.println("Hobbies: " + cost)
        assertTrue(cost < 0.2, "Hobbies")
    }

    @Test
    fun `calculateCost should add cost for different faculties`() {
        val user1 = User(
            email = "user1@example.com",
            info = Info(
                age = 20,
                description = "",
                sleepSchedule = Pair(22, 6),
                hobbies = listOf(Hobby.basketball, Hobby.climbing),
                smoke = false,
                drink = false,
                personalityType = 5,
                yearOfStudy = 2,
                faculty = Faculty.WMS,
                relationshipStatus = 1
            ),
            preferences = Preferences(
                sleepScheduleMatters = true,
                hobbiesMatters = true,
                smokingImportance = 5,
                drinkImportance = 5,
                personalityTypeImportance = 5,
                yearOfStudyMatters = true,
                facultyMatters = true,
                relationshipStatusImportance = 1
            )
        )
        val user2 = User(
            email = "user2@example.com",
            info = Info(
                age = 20,
                description = "",
                sleepSchedule = Pair(22, 6),
                hobbies = listOf(Hobby.cooking, Hobby.cycling),
                smoke = false,
                drink = false,
                personalityType = 5,
                yearOfStudy = 2,
                faculty = Faculty.WEAIIB,
                relationshipStatus = 1
            ),
            preferences = Preferences(
                sleepScheduleMatters = true,
                hobbiesMatters = true,
                smokingImportance = 5,
                drinkImportance = 5,
                personalityTypeImportance = 5,
                yearOfStudyMatters = true,
                facultyMatters = true,
                relationshipStatusImportance = 1
            )
        )

        val cost = CostFunction.calculateCost(user1, user2)
        System.out.println("Faculty: " + cost)
        assertTrue(cost < 0.2, "Faculty")
    }

    @Test
    fun `calculateCost should be high for very different users`() {
        val user1 = User(
            email = "user1@example.com",
            info = Info(
                age = 20,
                description = "",
                sleepSchedule = Pair(22, 6),
                hobbies = listOf(Hobby.basketball, Hobby.climbing),
                smoke = false,
                drink = false,
                personalityType = 1,
                yearOfStudy = 1,
                faculty = Faculty.WMS,
                relationshipStatus = 1
            ),
            preferences = Preferences(
                sleepScheduleMatters = true,
                hobbiesMatters = true,
                smokingImportance = 5,
                drinkImportance = 5,
                personalityTypeImportance = 5,
                yearOfStudyMatters = true,
                facultyMatters = true,
                relationshipStatusImportance = 5
            )
        )

        val user2 = User(
            email = "user2@example.com",
            info = Info(
                age = 30,
                description = "",
                sleepSchedule = Pair(2, 10),
                hobbies = listOf(Hobby.painting, Hobby.music),
                smoke = true,
                drink = true,
                personalityType = 8,
                yearOfStudy = 5,
                faculty = Faculty.WI,
                relationshipStatus = 3
            ),
            preferences = Preferences(
                sleepScheduleMatters = true,
                hobbiesMatters = true,
                smokingImportance = 5,
                drinkImportance = 5,
                personalityTypeImportance = 5,
                yearOfStudyMatters = true,
                facultyMatters = true,
                relationshipStatusImportance = 5
            )
        )

        val cost = CostFunction.calculateCost(user1, user2)
        System.out.println("Differ: " + cost)
        assertTrue(cost > 0.7, "Cost should be high for very different users")
    }
}