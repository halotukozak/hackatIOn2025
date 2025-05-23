package edu.agh.roomie.rest

import edu.agh.roomie.rest.endpoints.configureAuthRouting
import edu.agh.roomie.rest.endpoints.configureInitialRouting
import edu.agh.roomie.rest.endpoints.configureMatchRouting
import edu.agh.roomie.rest.endpoints.configureUserRouting
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

context(Dependencies)
fun Application.configureRouting() {
    install(StatusPages) {
        exception<IllegalArgumentException> { call, cause ->
            call.respondText(text = "400: $cause", status = HttpStatusCode.BadRequest)
        }
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }
    install(ContentNegotiation) {
        json()
    }
    routing {
        swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml")
    }
    routing {
        get("/health") {
            call.respond(HttpStatusCode.OK, "OK")
        }
    }
    configureInitialRouting()
    configureAuthRouting()
    configureUserRouting()
    configureMatchRouting()
}
