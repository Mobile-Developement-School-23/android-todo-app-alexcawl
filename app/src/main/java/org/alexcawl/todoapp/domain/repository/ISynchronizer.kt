package org.alexcawl.todoapp.domain.repository

import org.alexcawl.todoapp.data.util.NetworkException

interface ISynchronizer {
    @Throws(NetworkException::class)
    suspend fun synchronize()
}