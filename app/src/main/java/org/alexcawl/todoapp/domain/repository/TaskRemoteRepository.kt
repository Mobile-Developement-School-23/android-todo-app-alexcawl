package org.alexcawl.todoapp.domain.repository

import org.alexcawl.todoapp.data.util.NetworkException
import org.alexcawl.todoapp.domain.model.TaskModel

interface TaskRemoteRepository {
    @Throws(NetworkException::class)
    suspend fun addTask(task: TaskModel)

    @Throws(NetworkException::class)
    suspend fun updateTask(task: TaskModel)

    @Throws(NetworkException::class)
    suspend fun deleteTask(task: TaskModel)
}