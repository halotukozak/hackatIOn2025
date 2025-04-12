package edu.agh.roomie.rest

import edu.agh.roomie.rest.model.Departament
import edu.agh.roomie.rest.model.Hobby
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureInitialRouting() {
  routing {
    route("/registration") {
      get("/available-hobbies") {
        call.respond(HttpStatusCode.OK, Hobby.entries)
      }
      get("/available-departments") {
        call.respond(HttpStatusCode.OK, Departament.entries)
      }
    }
  }
}
