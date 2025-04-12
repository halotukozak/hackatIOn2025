package edu.agh.roomie.rest.endpoints

import edu.agh.roomie.rest.Dependencies
import edu.agh.roomie.rest.model.AuthResponse
import edu.agh.roomie.rest.model.DeleteRequest
import edu.agh.roomie.rest.model.LoginRequest
import edu.agh.roomie.rest.model.RegisterRequest
import edu.agh.roomie.service.AuthService
import edu.agh.roomie.service.MatchService
import edu.agh.roomie.service.UserService
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.*
import org.jetbrains.exposed.sql.Database
import kotlin.test.*

class AuthRoutingTest {
    private lateinit var database: Database
    private lateinit var userService: UserService
    private lateinit var authService: AuthService
    private lateinit var matchService: MatchService
    private lateinit var dependencies: Dependencies

    @BeforeTest
    fun setUp() {
        // Set up an in-memory H2 database for testing
        database = Database.connect(
            url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
            driver = "org.h2.Driver"
        )

        // Initialize services
        userService = UserService(database)
        authService = AuthService()
        matchService = MatchService(database)

        // Create dependencies
        dependencies = Dependencies(
            database = database,
            userService = userService,
            authService = authService,
            matchService = matchService
        )
    }

    @Test
    fun testRegisterEndpoint() = testApplication {
        // Configure the test application
        application {
            install(ContentNegotiation) {
                json()
            }
            routing {
                with(dependencies) {
                    route("/auth") {
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
            }
        }

        // Test successful registration
        val response = client.post("/auth/register") {
            contentType(ContentType.Application.Json)
            setBody("""{"email":"test@example.com","password":"password123"}""")
        }

        // Verify response
        assertEquals(HttpStatusCode.Created, response.status)
        val responseText = response.bodyAsText()
        assertTrue(responseText.contains("token"))
        assertTrue(responseText.contains("userId"))
    }

    @Test
    fun testLoginEndpoint() = testApplication {
        // Configure the test application
        application {
            install(ContentNegotiation) {
                json()
            }
            routing {
                with(dependencies) {
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
                    }
                }
            }
        }

        // Register a user first
        val email = "login@example.com"
        val password = "password123"
        val userId = userService.register(RegisterRequest(email = email, password = password))

        // Test successful login
        val response = client.post("/auth/login") {
            contentType(ContentType.Application.Json)
            setBody("""{"email":"$email","password":"$password"}""")
        }

        // Verify response
        assertEquals(HttpStatusCode.OK, response.status)
        val responseText = response.bodyAsText()
        assertTrue(responseText.contains("token"))
        assertTrue(responseText.contains("userId"))

        // Test invalid credentials
        val invalidResponse = client.post("/auth/login") {
            contentType(ContentType.Application.Json)
            setBody("""{"email":"$email","password":"wrongpassword"}""")
        }

        // Verify response
        assertEquals(HttpStatusCode.Unauthorized, invalidResponse.status)
    }

    @Test
    fun testLogoutEndpoint() = testApplication {
        // Configure the test application
        application {
            install(ContentNegotiation) {
                json()
            }
            routing {
                with(dependencies) {
                    route("/auth") {
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
                    }
                }
            }
        }

        // Register a user and get a token
        val userId = userService.register(RegisterRequest(email = "logout@example.com", password = "password123"))
        val token = authService.generateToken(userId)

        // Test successful logout
        val response = client.post("/auth/logout") {
            header("Authorization", "Bearer $token")
        }

        // Verify response
        assertEquals(HttpStatusCode.OK, response.status)

        // Test invalid token
        val invalidResponse = client.post("/auth/logout") {
            header("Authorization", "InvalidToken")
        }

        // Verify response
        assertEquals(HttpStatusCode.BadRequest, invalidResponse.status)
    }

    @Test
    fun testUnregisterEndpoint() = testApplication {
        // Configure the test application
        application {
            install(ContentNegotiation) {
                json()
            }
            routing {
                with(dependencies) {
                    route("/auth") {
                        delete("/unregister") {
                            val deleteRequest = call.receive<DeleteRequest>()
                            userService.removeUser(deleteRequest.userId)
                            authService.removeToken(deleteRequest.userId.toString())
                            call.respond(HttpStatusCode.OK, "User unregistered successfully")
                        }
                    }
                }
            }
        }

        // Register a user
        val userId = userService.register(RegisterRequest(email = "unregister@example.com", password = "password123"))

        // Test successful unregistration
        val response = client.delete("/auth/unregister") {
            contentType(ContentType.Application.Json)
            setBody("""{"userId":$userId}""")
        }

        // Verify response
        assertEquals(HttpStatusCode.OK, response.status)

        // Verify user is removed
        assertNull(userService.getUserById(userId))
    }
}
