package edu.agh.roomie.rest

import edu.agh.roomie.rest.endpoints.configureAuthRouting
import edu.agh.roomie.rest.endpoints.configureInitialRouting
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

context(Dependencies)
fun Application.configureRouting() {
  install(StatusPages) {
    exception<Throwable> { call, cause ->
      call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
    }
  }
  configureInitialRouting()
  configureAuthRouting()
}
