package edu.agh.roomie

import edu.agh.edu.agh.roomie.configureHTTP
import edu.agh.edu.agh.roomie.configureRouting
import edu.agh.edu.agh.roomie.configureSecurity
import edu.agh.edu.agh.roomie.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {
  io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
  configureSecurity()
  configureHTTP()
  configureSerialization()
  configureDatabases()
  configureRouting()
}
