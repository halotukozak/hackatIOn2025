package edu.agh.roomie.rest.endpoints

import edu.agh.roomie.rest.Dependencies
import edu.agh.roomie.rest.model.AuthResponse
import edu.agh.roomie.rest.model.LoginRequest
import edu.agh.roomie.rest.model.RegisterRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

context(Dependencies)
fun Application.configureAuthRouting() = routing {
  route("/auth") {
    post("/login") {
      val loginRequest = call.receive<LoginRequest>()
      val userId = userService.authenticate(loginRequest.email, loginRequest.password)

      if (userId != null) {
        val token = authService.generateToken(userId)
        call.respond(HttpStatusCode.OK, AuthResponse(token = token, userId = userId))
      } else {
        call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
      }
    }

    post("/logout") {
      val authHeader = call.request.headers["Authorization"]
      if (authHeader != null && authHeader.startsWith("Bearer ")) {
        val token = authHeader.substring(7)
        authService.removeToken(token)
        call.respond(HttpStatusCode.OK, "Logged out successfully")
      } else {
        call.respond(HttpStatusCode.BadRequest, "Invalid token")
      }
    }

    post("/register") {
      val registerRequest = call.receive<RegisterRequest>()
      try {
        val userId = userService.register(registerRequest)
        val token = authService.generateToken(userId)
        call.respond(HttpStatusCode.Created, AuthResponse(token = token, userId = userId))
      } catch (e: Exception) {
        call.respond(HttpStatusCode.BadRequest, "Registration failed: ${e.message}")
      }
    }
  }
}