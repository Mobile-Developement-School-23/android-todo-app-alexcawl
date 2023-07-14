package org.alexcawl.todoapp.domain.repository

import kotlinx.coroutines.flow.Flow
import org.alexcawl.todoapp.domain.model.ValidationException
import org.alexcawl.todoapp.domain.model.DataState
import org.alexcawl.todoapp.domain.model.Priority
import org.alexcawl.todoapp.domain.model.TaskModel
import java.util.UUID

interface ITaskLocalRepository {
    fun getTasks(): Flow<DataState<List<TaskModel>>>

    fun getTask(id: UUID): Flow<DataState<TaskModel>>

    @Throws(ValidationException::class)
    suspend fun addTask(text: String, priority: Priority, deadline: Long?)

    @Throws(ValidationException::class)
    suspend fun updateTask(task: TaskModel)

    suspend fun deleteTask(task: TaskModel)
}