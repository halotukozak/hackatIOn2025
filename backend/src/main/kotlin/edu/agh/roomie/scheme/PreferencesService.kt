package edu.agh.roomie.scheme

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database

class PreferencesService(database: Database) {
  class PreferencesEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PreferencesEntity>(PreferencesTable)

    var sleepScheduleMatters by PreferencesTable.sleepScheduleMatters
    var hobbiesMatters by PreferencesTable.hobbiesMatters
    var smokingImportance by PreferencesTable.smokingImportance
    var drinkImportance by PreferencesTable.drinkImportance
    var personalityTypeImportance by PreferencesTable.personalityTypeImportance
    var yearOfStudyMatters by PreferencesTable.yearOfStudyMatters
    var facultyMatters by PreferencesTable.facultyMatters
    var relationshipStatusImportance by PreferencesTable.relationshipStatusImportance
  }

  object PreferencesTable : IntIdTable() {
    val sleepScheduleMatters = bool("sleep_schedule_matters")
    val hobbiesMatters = bool("hobbies_matters")
    val smokingImportance = integer("smoking_importance")
    val drinkImportance = integer("drink_importance")
    val personalityTypeImportance = integer("personality_type_importance") // Scale: 1 (not important) to 5 (very important)
    val yearOfStudyMatters = bool("year_of_study_matters") // Scale: 1 (not important) to 5 (very important)
    val facultyMatters = bool("faculty_matters")
    val relationshipStatusImportance = integer("relationship_status_importance") // Scale: 1 (not important) to 5 (very important)
  }
}
