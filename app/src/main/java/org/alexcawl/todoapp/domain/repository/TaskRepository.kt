package org.alexcawl.todoapp.domain.repository

import android.util.Log
import kotlinx.coroutines.flow.*
import org.alexcawl.todoapp.data.database.datasource.DatabaseSource
import org.alexcawl.todoapp.data.database.usecases_impl.*
import org.alexcawl.todoapp.data.database.util.RoomState
import org.alexcawl.todoapp.data.network.datasource.NetworkSource
import org.alexcawl.todoapp.data.network.util.NetworkState
import org.alexcawl.todoapp.data.util.toEntity
import org.alexcawl.todoapp.domain.model.DataState
import org.alexcawl.todoapp.domain.model.Priority
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.util.NetworkException
import org.alexcawl.todoapp.domain.util.ValidationException
import java.util.*

class TaskRepository(
    private val databaseSource: DatabaseSource,
    private val networkSource: NetworkSource
) {
    fun getAllTasks(): Flow<DataState<List<TaskModel>>> = flow {
        emit(DataState.Initial)
        databaseSource.getTasks().collect { roomState ->
            when (roomState) {
                is RoomState.Success -> {
                    val tasks: List<TaskModel> = roomState.data
                    emit(DataState.Deprecated(tasks))
                    networkSource.patchTasks(tasks)
                        .collect { networkState ->
                            when (networkState) {
                                is NetworkState.Success -> databaseSource.synchronizeData(
                                    networkState.data,
                                    networkState.revision
                                ).also { emit(DataState.Latest(networkState.data)) }
                                is NetworkState.Failure -> emit(DataState.Deprecated(tasks))
                                else -> {}
                            }
                        }
                }
                is RoomState.Failure -> emit(DataState.Exception(roomState.cause))
                else -> {}
            }
        }
    }

    fun getUncompletedTasks(): Flow<DataState<List<TaskModel>>> = getAllTasks().map { dataState ->
        when (dataState) {
            is DataState.Deprecated -> DataState.Deprecated(dataState.data.filter { !it.isDone })
            is DataState.Latest -> DataState.Latest(dataState.data.filter { !it.isDone })
            else -> dataState
        }
    }

    fun getTaskById(id: UUID): Flow<DataState<TaskModel>> = flow {
        emit(DataState.Initial)
        databaseSource.getTask(id).collect { roomState ->
            when (roomState) {
                is RoomState.Success -> emit(DataState.Latest(roomState.data))
                is RoomState.Failure -> emit(DataState.Exception(roomState.cause))
                else -> {}
            }
        }
    }.catch {
        emit(DataState.Exception(it))
    }

    @Throws(ValidationException::class, NetworkException::class)
    suspend fun addTask(
        text: String,
        priority: Priority,
        deadline: Long?
    ) {
        validateTask(text, deadline)
        val task = buildTaskModel(text, priority, deadline)
        networkSource.postTask(task).collect { networkState ->
            when (networkState) {
                is NetworkState.Success -> {
                    databaseSource.updateTask(
                        buildTaskModel(
                            text,
                            priority,
                            deadline
                        ).toEntity()
                    )
                }
                is NetworkState.Failure -> throw NetworkException("Unsynchronized data!")
                else -> {}
            }
        }
    }

    @Throws(NetworkException::class)
    suspend fun removeTask(task: TaskModel) {
        networkSource.deleteTask(task).collect { networkState ->
            when (networkState) {
                is NetworkState.Success -> databaseSource.removeTask(task.toEntity())
                is NetworkState.Failure -> throw NetworkException("Unsynchronized data!")
                else -> {}
            }
        }
    }

    @Throws(ValidationException::class, NetworkException::class)
    suspend fun updateTask(task: TaskModel) {
        validateTask(task.text, task.deadline)
        networkSource.putTask(task).collect { networkState ->
            when (networkState) {
                is NetworkState.Success -> databaseSource.updateTask(
                    task.copy(modifyingTime = System.currentTimeMillis()).toEntity()
                )
                is NetworkState.Failure -> throw NetworkException("Unsynchronized data!")
                else -> {}
            }
        }
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