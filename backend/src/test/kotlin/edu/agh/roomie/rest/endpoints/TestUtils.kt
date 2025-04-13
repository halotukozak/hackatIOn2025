package edu.agh.roomie.rest.endpoints

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Utility functions for testing HTTP endpoints.
 */
object TestUtils {
    /**
     * Verifies that the HTTP response has the expected status code.
     */
    suspend fun verifyResponseStatus(response: HttpResponse, expectedStatus: HttpStatusCode) {
        assertEquals(expectedStatus, response.status, "Response status should match expected status")
    }

    /**
     * Verifies that the HTTP response body contains the expected text.
     */
    suspend fun verifyResponseContains(response: HttpResponse, expectedText: String) {
        val responseText = response.bodyAsText()
        assertTrue(responseText.contains(expectedText), "Response should contain '$expectedText'")
    }

    /**
     * Verifies that the HTTP response has the expected status code and contains the expected text.
     */
    suspend fun verifyResponse(response: HttpResponse, expectedStatus: HttpStatusCode, expectedText: String) {
        verifyResponseStatus(response, expectedStatus)
        verifyResponseContains(response, expectedText)
    }

    /**
     * Makes a GET request to the specified URL and returns the response.
     */
    suspend fun HttpClient.performGet(url: String, headers: Map<String, String> = emptyMap()): HttpResponse {
        return get(url) {
            headers.forEach { (key, value) ->
                header(key, value)
            }
        }
    }

    /**
     * Makes a POST request to the specified URL with the given body and returns the response.
     */
    suspend fun HttpClient.performPost(
        url: String,
        body: String,
        contentType: ContentType = ContentType.Application.Json,
        headers: Map<String, String> = emptyMap()
    ): HttpResponse {
        return post(url) {
            contentType(contentType)
            setBody(body)
            headers.forEach { (key, value) ->
                header(key, value)
            }
        }
    }

    /**
     * Makes a DELETE request to the specified URL with the given body and returns the response.
     */
    suspend fun HttpClient.performDelete(
        url: String,
        body: String? = null,
        contentType: ContentType = ContentType.Application.Json,
        headers: Map<String, String> = emptyMap()
    ): HttpResponse {
        return delete(url) {
            if (body != null) {
                contentType(contentType)
                setBody(body)
            }
            headers.forEach { (key, value) ->
                header(key, value)
            }
        }
    }
}