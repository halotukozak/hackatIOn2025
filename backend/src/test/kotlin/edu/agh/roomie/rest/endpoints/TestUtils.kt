package edu.agh.roomie.rest.endpoints

import edu.agh.roomie.TestUtils
import edu.agh.roomie.rest.Dependencies
import edu.agh.roomie.service.AuthService
import edu.agh.roomie.service.MatchService
import edu.agh.roomie.service.UserService
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object EndpointTestUtils {
    /**
     * Creates a test application with the necessary dependencies
     */
    fun createTestApplication(
        block: suspend ApplicationTestBuilder.(Dependencies) -> Unit
    ) = testApplication {
        // Create an in-memory database
        val database = TestUtils.createTestDatabase()

        // Create services
        val userService = UserService(database)
        val authService = AuthService()
        val matchService = MatchService(database)

        // Create dependencies
        val dependencies = Dependencies(
            database = database,
            userService = userService,
            authService = authService,
            matchService = matchService
        )

        // Configure content negotiation for the application
        application {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                })
            }
        }


        // Run the test
        block(dependencies)
    }

    /**
     * Makes a request with an authorization header
     */
    suspend fun HttpClient.requestWithAuth(
        method: HttpMethod,
        path: String,
        token: String,
        setup: HttpRequestBuilder.() -> Unit = {}
    ): HttpResponse {
        return request(path) {
            this.method = method
            header(HttpHeaders.Authorization, "Bearer $token")
            setup()
        }
    }

    /**
     * JSON configuration for serialization/deserialization
     */
    val jsonConfiguration = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    }
}
