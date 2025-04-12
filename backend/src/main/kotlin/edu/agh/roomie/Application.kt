package edu.agh.roomie

import edu.agh.roomie.rest.configureHTTP
import edu.agh.roomie.rest.configureRouting
import io.ktor.server.application.*

fun main(args: Array<String>) {
  io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
  configureHTTP()
  configureDatabases()
  configureRouting()
}
