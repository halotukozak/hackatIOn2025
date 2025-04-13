package edu.agh.roomie.service

import edu.agh.roomie.rest.model.*
import io.github.serpro69.kfaker.Faker
import kotlin.random.Random

object FakeUserGenerator {
    private val faker = Faker()

    fun generateFakeUser(userId: Int): User {
        val firstName = faker.name.firstName()
        val lastName = faker.name.lastName()

        return User(
            id = userId,
            email = "${firstName.lowercase()}.${lastName.lowercase()}$userId@example.com",
            info = generateFakeInfo(),
            preferences = generateFakePreferences()
        )
    }


    fun randomMatchStatus(): MatchStatus = MatchStatus.entries.toTypedArray().random()

    private fun generateFakeInfo(): Info {
        val randomFaculty = Faculty.entries.random()
        val randomHobbies = Hobby.entries.shuffled().take(Random.nextInt(1, 5))

        return Info(
            fullName = faker.name.firstName() + " " + faker.name.lastName(),
            gender = Random.nextInt(1, 3),
            age = Random.nextInt(18, 30),
            description = List(Random.nextInt(2, 5)) { faker.lorem.words() }.joinToString(" "),
            sleepSchedule = Pair(generateFakeStartTime(), generateFakeEndTime()),
            hobbies = randomHobbies,
            smoke = Random.nextInt(0, 3),
            drink = Random.nextInt(0, 3),
            personalityType = Random.nextInt(1, 100),
            yearOfStudy = Random.nextInt(1, 6),
            faculty = randomFaculty,
            relationshipStatus = if (Random.nextBoolean()) Random.nextInt(1, 3) else null
        )
    }

    private fun generateFakePreferences(): Preferences {
        return Preferences(
            sleepScheduleMatters = Random.nextBoolean(),
            hobbiesMatters = Random.nextBoolean(),
            smokingImportance = if (Random.nextBoolean() ) Random.nextInt(0, 3) else null,
            drinkImportance = if (Random.nextBoolean()) Random.nextInt(0, 3) else null,
            personalityTypeImportance = if (Random.nextBoolean()) Random.nextInt(0, 3) else null,
            yearOfStudyMatters = Random.nextBoolean(),
            facultyMatters = Random.nextBoolean(),
            relationshipStatusImportance = if (Random.nextBoolean()) Random.nextInt(0, 3) else null
        )
    }

    private fun generateFakeStartTime(): String {
        val hour = Random.nextInt(20, 23)
        val minute = Random.nextInt(0, 60)
        return String.format("%02d:%02d", hour, minute)
    }

    private fun generateFakeEndTime(): String {
        val hour = Random.nextInt(6, 12)
        val minute = Random.nextInt(0, 60)
        return String.format("%02d:%02d", hour, minute)
    }
}
