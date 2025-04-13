package edu.agh.roomie.rest.endpoints

import edu.agh.roomie.rest.model.AuthResponse
import edu.agh.roomie.rest.model.DeleteRequest
import edu.agh.roomie.rest.model.LoginRequest
import edu.agh.roomie.rest.model.RegisterRequest
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import kotlin.test.*
import edu.agh.roomie.rest.endpoints.EndpointTestUtils.requestWithAuth

class AuthRoutingTest {

    @Test
    fun `test register endpoint`() = EndpointTestUtils.createTestApplication { dependencies ->
        // Configure the auth routing
        application {
            with(dependencies) {
                configureAuthRouting()
            }
        }

        // Test register endpoint
        val response = client.post("/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(EndpointTestUtils.jsonConfiguration.encodeToString(RegisterRequest.serializer(), RegisterRequest("test@example.com", "password123")))
        }

        // Assert
        assertEquals(HttpStatusCode.Created, response.status)
        val responseBody = response.bodyAsText()
        val authResponse = EndpointTestUtils.jsonConfiguration.decodeFromString(AuthResponse.serializer(), responseBody)
        assertNotNull(authResponse.token)
        assertNotNull(authResponse.userId)
    }

    @Test
    fun `test login endpoint`() = EndpointTestUtils.createTestApplication { dependencies ->
        // Configure the auth routing
        application {
            with(dependencies) {
                configureAuthRouting()
            }
        }

        // Register a user first
        val email = "login-test@example.com"
        val password = "password123"

        client.post("/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(EndpointTestUtils.jsonConfiguration.encodeToString(RegisterRequest.serializer(), RegisterRequest(email, password)))
        }

        // Test login endpoint
        val response = client.post("/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(EndpointTestUtils.jsonConfiguration.encodeToString(LoginRequest.serializer(), LoginRequest(email, password)))
        }

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)
        val responseBody = response.bodyAsText()
        val authResponse = EndpointTestUtils.jsonConfiguration.decodeFromString(AuthResponse.serializer(), responseBody)
        assertNotNull(authResponse.token)
        assertNotNull(authResponse.userId)
    }

    @Test
    fun `test login with invalid credentials`() = EndpointTestUtils.createTestApplication { dependencies ->
        // Configure the auth routing
        application {
            with(dependencies) {
                configureAuthRouting()
            }
        }

        // Test login endpoint with invalid credentials
        val response = client.post("/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(EndpointTestUtils.jsonConfiguration.encodeToString(LoginRequest.serializer(), LoginRequest("nonexistent@example.com", "wrongpassword")))
        }

        // Assert
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun `test logout endpoint`() = EndpointTestUtils.createTestApplication { dependencies ->
        // Configure the auth routing
        application {
            with(dependencies) {
                configureAuthRouting()
            }
        }

        // Register a user first
        val registerResponse = client.post("/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(EndpointTestUtils.jsonConfiguration.encodeToString(RegisterRequest.serializer(), RegisterRequest("logout-test@example.com", "password123")))
        }

        val responseBody = registerResponse.bodyAsText()
        val authResponse = EndpointTestUtils.jsonConfiguration.decodeFromString(AuthResponse.serializer(), responseBody)
        val token = authResponse.token

        // Test logout endpoint
        val response = client.requestWithAuth(HttpMethod.Post, "/auth/logout", token)

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Logged out successfully", response.bodyAsText())
    }

    @Test
    fun `test unregister endpoint`() = EndpointTestUtils.createTestApplication { dependencies ->
        // Configure the auth routing
        application {
            with(dependencies) {
                configureAuthRouting()
            }
        }

        // Register a user first
        val registerResponse = client.post("/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(EndpointTestUtils.jsonConfiguration.encodeToString(RegisterRequest.serializer(), RegisterRequest("unregister-test@example.com", "password123")))
        }

        val responseBody = registerResponse.bodyAsText()
        val authResponse = EndpointTestUtils.jsonConfiguration.decodeFromString(AuthResponse.serializer(), responseBody)
        val userId = authResponse.userId

        // Test unregister endpoint
        val response = client.delete("/auth/unregister") {
            contentType(ContentType.Application.Json)
            setBody(EndpointTestUtils.jsonConfiguration.encodeToString(DeleteRequest.serializer(), DeleteRequest(userId)))
        }

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("User unregistered successfully", response.bodyAsText())

        // Try to login with the unregistered user
        val loginResponse = client.post("/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(EndpointTestUtils.jsonConfiguration.encodeToString(LoginRequest.serializer(), LoginRequest("unregister-test@example.com", "password123")))
        }

        // Assert
        assertEquals(HttpStatusCode.Unauthorized, loginResponse.status)
    }
}
