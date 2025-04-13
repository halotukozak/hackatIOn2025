package edu.agh.roomie

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

object TestUtils {
  private val postgresContainer by lazy {
    PostgreSQLContainer<Nothing>(DockerImageName.parse("postgres:15-alpine")).apply {
      withDatabaseName("testdb")
      withUsername("test")
      withPassword("test")
      start()
    }
  }

  /**
   * Creates a PostgreSQL database for testing using TestContainers
   */

  val testDatabase: Database by lazy {
    Database.connect(
      url = postgresContainer.jdbcUrl,
      user = postgresContainer.username,
      driver = "org.postgresql.Driver",
      password = postgresContainer.password
    )
  }

  fun createTestDatabase(): Database {
    // Ensure the container is started
    if (!postgresContainer.isRunning) {
      postgresContainer.start()
    }

    return testDatabase.also {
      transaction { SchemaUtils.dropDatabase() }
    }
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
