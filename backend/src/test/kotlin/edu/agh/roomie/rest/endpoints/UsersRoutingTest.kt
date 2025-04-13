package edu.agh.roomie.rest.endpoints

import edu.agh.roomie.rest.model.RegisterRequest
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import kotlin.test.Test

class UsersRoutingTest : BaseRoutingTest() {

    @Test
    fun testGetAllUsersEndpoint() = testApplication {
        // Configure the test application
        configureTestApplication {
            routing {
                with(dependencies) {
                    get("/users") {
                        val users = userService.getAllUsers()
                        call.respond(HttpStatusCode.OK, users)
                    }
                }
            }
        }

        // Register a few users
        userService.register(RegisterRequest(email = "user5@example.com", password = "password123"))
        userService.register(RegisterRequest(email = "user6@example.com", password = "password123"))

        // Test getting all users
        val response = client.get("/users")

        // Verify response
        TestUtils.verifyResponseStatus(response, HttpStatusCode.OK)
        TestUtils.verifyResponseContains(response, "user5@example.com")
        TestUtils.verifyResponseContains(response, "user6@example.com")
    }
}