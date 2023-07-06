package org.alexcawl.todoapp.domain.repository

import org.alexcawl.todoapp.data.util.NetworkException

interface Synchronizer {
    @Throws(NetworkException::class)
    suspend fun synchronize()
}