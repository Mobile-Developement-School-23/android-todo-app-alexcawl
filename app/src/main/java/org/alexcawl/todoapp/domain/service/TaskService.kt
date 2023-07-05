package org.alexcawl.todoapp.domain.service

import kotlinx.coroutines.flow.Flow
import org.alexcawl.todoapp.data.util.NetworkException
import org.alexcawl.todoapp.data.util.ValidationException
import org.alexcawl.todoapp.domain.model.DataState
import org.alexcawl.todoapp.domain.model.Priority
import org.alexcawl.todoapp.domain.model.TaskModel
import java.util.UUID

interface TaskService {
    fun getTasks(): Flow<DataState<List<TaskModel>>>

    fun getTask(id: UUID): Flow<DataState<TaskModel>>

    @Throws(ValidationException::class, NetworkException::class)
    suspend fun addTask(text: String, priority: Priority, deadline: Long?)

    @Throws(ValidationException::class, NetworkException::class)
    suspend fun updateTask(task: TaskModel)

    @Throws(NetworkException::class)
    suspend fun deleteTask(task: TaskModel)
}