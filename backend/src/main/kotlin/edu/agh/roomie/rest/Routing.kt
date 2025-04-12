package edu.agh.roomie.rest

import edu.agh.roomie.rest.endpoints.configureAuthRouting
import edu.agh.roomie.rest.endpoints.configureInitialRouting
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

context(Dependencies)
fun Application.configureRouting() {
  install(StatusPages) {
    exception<Throwable> { call, cause ->
      call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
    }
  }
  routing {
    swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml")

    get("/users/match/{id}") {
      val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")

      val users = listOf(
//        User("1", "John Doe"),
//        User("2", "Jane Smith"),
//        User("3", "Alice Brown")
      )

      call.respond(users)
    }
  }
  configureInitialRouting()
  configureAuthRouting()
}
