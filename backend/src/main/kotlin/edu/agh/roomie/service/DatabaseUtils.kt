package edu.agh.roomie.service

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

/**
 * Utility function for database transactions that uses the IO dispatcher.
 * This centralizes the dispatcher usage and allows for consistent error handling.
 */
suspend fun <T> dbQuery(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) {
        block()
    }