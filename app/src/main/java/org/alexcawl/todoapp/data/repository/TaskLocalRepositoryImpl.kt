package org.alexcawl.todoapp.data.repository

import kotlinx.coroutines.flow.*
import org.alexcawl.todoapp.data.database.datasource.DatabaseSource
import org.alexcawl.todoapp.data.database.util.RoomState
import org.alexcawl.todoapp.data.usecases.*
import org.alexcawl.todoapp.data.util.NetworkException
import org.alexcawl.todoapp.data.util.ValidationException
import org.alexcawl.todoapp.domain.model.DataState
import org.alexcawl.todoapp.domain.model.Priority
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.repository.TaskRemoteRepository
import org.alexcawl.todoapp.domain.repository.TaskLocalRepository
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskLocalRepositoryImpl @Inject constructor(
    private val databaseSource: DatabaseSource,
    private val taskRemoteRepository: TaskRemoteRepository
) : TaskLocalRepository {
    override fun getTasks(): Flow<DataState<List<TaskModel>>> = flow {
        databaseSource.getTasks().collect {
            when (it) {
                is RoomState.Initial -> DataState.Initial
                is RoomState.Success -> emit(DataState.Result(it.data))
                is RoomState.Failure -> emit(DataState.Exception(it.cause))
            }
        }
    }

    override fun getTask(id: UUID): Flow<DataState<TaskModel>> = flow {
        databaseSource.getTask(id).collect {
            when (it) {
                is RoomState.Initial -> DataState.Initial
                is RoomState.Success -> emit(DataState.Result(it.data))
                is RoomState.Failure -> emit(DataState.Exception(it.cause))
            }
        }
    }.catch {
        emit(DataState.Exception(it))
    }

    @Throws(ValidationException::class, NetworkException::class)
    override suspend fun addTask(text: String, priority: Priority, deadline: Long?) {
        validateTask(text, deadline)
        val task = buildTaskModel(text, priority, deadline)
        databaseSource.updateTask(task)
        taskRemoteRepository.addTask(task)
    }

    @Throws(NetworkException::class)
    override suspend fun deleteTask(task: TaskModel) {
        databaseSource.deleteTask(task)
        taskRemoteRepository.deleteTask(task)
    }

    @Throws(ValidationException::class, NetworkException::class)
    override suspend fun updateTask(task: TaskModel) {
        validateTask(task.text, task.deadline)
        databaseSource.updateTask(task.copy(modifyingTime = System.currentTimeMillis()))
        taskRemoteRepository.updateTask(task)
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
        getUUID(),
        text,
        priority,
        false,
        System.currentTimeMillis(),
        System.currentTimeMillis(),
        deadline,
    )
}