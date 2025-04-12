package edu.agh.roomie.rest

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*

fun Application.configureHTTP() {
  install(Compression)
  install(CORS) {
    anyHost() // or host("localhost:5174")

    allowMethod(HttpMethod.Options)
    allowMethod(HttpMethod.Put)
    allowMethod(HttpMethod.Delete)
    allowMethod(HttpMethod.Patch)
    allowMethod(HttpMethod.Post) // ← add this

    allowHeader(HttpHeaders.Authorization)
    allowHeader(HttpHeaders.ContentType) // ← add this
    allowHeader("MyCustomHeader")

    allowCredentials = true // ← optional, if you're using cookies
  }
}
