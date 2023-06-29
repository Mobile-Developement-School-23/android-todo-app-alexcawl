package org.alexcawl.todoapp.domain.repository

import kotlinx.coroutines.flow.*
import org.alexcawl.todoapp.data.database.datasource.DatabaseSource
import org.alexcawl.todoapp.data.database.usecases_impl.*
import org.alexcawl.todoapp.data.network.datasource.NetworkSource
import org.alexcawl.todoapp.data.util.DataState
import org.alexcawl.todoapp.data.util.toEntity
import org.alexcawl.todoapp.domain.model.Priority
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.util.ValidationException
import java.util.*

class TaskRepository(
    private val databaseSource: DatabaseSource,
    private val networkSource: NetworkSource
) {

    fun getAllTasks(): Flow<DataState<List<TaskModel>>> =
        databaseSource.getAllTasks()

    fun getUncompletedTasks(): Flow<DataState<List<TaskModel>>> =
        databaseSource.getUncompletedTasks()

    fun getTaskById(id: UUID): Flow<DataState<TaskModel>> =
        databaseSource.getTask(id.toString())

    @Throws(ValidationException::class)
    suspend fun addTask(
        text: String,
        priority: Priority,
        deadline: Long?
    ) {
        validateTask(text, deadline)
        databaseSource.updateTask(buildTaskModel(text, priority, deadline).toEntity())
    }

    suspend fun removeTask(task: TaskModel) {
        databaseSource.removeTask(task.toEntity())
    }

    @Throws(ValidationException::class)
    suspend fun updateTask(task: TaskModel) {
        validateTask(task.text, task.deadline)
        databaseSource.updateTask(task.copy(modifyingTime = System.currentTimeMillis()).toEntity())
    }

    @Throws(ValidationException::class)
    private fun validateTask(text: String, deadline: Long?) {
        if (text.isBlank()) {
            throw ValidationException("Text is blank!")
        }
        if ((deadline ?: 0) < 0) {
            throw ValidationException("Deadline: $deadline is not valid!")
        }
    }

    private fun getUUID(): UUID = UUID.randomUUID()

    private fun buildTaskModel(
        text: String, priority: Priority, deadline: Long?
    ): TaskModel = TaskModel(
        getUUID(), text, priority, false, System.currentTimeMillis(), deadline, null
    )
}