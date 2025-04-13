package edu.agh.roomie

import edu.agh.roomie.rest.Dependencies
import edu.agh.roomie.rest.configureHTTP
import edu.agh.roomie.rest.configureRouting
import edu.agh.roomie.rest.endpoints.configureAuthRouting
import edu.agh.roomie.rest.endpoints.configureUserRouting
import edu.agh.roomie.rest.model.RegisterRequest
import edu.agh.roomie.service.AuthService
import edu.agh.roomie.service.FakeUserGenerator
import edu.agh.roomie.service.MatchService
import edu.agh.roomie.service.UserService
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.transactions.transaction

fun main(args: Array<String>) {
  io.ktor.server.netty.EngineMain.main(args)
}

val isDeployment = System.getenv("DEPLOYMENT") == "true"

fun Application.module() {
  val database = configureDatabases()
  with(
    Dependencies(
      database = database,
      userService = UserService(database),
      authService = AuthService(),
      matchService = MatchService(database),
    )
  ) {
    if (!isDeployment) transaction {
      generateFakeData()
    }

    val user = userService.register(RegisterRequest("admin@admin.pl", "admin"))
    userService.upsertUserInfo(user, FakeUserGenerator.generateFakeInfo())
    userService.upsertUserPreferences(user, FakeUserGenerator.generateFakePreferences())

    val user2 = userService.register(RegisterRequest("admin2@admin.pl", "admin"))
    userService.upsertUserInfo(user2, FakeUserGenerator.generateFakeInfo())
    userService.upsertUserPreferences(user2, FakeUserGenerator.generateFakePreferences())

    configureHTTP()
    configureRouting()
    configureAuthRouting()
    configureUserRouting()
  }
}
