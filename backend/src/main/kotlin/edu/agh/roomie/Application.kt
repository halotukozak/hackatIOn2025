package edu.agh.roomie

import edu.agh.roomie.rest.Dependencies
import edu.agh.roomie.rest.configureHTTP
import edu.agh.roomie.rest.configureRouting
import edu.agh.roomie.service.AuthService
import edu.agh.roomie.service.UserService
import io.ktor.server.application.*

fun main(args: Array<String>) {
  io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

  val database = configureDatabases()
  with
    Dependencies(
      database = database,
      userService = UserService(database),
      authService = AuthService()
    )
  ) {
    configureHTTP()
    configureRouting()
  }
}
