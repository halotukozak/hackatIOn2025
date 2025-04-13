//package edu.agh.roomie.rest.endpoints
//
//import edu.agh.roomie.rest.model.AdditionalInfoRequest
//import edu.agh.roomie.rest.model.AdditionalPreferencesRequest
//import edu.agh.roomie.rest.model.Hobby
//import edu.agh.roomie.rest.model.RegisterRequest
//import io.ktor.client.request.*
//import io.ktor.http.*
//import io.ktor.server.request.*
//import io.ktor.server.response.*
//import io.ktor.server.routing.*
//import io.ktor.server.testing.*
//import kotlinx.serialization.encodeToString
//import kotlinx.serialization.json.Json
//import kotlin.test.Test
//
//class InitialRoutingTest : BaseRoutingTest() {
//
//  @Test
//  fun testGetAvailableHobbiesEndpoint() = testApplication {
//    // Configure the test application
//    configureTestApplication {
//      routing {
//        with(dependencies) {
//          route("/registration") {
//            get("/available-hobbies") {
//              call.respond(HttpStatusCode.OK, Hobby.entries.toList())
//            }
//          }
//        }
//      }
//    }
//
//    // Test getting available hobbies
//    val response = client.get("/registration/available-hobbies")
//
//    // Verify response
//    TestUtils.verifyResponseStatus(response, HttpStatusCode.OK)
//    TestUtils.verifyResponseContains(response, "music")
//    TestUtils.verifyResponseContains(response, "cooking")
//  }
//
//  @Test
//  fun testGetAvailableDepartmentsEndpoint() = testApplication {
//    // Configure the test application
//    configureTestApplication {
//      routing {
//        with(dependencies) {
//          route("/registration") {
//            get("/available-departments") {
//              call.respond(HttpStatusCode.OK, edu.agh.roomie.rest.model.Faculties.all)
//            }
//          }
//        }
//      }
//    }
//
//    // Test getting available departments
//    val response = client.get("/registration/available-departments")
//
//    // Verify response
//    TestUtils.verifyResponse(response, HttpStatusCode.OK, "WI")
//  }
//
//  @Test
//  fun testPostAdditionalInfoEndpoint() = testApplication {
//    // Configure the test application
//    configureTestApplication {
//      routing {
//        with(dependencies) {
//          route("/registration") {
//            post("/additional-info") {
//              val additionalInfoRequest = call.receive<AdditionalInfoRequest>()
//              val userId = additionalInfoRequest.userId
//              userService.upsertUserInfo(
//                userId,
//                additionalInfoRequest.info,
//              )?.let {
//                call.respond(HttpStatusCode.OK, "User additional data updated successfully")
//              } ?: run {
//                call.respond(HttpStatusCode.BadRequest, "Failed to update user additional data")
//              }
//            }
//          }
//        }
//      }
//    }
//
//    // Register a user first
//    val userId = userService.register(RegisterRequest(email = "info@example.com", password = "password123"))
//
//    // Test posting additional info
//    val info = createTestInfo()
//    val response = client.post("/registration/additional-info") {
//      contentType(ContentType.Application.Json)
//      setBody("""{"userId":$userId,"info":${Json.encodeToString(info)}}""")
//    }
//
//    // Verify response
//    TestUtils.verifyResponseStatus(response, HttpStatusCode.OK)
//    TestUtils.verifyResponseContains(response, "updated successfully")
//
//    // Test with invalid user ID
//    val invalidResponse = client.post("/registration/additional-info") {
//      contentType(ContentType.Application.Json)
//      setBody("""{"userId":999,"info":${Json.encodeToString(info)}}""")
//    }
//
//    // Verify response
//    TestUtils.verifyResponseStatus(invalidResponse, HttpStatusCode.BadRequest)
//  }
//
//  @Test
//  fun testPostAdditionalPreferencesEndpoint() = testApplication {
//    // Configure the test application
//    configureTestApplication {
//      routing {
//        with(dependencies) {
//          route("/registration") {
//            post("/additional-preferences") {
//              val additionalPreferencesRequest = call.receive<AdditionalPreferencesRequest>()
//              val userId = additionalPreferencesRequest.userId
//              userService.upsertUserPreferences(
//                userId,
//                additionalPreferencesRequest.preferences,
//              )?.let {
//                call.respond(HttpStatusCode.OK, "User preferences updated successfully")
//              } ?: run {
//                call.respond(HttpStatusCode.BadRequest, "Failed to update user preferences")
//              }
//            }
//          }
//        }
//      }
//    }
//
//    // Register a user first
//    val userId = userService.register(RegisterRequest(email = "prefs@example.com", password = "password123"))
//
//    // Test posting additional preferences
//    val preferences = createTestPreferences()
//    val response = client.post("/registration/additional-preferences") {
//      contentType(ContentType.Application.Json)
//      setBody("""{"userId":$userId,"preferences":${Json.encodeToString(preferences)}}""")
//    }
//
//    // Verify response
//    TestUtils.verifyResponseStatus(response, HttpStatusCode.OK)
//    TestUtils.verifyResponseContains(response, "updated successfully")
//
//    // Test with invalid user ID
//    val invalidResponse = client.post("/registration/additional-preferences") {
//      contentType(ContentType.Application.Json)
//      setBody("""{"userId":999,"preferences":${Json.encodeToString(preferences)}}""")
//    }
//
//    // Verify response
//    TestUtils.verifyResponseStatus(invalidResponse, HttpStatusCode.BadRequest)
//  }
//}
