package edu.agh.roomie

import edu.agh.roomie.rest.configureHTTP
import edu.agh.roomie.rest.configureRouting
import edu.agh.roomie.rest.configureSecurity
import io.ktor.server.application.*

fun main(args: Array<String>) {
  io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
  configureSecurity()
  configureHTTP()
  configureDatabases()
  configureRouting()
}
