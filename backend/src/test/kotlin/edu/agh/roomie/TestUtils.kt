package edu.agh.roomie

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

object TestUtils {
    /**
     * Creates an in-memory H2 database for testing
     */
    fun createTestDatabase(): Database {
        return Database.connect(
            url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
            user = "root",
            driver = "org.h2.Driver",
            password = ""
        )
    }
    
    /**
     * Executes a transaction on the test database
     */
    fun <T> withTestDatabase(block: () -> T): T {
        val database = createTestDatabase()
        return transaction(database) {
            block()
        }
    }
}