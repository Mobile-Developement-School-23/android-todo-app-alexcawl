package org.alexcawl.todoapp.domain.service

import org.alexcawl.todoapp.data.util.NetworkException

interface SynchronizeService {
    fun get(): Int

    fun set(revision: Int)

    @Throws(NetworkException::class)
    suspend fun synchronize()
}