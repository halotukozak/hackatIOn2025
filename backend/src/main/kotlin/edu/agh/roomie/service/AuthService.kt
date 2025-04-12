package edu.agh.roomie.service

import java.util.Base64
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class AuthService {
    // In a real application, you would use a proper JWT library
    // This is a simple implementation for demonstration purposes
    private val tokenStore = ConcurrentHashMap<String, Int>()
    
    fun generateToken(userId: Int): String {
        val randomToken = UUID.randomUUID().toString()
        val userIdEncoded = Base64.getEncoder().encodeToString(userId.toString().toByteArray())
        val token = "$randomToken.$userIdEncoded"
        
        tokenStore[token] = userId
        
        return token
    }
    
    fun validateToken(token: String): Int? {
        return tokenStore[token]
    }
    
    fun removeToken(token: String) {
        tokenStore.remove(token)
    }
}