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
            email = "${firstName.lowercase()}.${lastName.lowercase()}$userId@example.com",
            info = generateFakeInfo(),
            preferences = generateFakePreferences()
        )
    }

    private fun generateFakeInfo(): Info {
        val randomFaculty = Faculty.entries.random()
        val randomHobbies = Hobby.entries.shuffled().take(Random.nextInt(1, 5))

        return Info(
            name = faker.name.firstName(),
            surname = faker.name.lastName(),
            age = Random.nextInt(18, 30),
            description = List(Random.nextInt(2, 5)) { faker.lorem.words() }.joinToString(" "),
            sleepSchedule = Pair(Random.nextInt(21, 24), Random.nextInt(5, 9)),
            hobbies = randomHobbies,
            smoke = Random.nextBoolean(),
            drink = Random.nextBoolean(),
            personalityType = Random.nextInt(1, 5),
            yearOfStudy = Random.nextInt(1, 6),
            faculty = randomFaculty,
            relationshipStatus = Random.nextInt(1, 4)
        )
    }

    private fun generateFakePreferences(): Preferences = Preferences(
        sleepScheduleMatters = Random.nextBoolean(),
        hobbiesMatters = Random.nextBoolean(),
        smokingImportance = Random.nextInt(0, 6),
        drinkImportance = Random.nextInt(0, 6),
        personalityTypeImportance = Random.nextInt(0, 6),
        yearOfStudyMatters = Random.nextBoolean(),
        facultyMatters = Random.nextBoolean(),
        relationshipStatusImportance = Random.nextInt(0, 6)
    )
}
