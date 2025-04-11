package edu.agh

import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.jetbrains.exposed.sql.*

fun Application.configureSecurity() {
  authentication {
    basic(name = "myauth1") {
      realm = "Ktor Server"
      validate { credentials ->
        if (credentials.name == credentials.password) {
          UserIdPrincipal(credentials.name)
        } else {
          null
        }
      }
    }

    form(name = "myauth2") {
      userParamName = "user"
      passwordParamName = "password"
      challenge {
        /**/
      }
    }
  }
  authentication {
    oauth("auth-oauth-google") {
      urlProvider = { "http://localhost:8080/callback" }
      providerLookup = {
        OAuthServerSettings.OAuth2ServerSettings(
          name = "google",
          authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
          accessTokenUrl = "https://accounts.google.com/o/oauth2/token",
          requestMethod = HttpMethod.Post,
          clientId = System.getenv("GOOGLE_CLIENT_ID"),
          clientSecret = System.getenv("GOOGLE_CLIENT_SECRET"),
          defaultScopes = listOf("https://www.googleapis.com/auth/userinfo.profile")
        )
      }
      client = HttpClient(Apache)
    }
  }
  routing {
    authenticate("myauth1") {
      get("/protected/route/basic") {
        val principal = call.principal<UserIdPrincipal>()!!
        call.respondText("Hello ${principal.name}")
      }
    }
    authenticate("myauth2") {
      get("/protected/route/form") {
        val principal = call.principal<UserIdPrincipal>()!!
        call.respondText("Hello ${principal.name}")
      }
    }
    authenticate("auth-oauth-google") {
      get("login") {
        call.respondRedirect("/callback")
      }

      get("/callback") {
        val principal: OAuthAccessTokenResponse.OAuth2? = call.authentication.principal()
        call.sessions.set(UserSession(principal?.accessToken.toString()))
        call.respondRedirect("/hello")
      }
    }
  }
}

class UserSession(accessToken: String)
